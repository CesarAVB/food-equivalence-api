package br.com.sistema.equivalence.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalcularEquivalenciasRequest {

    // ====================================================
    // Atributos - Dados da Requisição
    // ====================================================
    private Integer alimentoId;

    private Double quantidade;
}