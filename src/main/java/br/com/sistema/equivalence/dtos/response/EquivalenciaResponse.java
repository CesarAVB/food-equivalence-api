package br.com.sistema.equivalence.dtos.response;

import java.util.List;

public record EquivalenciaResponse(
        AlimentoDTO alimentoSelecionado,
        Double quantidade,
        Double calorias,
        List<EquivalenteDTO> equivalentes
) {}