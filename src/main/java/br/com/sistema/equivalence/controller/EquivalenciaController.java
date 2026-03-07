package br.com.sistema.equivalence.controller;

import br.com.sistema.equivalence.dtos.request.CalcularEquivalenciasRequest;
import br.com.sistema.equivalence.dtos.response.EquivalenciaResponse;
import br.com.sistema.equivalence.service.EquivalenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/equivalencias")
@RequiredArgsConstructor
public class EquivalenciaController {

    private final EquivalenciaService equivalenciaService;

    // ====================================================
    // Métodos - Calcular equivalências de alimentos
    // ====================================================
    @PostMapping("/calcular")
    public ResponseEntity<EquivalenciaResponse> calcularEquivalencias(@Valid @RequestBody CalcularEquivalenciasRequest request) {
        EquivalenciaResponse response = equivalenciaService.calcularEquivalencias(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}