package com.project.server.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDTO {

	private Long id;
	private String userName;
	private String perfil;
}
