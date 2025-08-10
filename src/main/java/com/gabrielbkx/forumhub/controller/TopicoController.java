package com.gabrielbkx.forumhub.controller;

import com.gabrielbkx.forumhub.dto.TopicoDTO;
import com.gabrielbkx.forumhub.dto.TopicoDetalhadoDTO;
import com.gabrielbkx.forumhub.dto.TopicoListagemDTO;
import com.gabrielbkx.forumhub.model.Curso;
import com.gabrielbkx.forumhub.model.Topico;
import com.gabrielbkx.forumhub.model.Usuario;
import com.gabrielbkx.forumhub.repository.CursoRepository;
import com.gabrielbkx.forumhub.repository.TopicoRepository;
import com.gabrielbkx.forumhub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/topicos")
@RequiredArgsConstructor
public class TopicoController {

    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid TopicoDTO dto) {


        if (topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem())) {
            return ResponseEntity.badRequest().body("Já existe um tópico com o mesmo título e mensagem.");
        }

        Usuario autor = usuarioRepository.findById(dto.autorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));


        Curso curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));


        Topico topico = Topico.builder()
                .titulo(dto.titulo())
                .mensagem(dto.mensagem())
                .status("ABERTO")
                .dataCriacao(LocalDateTime.now())
                .autor(autor)
                .curso(curso)
                .build();

        topicoRepository.save(topico);

        return ResponseEntity
                .created(URI.create("/topicos/" + topico.getId()))
                .body(topico);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalhar(@PathVariable Long id) {

        return topicoRepository.findById(id)
                .map(topico -> {
                    TopicoDetalhadoDTO dto = new TopicoDetalhadoDTO(
                            topico.getId(),
                            topico.getTitulo(),
                            topico.getMensagem(),
                            topico.getDataCriacao(),
                            topico.getStatus(),
                            topico.getAutor().getNome(),
                            topico.getCurso().getNome()
                    );
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String nomeCurso,
            @RequestParam(required = false) Integer ano,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<Topico> pagina = topicoRepository.findByFiltro(nomeCurso, ano, pageable);

        Page<TopicoListagemDTO> resultado = pagina.map(topico ->
                new TopicoListagemDTO(
                        topico.getId(),
                        topico.getTitulo(),
                        topico.getMensagem(),
                        topico.getDataCriacao(),
                        topico.getStatus(),
                        topico.getAutor().getNome(),
                        topico.getCurso().getNome()
                )
        );

        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid TopicoDTO dto) {
        // Verifica se o tópico existe
        var topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verifica duplicidade (exceto para o próprio tópico)
        if (topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem())) {
            var existente = topicoRepository.findAll().stream()
                    .filter(t -> t.getTitulo().equals(dto.titulo()) && t.getMensagem().equals(dto.mensagem()))
                    .findFirst();
            if (existente.isPresent() && !existente.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Já existe um tópico com o mesmo título e mensagem.");
            }
        }

        // Busca autor
        var autor = usuarioRepository.findById(dto.autorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        // Busca curso
        var curso = cursoRepository.findById(dto.cursoId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        // Atualiza os dados
        var topico = topicoOptional.get();
        topico.setTitulo(dto.titulo());
        topico.setMensagem(dto.mensagem());
        topico.setAutor(autor);
        topico.setCurso(curso);

        topicoRepository.save(topico);

        return ResponseEntity.ok(topico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        var topicoOptional = topicoRepository.findById(id);

        if (topicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
