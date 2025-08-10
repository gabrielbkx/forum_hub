package com.gabrielbkx.forumhub.controller;


import com.gabrielbkx.forumhub.dto.DadosAutenticacao;
import com.gabrielbkx.forumhub.model.Usuario;
import com.gabrielbkx.forumhub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/auth")
    public class AutenticacaoController {

        private final AuthenticationManager authenticationManager;
        private final TokenService tokenService;

        public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
            this.authenticationManager = authenticationManager;
            this.tokenService = tokenService;
        }

        @PostMapping("/login")
        public ResponseEntity<String> login(@RequestBody @Valid DadosAutenticacao dados) {
            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            String token = tokenService.gerarToken((Usuario) authentication.getPrincipal());

            return ResponseEntity.ok(token);
        }
    }



