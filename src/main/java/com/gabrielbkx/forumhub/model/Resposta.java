package com.gabrielbkx.forumhub.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "respostas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Lob
    private String mensagem;

    private LocalDateTime dataCriacao;

    private Boolean solucao;

    @ManyToOne
    @JoinColumn(name = "topico_id", nullable = false)
    @JsonBackReference
    private Topico topico;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    @JsonManagedReference
    private Usuario autor;

}
