package com.project.server.model.dto;

import com.project.server.model.enums.EnumTipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditStatusOrcamentoRequest {

    private Long id;
    private EnumTipoMovimentacao enumTipoMovimentacao;

    private String tipoCotacao;

}
