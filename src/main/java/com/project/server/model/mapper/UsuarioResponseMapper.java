package com.project.server.model.mapper;

import com.project.server.model.Usuario;
import com.project.server.model.dto.UsuarioDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsuarioResponseMapper {

    public static UsuarioDTO usuarioDTO(Usuario usuario){
        return Optional.ofNullable(usuario)
                .map(u -> UsuarioDTO.builder()
                    .id(u.getId())
                    .userName(u.getUsername())
                    .perfil(u.getPerfil())
                    .build())
                .orElse(null);
    }

}
