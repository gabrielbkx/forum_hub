package com.gabrielbkx.forumhub.controller;

import com.gabrielbkx.forumhub.dto.DadosCadastroUsuario;
import com.gabrielbkx.forumhub.model.Perfil;
import com.gabrielbkx.forumhub.model.Usuario;
import com.gabrielbkx.forumhub.repository.PerfilRepository;
import com.gabrielbkx.forumhub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             PerfilRepository perfilRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> cadastrarUsuario(@RequestBody @Valid DadosCadastroUsuario dados) {

        // Verificar se email já existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(dados.getEmail());
        if (usuarioExistente.isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        // Buscar perfil padrão (exemplo: ROLE_USUARIO)
        Perfil perfilPadrao = perfilRepository.findByNome("ROLE_USUARIO")
                .orElseThrow(() -> new RuntimeException("Perfil padrão não encontrado"));

        // Criar usuário e salvar
        Usuario usuario = Usuario.builder()
                .nome(dados.getNome())
                .email(dados.getEmail())
                .senha(passwordEncoder.encode(dados.getSenha()))
                .perfis(Collections.singleton(perfilPadrao))
                .build();

        usuarioRepository.save(usuario);

        URI uri = URI.create("/usuarios/" + usuario.getId());

        return ResponseEntity.created(uri).build();
    }
}
