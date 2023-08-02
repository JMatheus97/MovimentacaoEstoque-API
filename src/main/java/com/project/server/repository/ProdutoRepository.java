package com.project.server.repository;

import com.project.server.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("select p from Produto p where p.codigoDeBarra = ?1")
    Produto findByCodigoDeBarra(String codigoDeBarra);
}
