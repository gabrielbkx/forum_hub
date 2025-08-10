package com.gabrielbkx.forumhub.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record UsuarioDTO(
        @NotBlank String nome,
        @Email @NotBlank String email,
        @NotBlank String senha,
        Set<Long> perfisIds
) {}
