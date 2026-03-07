package br.com.sistema.equivalence.enums;

import lombok.Getter;

@Getter
public enum GrupoAlimentar {
    CARBOIDRATOS("Carboidratos"),
    FRUTAS("Frutas"),
    GORDURA_VEGETAL("Gordura Vegetal"),
    LATICINEOS("Laticíneos"),
    PROTEINA("Proteína");

    private final String descricao;

    GrupoAlimentar(String descricao) {
        this.descricao = descricao;
    }
}