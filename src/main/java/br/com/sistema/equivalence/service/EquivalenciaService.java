package br.com.sistema.equivalence.service;

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
    // Métodos - Listar Grupos Disponíveis
    // ====================================================
    public List<String> listarGrupos() {
        return alimentoRepository.findDistinctGrupos();
    }

    // ====================================================
    // Métodos - Listar Alimentos por Grupo
    // ====================================================
    public List<AlimentoListaDTO> listarAlimentosPorGrupo(GrupoAlimentar grupo) {
        List<Alimento> alimentos = alimentoRepository.findByGrupo(grupo);
        
        return alimentos.stream()
            .map(alimento -> new AlimentoListaDTO(
                alimento.getId(),
                alimento.getDescricao(),
                alimento.getGrupo().getDescricao(),
                alimento.getEnergiaKcal()
            ))
            .collect(Collectors.toList());
    }

    // ====================================================
    // Métodos - Buscar Alimentos por Descrição
    // ====================================================
    public List<AlimentoListaDTO> buscarAlimentos(String descricao) {
        List<Alimento> alimentos = alimentoRepository.findByDescricaoIgnoreCaseContaining(descricao);
        
        return alimentos.stream()
            .map(alimento -> new AlimentoListaDTO(
                alimento.getId(),
                alimento.getDescricao(),
                alimento.getGrupo().getDescricao(),
                alimento.getEnergiaKcal()
            ))
            .collect(Collectors.toList());
    }

    // ====================================================
    // Métodos - Calcular Equivalências
    // ====================================================
    public EquivalenciaResponse calcularEquivalencias(CalcularEquivalenciasRequest request) {
        validarQuantidade(request.getQuantidade());
        
        Alimento alimentoSelecionado = buscarAlimentoPorId(request.getAlimentoId());
        
        Double caloriasSelecionadas = calcularCalorias(alimentoSelecionado.getEnergiaKcal(), request.getQuantidade());
        
        List<Alimento> alimentosMesmoGrupo = alimentoRepository.findByGrupo(alimentoSelecionado.getGrupo());
        
        List<EquivalenteDTO> equivalentes = gerarEquivalentes(alimentosMesmoGrupo, alimentoSelecionado, caloriasSelecionadas);
        
        return new EquivalenciaResponse(
            converterParaDTO(alimentoSelecionado),
            request.getQuantidade(),
            caloriasSelecionadas,
            equivalentes
        );
    }

    // ====================================================
    // Métodos Privados - Validações
    // ====================================================
    private void validarQuantidade(Double quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new QuantidadeInvalidaException("Quantidade deve ser maior que 0");
        }
    }

    private Alimento buscarAlimentoPorId(Integer alimentoId) {
        return alimentoRepository.findById(alimentoId)
            .orElseThrow(() -> new AlimentoNaoEncontradoException("Alimento com ID " + alimentoId + " não encontrado"));
    }

    // ====================================================
    // Métodos Privados - Cálculos
    // ====================================================
    private Double calcularCalorias(Double kcalPor100g, Double quantidade) {
        return (kcalPor100g / 100) * quantidade;
    }

    private List<EquivalenteDTO> gerarEquivalentes(List<Alimento> alimentos, Alimento alimentoSelecionado, Double caloriasAlvo) {
        return alimentos.stream()
            .filter(alimento -> !alimento.getId().equals(alimentoSelecionado.getId()))
            .map(alimento -> calcularEquivalente(alimento, caloriasAlvo))
            .collect(Collectors.toList());
    }

    private EquivalenteDTO calcularEquivalente(Alimento alimento, Double caloriasAlvo) {
        Double quantidadeEquivalente = (caloriasAlvo * 100) / alimento.getEnergiaKcal();
        Double diferencaPercentual = Math.abs((alimento.getEnergiaKcal() - 100) / 100.0);
        
        return new EquivalenteDTO(
            alimento.getId(),
            alimento.getDescricao(),
            Math.round(quantidadeEquivalente * 100.0) / 100.0,
            alimento.getEnergiaKcal(),
            caloriasAlvo,
            diferencaPercentual
        );
    }

    // ====================================================
    // Métodos Privados - Conversões
    // ====================================================
    private AlimentoDTO converterParaDTO(Alimento alimento) {
        return new AlimentoDTO(
            alimento.getId(),
            alimento.getCodigoTaco(),
            alimento.getGrupo(),
            alimento.getDescricao(),
            alimento.getEnergiaKcal()
        );
    }
}