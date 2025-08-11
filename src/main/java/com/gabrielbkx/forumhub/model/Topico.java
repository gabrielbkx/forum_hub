package com.gabrielbkx.forumhub.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "topicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String titulo;

    @NotBlank
    @Lob
    private String mensagem;

    private LocalDateTime dataCriacao;

    @NotBlank
    private String status;

    // Muitos tópicos para um autor
    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    @JsonBackReference  // evita ciclo ao serializar
    private Usuario autor;

    // Muitos tópicos para um curso
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    @JsonBackReference(value = "curso-topicos")
    private Curso curso;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Resposta> respostas;
}