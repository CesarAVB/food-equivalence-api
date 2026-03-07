package br.com.sistema.equivalence.enums;

public enum GrupoAlimentar {
    CEREAIS_E_DERIVADOS("Cereais e derivados"),
    VERDURAS_HORTALICAS_E_DERIVADOS("Verduras, hortaliças e derivados"),
    FRUTAS_E_DERIVADOS("Frutas e derivados"),
    GORDURAS_E_OLEOS("Gorduras e óleos"),
    PESCADOS_E_FRUTOS_DO_MAR("Pescados e frutos do mar"),
    CARNES_E_DERIVADOS("Carnes e derivados"),
    LEITE_E_DERIVADOS("Leite e derivados"),
    BEBIDAS_ALCOOLICAS_E_NAO_ALCOOLICAS("Bebidas (alcoólicas e não alcoólicas)"),
    OVOS_E_DERIVADOS("Ovos e derivados"),
    PRODUTOS_ACUCARADOS("Produtos açucarados"),
    MISCELANEAS("Miscelâneas"),
    OUTROS_ALIMENTOS_INDUSTRIALIZADOS("Outros alimentos industrializados"),
    ALIMENTOS_PREPARADOS("Alimentos preparados"),
    LEGUMINOSAS_E_DERIVADOS("Leguminosas e derivados"),
    NOZES_E_SEMENTES("Nozes e sementes");

    private final String descricao;

    GrupoAlimentar(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}