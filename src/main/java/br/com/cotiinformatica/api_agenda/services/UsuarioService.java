package br.com.cotiinformatica.api_agenda.services;

import br.com.cotiinformatica.api_agenda.dtos.CriarUsuarioRequest;
import br.com.cotiinformatica.api_agenda.dtos.CriarUsuarioResponse;
import br.com.cotiinformatica.api_agenda.entities.Usuario;
import br.com.cotiinformatica.api_agenda.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /*
        Método para criar um usuário no sistema
     */
    public CriarUsuarioResponse criarUsuario(CriarUsuarioRequest request) throws Exception {

        //Criando um objeto da entidade Usuario
        var usuario = new Usuario();

        //Preencher os dados do usuário
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(criptografarSenha(request.senha()));
        usuario.setDataHoraCriacao(LocalDateTime.now());

        //Salvar o usuário no banco de dados
        usuarioRepository.save(usuario);

        //Retornar os dados (resposta)
        return new CriarUsuarioResponse(
                "Usuário cadastrado com sucesso.",
                LocalDateTime.now(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    /*
        Método para criptografar a senha do usuário
     */
    private String criptografarSenha(String senha) throws Exception {
        //Instanciando o algoritmo SHA-256
        var messageDigest = MessageDigest.getInstance("SHA-256");
        //Gerando o hash da senha
        var hash = messageDigest.digest(
                senha.getBytes(StandardCharsets.UTF_8)
        );

        //Convertendo o hash para hexadecimal
        var hexadecimal = new StringBuilder();
        for (byte b : hash) {
            hexadecimal.append(String.format("%02x", b));
        }

        return hexadecimal.toString();
    }

}
