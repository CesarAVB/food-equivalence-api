package br.com.sistema.equivalence.enums;

import lombok.Getter;

@Getter
public enum GrupoAlimentar {
    FRUTAS("Frutas"),
    CARBOIDRATOS("Carboidratos"),
    PROTEINA("Proteína"),
    LATICINEOS("Laticíneos"),
    GORDURA_VEGETAL("Gordura Vegetal");

    private final String descricao;

    GrupoAlimentar(String descricao) {
        this.descricao = descricao;
    }
}