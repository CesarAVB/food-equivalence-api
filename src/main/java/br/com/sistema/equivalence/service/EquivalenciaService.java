package br.com.sistema.equivalence.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.sistema.equivalence.dtos.request.CalcularEquivalenciasRequest;
import br.com.sistema.equivalence.dtos.response.AlimentoDTO;
import br.com.sistema.equivalence.dtos.response.AlimentoListaDTO;
import br.com.sistema.equivalence.dtos.response.EquivalenciaResponse;
import br.com.sistema.equivalence.dtos.response.EquivalenteDTO;
import br.com.sistema.equivalence.entity.Alimento;
import br.com.sistema.equivalence.enums.GrupoAlimentar;
import br.com.sistema.equivalence.exception.AlimentoNaoEncontradoException;
import br.com.sistema.equivalence.exception.QuantidadeInvalidaException;
import br.com.sistema.equivalence.repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquivalenciaService {

  private final AlimentoRepository alimentoRepository;

  // ====================================================
  // Métodos - Calcular Equivalências de Alimentos
  // ====================================================
  public EquivalenciaResponse calcularEquivalencias(CalcularEquivalenciasRequest request) {
    validarQuantidade(request.quantidade());

    Alimento alimento = obterAlimentoPorId(request.alimentoId());
    Double caloriasTotais = calcularCaloriasTotais(request.quantidade(), alimento.getEnergiaKcal());

    List<EquivalenteDTO> equivalentes = construirEquivalentes(alimento, caloriasTotais);

    return new EquivalenciaResponse(
      converterParaAlimentoDTO(alimento),
      request.quantidade(),
      caloriasTotais,
      equivalentes
    );
  }

  // ====================================================
  // Métodos - Listar Alimentos por Grupo Específico
  // ====================================================
  public List<AlimentoListaDTO> listarAlimentosPorGrupo(GrupoAlimentar grupo) {
    return alimentoRepository.findByGrupo(grupo).stream().map(alimento -> new AlimentoListaDTO(
        alimento.getId(),
        alimento.getDescricao(),
        alimento.getGrupo().getDescricao(),
        alimento.getEnergiaKcal()
      )).collect(Collectors.toList());
  }

  // ====================================================
  // Métodos - Listar Todos os Grupos Disponíveis
  // ====================================================
  public List<String> listarGrupos() {
    return new ArrayList<>(java.util.Arrays.asList(GrupoAlimentar.values())).stream()
      .map(GrupoAlimentar::getDescricao)
      .sorted()
      .collect(Collectors.toList());
  }

  // ====================================================
  // Métodos - Buscar Alimentos por Descrição
  // ====================================================
  public List<AlimentoListaDTO> buscarAlimentos(String descricao) {
    if (descricao == null || descricao.trim().isEmpty()) {
      throw new IllegalArgumentException("Descrição não pode estar vazia");
    }

    return alimentoRepository.findByDescricaoContainingIgnoreCase(descricao.trim()).stream().map(alimento -> new AlimentoListaDTO(
        alimento.getId(),
        alimento.getDescricao(),
        alimento.getGrupo().getDescricao(),
        alimento.getEnergiaKcal()
      )).collect(Collectors.toList());
  }

  // ====================================================
  // Métodos Privados - Obter Alimento por ID
  // ====================================================
  private Alimento obterAlimentoPorId(Integer id) {
    return alimentoRepository.findById(id).orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento com ID " + id + " não encontrado"));
  }

  // ====================================================
  // Métodos Privados - Validar Quantidade
  // ====================================================
  private void validarQuantidade(Double quantidade) {
    if (quantidade == null || quantidade <= 0) {
      throw new QuantidadeInvalidaException("Quantidade deve ser maior que 0");
    }
  }

  // ====================================================
  // Métodos Privados - Calcular Calorias Totais
  // ====================================================
  private Double calcularCaloriasTotais(Double quantidade, Double kcalPor100g) {
    return (quantidade * kcalPor100g) / 100;
  }

  // ====================================================
  // Métodos Privados - Calcular Quantidade Equivalente
  // ====================================================
  private Double calcularQuantidadeEquivalente(Double caloriasTotais, Double kcalEquivalentePor100g) {
    return (caloriasTotais * 100) / kcalEquivalentePor100g;
  }

  // ====================================================
  // Métodos Privados - Calcular Diferença Percentual
  // ====================================================
  private Double calcularDiferencaPercentual(Double caloriasTotais, Double caloriaEquivalente) {
    return Math.abs(caloriasTotais - caloriaEquivalente) / caloriasTotais * 100;
  }

  // ====================================================
  // Métodos Privados - Converter para DTO Alimento
  // ====================================================
  private AlimentoDTO converterParaAlimentoDTO(Alimento alimento) {
    return new AlimentoDTO(
      alimento.getId(),
      alimento.getCodigoTaco(),
      alimento.getGrupo(),
      alimento.getDescricao(),
      alimento.getEnergiaKcal()
    );
  }

  // ====================================================
  // Métodos Privados - Construir Equivalentes
  // ====================================================
  private List<EquivalenteDTO> construirEquivalentes(Alimento alimentoBase, Double caloriasTotais) {
    List<Alimento> alimentosDoGrupo = alimentoRepository.findByGrupoAndIdNot(alimentoBase.getGrupo(), alimentoBase.getId());

    return alimentosDoGrupo.stream().map(alimento -> {
        Double quantidadeEquivalente = calcularQuantidadeEquivalente(caloriasTotais, alimento.getEnergiaKcal());
        Double caloriaEquivalente = (quantidadeEquivalente * alimento.getEnergiaKcal()) / 100;
        Double diferencaPercentual = calcularDiferencaPercentual(caloriasTotais, caloriaEquivalente);

        return new EquivalenteDTO(
          alimento.getId(),
          alimento.getDescricao(),
          Math.round(quantidadeEquivalente * 100.0) / 100.0,
          alimento.getEnergiaKcal(),
          Math.round(caloriaEquivalente * 100.0) / 100.0,
          Math.round(diferencaPercentual * 100.0) / 100.0
        );
      })
      .sorted((a, b) -> Double.compare(a.diferencaPercentual(), b.diferencaPercentual()))
      .collect(Collectors.toList());
  }
}