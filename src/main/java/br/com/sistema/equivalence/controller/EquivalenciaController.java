package br.com.sistema.equivalence.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sistema.equivalence.dtos.request.CalcularEquivalenciasRequest;
import br.com.sistema.equivalence.dtos.response.AlimentoListaDTO;
import br.com.sistema.equivalence.dtos.response.EquivalenciaResponse;
import br.com.sistema.equivalence.enums.GrupoAlimentar;
import br.com.sistema.equivalence.service.EquivalenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/equivalencias")
@RequiredArgsConstructor
public class EquivalenciaController {

    private final EquivalenciaService equivalenciaService;

    // ====================================================
    // Calcular Equivalências de Alimentos
    // ====================================================
    @PostMapping("/calcular")
    public ResponseEntity<EquivalenciaResponse> calcularEquivalencias(@Valid @RequestBody CalcularEquivalenciasRequest request) {
        EquivalenciaResponse response = equivalenciaService.calcularEquivalencias(request);
        return ResponseEntity.ok(response);
    }

    // ====================================================
    // Listar Alimentos por Grupo Específico
    // ====================================================
    @GetMapping("/alimentos/grupo/{grupo}")
    public ResponseEntity<List<AlimentoListaDTO>> listarAlimentosPorGrupo(@PathVariable String grupo) {
        try {
            GrupoAlimentar grupoAlimentar = converterDescricaoParaEnum(grupo); // Converter de "Carboidratos" para GrupoAlimentar.CARBOIDRATOS
            List<AlimentoListaDTO> alimentos = equivalenciaService.listarAlimentosPorGrupo(grupoAlimentar);
            return ResponseEntity.ok(alimentos);
            
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Grupo não encontrado: " + grupo);
            return ResponseEntity.badRequest().build();
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao listar alimentos: " + e.getMessage());
            e.printStackTrace();
            
            return ResponseEntity.status(500).build();
        }
    }

    // ====================================================
    // Listar Todos os Grupos Disponíveis
    // ====================================================
    @GetMapping("/grupos")
    public ResponseEntity<List<String>> listarGrupos() {
        List<String> grupos = equivalenciaService.listarGrupos();
        return ResponseEntity.ok(grupos);
    }

    // ====================================================
    // Buscar Alimentos por Descrição
    // ====================================================
    @GetMapping("/alimentos/buscar")
    public ResponseEntity<List<AlimentoListaDTO>> buscarAlimentos(@RequestParam String descricao) {
        List<AlimentoListaDTO> alimentos = equivalenciaService.buscarAlimentos(descricao);
        return ResponseEntity.ok(alimentos);
    }

    // ====================================================
    // Método Privado - Converter Descrição para Enum
    // ====================================================
    private GrupoAlimentar converterDescricaoParaEnum(String descricao) {
        for (GrupoAlimentar grupo : GrupoAlimentar.values()) {
            if (grupo.getDescricao().equals(descricao)) {
                return grupo;
            }
        }
        throw new IllegalArgumentException("Grupo não encontrado: " + descricao);
    }
}