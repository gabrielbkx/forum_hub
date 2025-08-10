package com.gabrielbkx.forumhub.controller;


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
import java.util.List;
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

    @GetMapping
    public List<Resposta> listarRespostas() {
        return respostaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resposta> buscarPorId(@PathVariable Long id) {
        Optional<Resposta> resposta = respostaRepository.findById(id);
        return resposta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criarResposta(@RequestBody @Valid Resposta resposta,
                                           @AuthenticationPrincipal UserDetails userDetails) {

        // Validar se o tópico existe
        if (resposta.getTopico() == null || resposta.getTopico().getId() == null) {
            return ResponseEntity.badRequest().body("Tópico inválido");
        }

        Optional<Topico> topicoOpt = topicoRepository.findById(resposta.getTopico().getId());
        if (topicoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Tópico não encontrado");
        }

        // Buscar o usuário autenticado
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(userDetails.getUsername());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        resposta.setTopico(topicoOpt.get());
        resposta.setAutor(usuarioOpt.get());
        resposta.setDataCriacao(LocalDateTime.now());

        Resposta salvo = respostaRepository.save(resposta);

        URI uri = URI.create("/respostas/" + salvo.getId());
        return ResponseEntity.created(uri).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarResposta(@PathVariable Long id,
                                               @RequestBody @Valid Resposta respostaAtualizada,
                                               @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Resposta> respostaOpt = respostaRepository.findById(id);
        if (respostaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resposta resposta = respostaOpt.get();

        // Só autor da resposta pode atualizar (exemplo simples)
        if (!resposta.getAutor().getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).body("Não autorizado a alterar esta resposta");
        }

        resposta.setMensagem(respostaAtualizada.getMensagem());
        resposta.setSolucao(respostaAtualizada.getSolucao());

        Resposta salvo = respostaRepository.save(resposta);

        return ResponseEntity.ok(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarResposta(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        Optional<Resposta> respostaOpt = respostaRepository.findById(id);
        if (respostaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resposta resposta = respostaOpt.get();

        // Só autor da resposta pode deletar (exemplo simples)
        if (!resposta.getAutor().getEmail().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).body("Não autorizado a deletar esta resposta");
        }

        respostaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
