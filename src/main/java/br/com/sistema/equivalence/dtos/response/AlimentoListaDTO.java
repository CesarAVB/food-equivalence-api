package br.com.sistema.equivalence.dtos.response;

public record AlimentoListaDTO(
  Integer id,
  String descricao,
  String grupo,
  Double energiaKcal
) {}