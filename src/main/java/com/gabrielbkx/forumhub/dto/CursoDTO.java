package com.gabrielbkx.forumhub.dto;


import jakarta.validation.constraints.NotBlank;

public record CursoDTO(
        @NotBlank String nome,
        @NotBlank String categoria
) {}
