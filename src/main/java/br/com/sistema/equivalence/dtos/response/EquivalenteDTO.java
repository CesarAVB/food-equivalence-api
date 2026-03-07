package br.com.sistema.equivalence.dtos.response;

public record EquivalenteDTO(
        Integer id,
        String descricao,
        Double quantidadeEquivalenteG,
        Double kcalPor100g,
        Double kcalTotais,
        Double diferencaPercentual
) {}