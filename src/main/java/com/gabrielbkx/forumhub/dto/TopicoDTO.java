package com.gabrielbkx.forumhub.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record TopicoDTO(
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotNull Long autorId,
        @NotNull Long cursoId
) {}
