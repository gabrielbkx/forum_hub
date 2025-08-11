package com.gabrielbkx.forumhub.model;



import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String categoria;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "curso-topicos")
    private List<Topico> topicos;
}
