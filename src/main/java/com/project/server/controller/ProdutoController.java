package com.project.server.controller;

import com.project.server.model.Produto;
import com.project.server.services.ProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Produto")
@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;


    @ApiOperation(value = "Retorna todos os produtos")
    @GetMapping
    public List<Produto> findAll(){
        return produtoService.findAll();
    }

    @ApiOperation(value = "Retorna um produto")
    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findById(id));
    }

    @ApiOperation(value = "Cadastra novo produto")
    @PostMapping
    public ResponseEntity<Produto> create(@RequestBody Produto produto){
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.create(produto));
    }

    @ApiOperation(value = "Editar produto")
    @PutMapping
    public ResponseEntity<Produto> edit(@RequestBody Produto produto) throws IllegalAccessException {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.edit(produto));
    }


    @ApiOperation(value = "Deletar produto")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        produtoService.delete(id);
    }

}
