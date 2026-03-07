package br.com.sistema.equivalence.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CalcularEquivalenciasRequest(
        @NotNull(message = "ID do alimento é obrigatório")
        Integer alimentoId,

        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser maior que 0")
        Double quantidade
) {}