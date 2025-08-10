package com.gabrielbkx.forumhub.repository;

import com.gabrielbkx.forumhub.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    boolean existsByTituloAndMensagem(String titulo, String mensagem);

    Page<Topico> findByCurso_NomeContainingIgnoreCaseAndDataCriacaoYear(
            String nomeCurso, int ano, Pageable pageable);

    @Query("SELECT t FROM Topico t WHERE " +
            "(:nomeCurso IS NULL OR LOWER(t.curso.nome) LIKE LOWER(CONCAT('%', :nomeCurso, '%'))) AND " +
            "(:ano IS NULL OR YEAR(t.dataCriacao) = :ano)")
    Page<Topico> findByFiltro(String nomeCurso, Integer ano, Pageable pageable);

}
