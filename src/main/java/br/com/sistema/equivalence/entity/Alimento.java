package br.com.sistema.equivalence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.sistema.equivalence.enums.GrupoAlimentar;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_substituicao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_substituicao", nullable = false)
    private String codigoTaco;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo", nullable = false)
    private GrupoAlimentar grupo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "energia_kcal", nullable = false)
    private Double energiaKcal;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "updated_at")
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}