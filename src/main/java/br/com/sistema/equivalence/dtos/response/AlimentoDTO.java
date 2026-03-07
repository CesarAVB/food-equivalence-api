package br.com.sistema.equivalence.dtos.response;

import br.com.sistema.equivalence.enums.GrupoAlimentar;

public record AlimentoDTO(
        Integer id,
        String codigoTaco,
        GrupoAlimentar grupo,
        String descricao,
        Double energiaKcal
) {}