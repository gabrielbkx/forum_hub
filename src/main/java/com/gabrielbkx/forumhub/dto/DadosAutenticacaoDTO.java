package com.gabrielbkx.forumhub.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacaoDTO(
        @NotBlank String login,
        @NotBlank String senha
) {}
