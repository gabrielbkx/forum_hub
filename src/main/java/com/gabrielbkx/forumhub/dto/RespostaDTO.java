package com.gabrielbkx.forumhub.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RespostaDTO(
        @NotBlank String mensagem,
        Long topicoId
) {}
