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

    // ====================================================
    // Converter Descrição para Enum
    // ====================================================
    public static GrupoAlimentar fromDescricao(String descricao) {
        for (GrupoAlimentar grupo : GrupoAlimentar.values()) {
            if (grupo.descricao.equals(descricao)) {
                return grupo;
            }
        }
        throw new IllegalArgumentException("Grupo não encontrado: " + descricao);
    }
}