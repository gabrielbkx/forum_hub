package com.gabrielbkx.forumhub.controller;


import com.gabrielbkx.forumhub.dto.RespostaDTO;
import com.gabrielbkx.forumhub.model.Resposta;
import com.gabrielbkx.forumhub.model.Topico;
import com.gabrielbkx.forumhub.model.Usuario;
import com.gabrielbkx.forumhub.repository.RespostaRepository;
import com.gabrielbkx.forumhub.repository.TopicoRepository;
import com.gabrielbkx.forumhub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;

import java.util.Optional;

@RestController
@RequestMapping("/respostas")
public class RespostaController {

    private final RespostaRepository respostaRepository;
    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;

    public RespostaController(RespostaRepository respostaRepository,
                              TopicoRepository topicoRepository,
                              UsuarioRepository usuarioRepository) {
        this.respostaRepository = respostaRepository;
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<?> criarResposta(@RequestBody @Valid RespostaDTO dto,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Topico> topicoOpt = topicoRepository.findById(dto.topicoId());
        if (topicoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Tópico não encontrado");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(userDetails.getUsername());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        Resposta resposta = Resposta.builder()
                .mensagem(dto.mensagem())
                .topico(topicoOpt.get())
                .autor(usuarioOpt.get())
                .dataCriacao(LocalDateTime.now())
                .build();

        Resposta salvo = respostaRepository.save(resposta);

        URI uri = URI.create("/respostas/" + salvo.getId());
        return ResponseEntity.created(uri).body(salvo);
    }

}
