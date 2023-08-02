package com.project.server.repository;

import com.project.server.model.MovimentacaoEstoque;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentecaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    @Query("select m from MovimentacaoEstoque m where m.produto.id = ?1")
    List<MovimentacaoEstoque> findByMovimentcaoAndProduto(Long id);

    @Query("SELECT m\n" +
            "FROM MovimentacaoEstoque m\n" +
            "JOIN FETCH m.produto p\n" +
            "WHERE p.id = ?1\n" +
            "AND m.id = (SELECT MAX(m2.id) FROM MovimentacaoEstoque m2 WHERE m2.produto.id = ?1)")
    MovimentacaoEstoque findLastByProdutoId(Long id);

}
