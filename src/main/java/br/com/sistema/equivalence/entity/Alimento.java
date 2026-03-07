package br.com.sistema.equivalence.entity;

import java.time.LocalDateTime;

import br.com.sistema.equivalence.enums.GrupoAlimentar;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_alimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_taco", nullable = false)
    private String codigoTaco;

    @Enumerated(EnumType.STRING)
    @Column(name = "grupo", nullable = false)
    private GrupoAlimentar grupo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "umidade")
    private Double umidade;

    @Column(name = "energia_kcal", nullable = false)
    private Double energiaKcal;

    @Column(name = "energia_kj")
    private Double energiaKj;

    @Column(name = "proteina_g")
    private Double proteinaG;

    @Column(name = "lipideos_g")
    private Double lipideosG;

    @Column(name = "colesterol_mg")
    private Double colesterolMg;

    @Column(name = "carboidratos_g")
    private Double carboidratosG;

    @Column(name = "fibra_alimentar_g")
    private Double fibraAlimentarG;

    @Column(name = "cinzas_g")
    private Double cinzasG;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "fonte")
    private String fonte;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "updated_at")
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}