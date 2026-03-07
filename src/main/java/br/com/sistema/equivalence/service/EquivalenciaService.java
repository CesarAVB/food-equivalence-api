package br.com.sistema.equivalence.service;

import br.com.sistema.equivalence.dtos.request.CalcularEquivalenciasRequest;
import br.com.sistema.equivalence.dtos.response.AlimentoDTO;
import br.com.sistema.equivalence.dtos.response.EquivalenteDTO;
import br.com.sistema.equivalence.dtos.response.EquivalenciaResponse;
import br.com.sistema.equivalence.entity.Alimento;
import br.com.sistema.equivalence.exception.AlimentoNaoEncontradoException;
import br.com.sistema.equivalence.exception.QuantidadeInvalidaException;
import br.com.sistema.equivalence.repository.AlimentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquivalenciaService {

    private final AlimentoRepository alimentoRepository;

    // ====================================================
    // Métodos - Buscar alimento por ID e validar
    // ====================================================
    private Alimento obterAlimentoPorId(Integer alimentoId) {
        return alimentoRepository.findById(alimentoId).orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento com ID " + alimentoId + " não encontrado"));
    }

    // ====================================================
    // Métodos - Validar quantidade
    // ====================================================
    private void validarQuantidade(Double quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new QuantidadeInvalidaException("Quantidade deve ser maior que 0");
        }
    }

    // ====================================================
    // Métodos - Calcular calorias totais
    // ====================================================
    private Double calcularCaloriasTotais(Double quantidade, Double energiaKcalPor100g) {
        return (quantidade * energiaKcalPor100g) / 100.0;
    }

    // ====================================================
    // Métodos - Calcular quantidade equivalente em gramas
    // ====================================================
    private Double calcularQuantidadeEquivalente(Double calorias, Double energiaKcalPor100g) {
        return (calorias * 100.0) / energiaKcalPor100g;
    }

    // ====================================================
    // Métodos - Calcular diferença percentual entre calorias
    // ====================================================
    private Double calcularDiferencaPercentual(Double calorias, Double calorias100gEquivalente) {
        if (calorias == 0) {
            return 0.0;
        }
        return Math.abs(calorias - calorias100gEquivalente) / calorias * 100.0;
    }

    // ====================================================
    // Métodos - Converter alimento para DTO
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
    // Métodos - Converter lista de alimentos para DTOs de equivalentes
    // ====================================================
    private List<EquivalenteDTO> construirEquivalentes(Double calorias, List<Alimento> alimentosEquivalentes) {

        return alimentosEquivalentes.stream().map(alimento -> {
                    Double quantidadeEquivalente = calcularQuantidadeEquivalente(calorias, alimento.getEnergiaKcal());
                    Double calorias100gEquivalente = (quantidadeEquivalente * alimento.getEnergiaKcal()) / 100.0;
                    Double diferencaPercentual = calcularDiferencaPercentual(calorias, calorias100gEquivalente);

                    return new EquivalenteDTO(
                            alimento.getId(),
                            alimento.getDescricao(),
                            Math.round(quantidadeEquivalente * 100.0) / 100.0,
                            alimento.getEnergiaKcal(),
                            Math.round(calorias100gEquivalente * 100.0) / 100.0,
                            Math.round(diferencaPercentual * 100.0) / 100.0
                    );
                }).sorted((a, b) -> Double.compare(a.diferencaPercentual(), b.diferencaPercentual())).toList();
    }

    // ====================================================
    // Métodos - Calcular equivalências (método principal)
    // ====================================================
    public EquivalenciaResponse calcularEquivalencias(CalcularEquivalenciasRequest request) {
        validarQuantidade(request.quantidade());
        Alimento alimentoSelecionado = obterAlimentoPorId(request.alimentoId());
        Double calorias = calcularCaloriasTotais(request.quantidade(), alimentoSelecionado.getEnergiaKcal());
        List<Alimento> alimentosEquivalentes = alimentoRepository.findByGrupoAndIdNot(alimentoSelecionado.getGrupo(), alimentoSelecionado.getId());
        List<EquivalenteDTO> equivalentes = construirEquivalentes(calorias, alimentosEquivalentes);

        return new EquivalenciaResponse(
                converterParaAlimentoDTO(alimentoSelecionado),
                request.quantidade(),
                Math.round(calorias * 100.0) / 100.0,
                equivalentes
        );
    }
}