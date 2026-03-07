package br.com.sistema.equivalence.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/equivalencias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EquivalenciaController {

    private final EquivalenciaService equivalenciaService;

    // ====================================================
    // Endpoints - Listar Grupos
    // ====================================================
    @GetMapping("/grupos")
    public ResponseEntity<List<String>> listarGrupos() {
        List<String> grupos = equivalenciaService.listarGrupos();
        return ResponseEntity.ok(grupos);
    }

    // ====================================================
    // Endpoints - Listar Alimentos por Grupo
    // ====================================================
    @GetMapping("/alimentos/grupo/{grupo}")
    public ResponseEntity<List<AlimentoListaDTO>> listarAlimentosPorGrupo(@PathVariable String grupo) {
        // Converter string do frontend para Enum
        GrupoAlimentar grupoEnum = GrupoAlimentar.valueOf(grupo.toUpperCase());
        List<AlimentoListaDTO> alimentos = equivalenciaService.listarAlimentosPorGrupo(grupoEnum);
        return ResponseEntity.ok(alimentos);
    }

    // ====================================================
    // Endpoints - Buscar Alimentos por Descrição
    // ====================================================
    @GetMapping("/alimentos/buscar")
    public ResponseEntity<List<AlimentoListaDTO>> buscarAlimentos(@RequestParam String descricao) {
        List<AlimentoListaDTO> alimentos = equivalenciaService.buscarAlimentos(descricao);
        return ResponseEntity.ok(alimentos);
    }

    // ====================================================
    // Endpoints - Calcular Equivalências
    // ====================================================
    @PostMapping("/calcular")
    public ResponseEntity<EquivalenciaResponse> calcularEquivalencias(@RequestBody CalcularEquivalenciasRequest request) {
        EquivalenciaResponse response = equivalenciaService.calcularEquivalencias(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}