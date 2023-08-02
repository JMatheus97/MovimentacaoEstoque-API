package com.project.server.controller;

import com.project.server.model.Usuario;
import com.project.server.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Usuario")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @ApiOperation(value = "Criar novo usuário")
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(usuario));
    }

    @ApiOperation(value = "Listar todos os usuários")
    @GetMapping
    public List<Usuario> listAll() {
        return usuarioService.findAll();
    }

    @ApiOperation(value = "Buscar usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findOne(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findById(id));
    }


    @ApiOperation(value = "Editar usuário")
    @PutMapping
    public ResponseEntity<Usuario> edit(@RequestBody Usuario usuario) throws IllegalAccessException {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.edit(usuario));
    }

    @ApiOperation(value = "Deletar usuário por ID")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        usuarioService.delete(id);
    }



}
