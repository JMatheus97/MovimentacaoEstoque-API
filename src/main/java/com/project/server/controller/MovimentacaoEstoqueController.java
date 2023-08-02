package com.project.server.controller;

import com.project.server.model.MovimentacaoEstoque;
import com.project.server.services.MovimentacaoEstoqueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Movimentção Estoque")
@RestController
@RequestMapping("/movimentecao")
public class MovimentacaoEstoqueController {

    @Autowired
    private MovimentacaoEstoqueService movimentacaoEstoqueService;


    @ApiOperation(value = "Retorna todos os movimentecões")
    @GetMapping
    public List<MovimentacaoEstoque> findAll(){
        return movimentacaoEstoqueService.findAll();
    }


    @ApiOperation(value = "Retorna um movimentação")
    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoque> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(movimentacaoEstoqueService.findById(id));
    }

    @ApiOperation(value = "Cadastra nova movimentação")
    @PostMapping
    public ResponseEntity<MovimentacaoEstoque> create(@RequestBody MovimentacaoEstoque movimentacaoEstoque){
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoEstoqueService.create(movimentacaoEstoque));
    }

    @ApiOperation(value = "Editar movimentação")
    @PutMapping
    public ResponseEntity<MovimentacaoEstoque> edit(@RequestBody MovimentacaoEstoque movimentacaoEstoque) throws IllegalAccessException {
        return ResponseEntity.status(HttpStatus.OK).body(movimentacaoEstoqueService.edit(movimentacaoEstoque));
    }

    @GetMapping("/ultimo/{produtoId}")
    public ResponseEntity<MovimentacaoEstoque> getLastMovimentacaoEstoque(@PathVariable Long produtoId) {
        MovimentacaoEstoque lastMovimentacao = movimentacaoEstoqueService.findLastByProdutoId(produtoId);
        if (lastMovimentacao != null) {
            return ResponseEntity.ok(lastMovimentacao);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @ApiOperation(value = "Deletar movimentação")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        movimentacaoEstoqueService.delete(id);
    }

}
