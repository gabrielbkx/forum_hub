package com.gabrielbkx.forumhub.dto;


import java.time.LocalDateTime;

public record TopicoDetalhadoDTO(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        String status,
        String autorNome,
        String cursoNome
) {}
