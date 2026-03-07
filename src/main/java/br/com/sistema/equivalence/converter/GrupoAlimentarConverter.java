package br.com.sistema.equivalence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import br.com.sistema.equivalence.enums.GrupoAlimentar;

@Converter(autoApply = true)
public class GrupoAlimentarConverter implements AttributeConverter<GrupoAlimentar, String> {

    // ====================================================
    // Converter Entity para Banco (Enum → String/ENUM)
    // ====================================================
    @Override
    public String convertToDatabaseColumn(GrupoAlimentar attribute) {
        if (attribute == null) {
            return null;
        }
        // Retorna a descrição: CARBOIDRATOS → "Carboidratos"
        return attribute.getDescricao();
    }

    // ====================================================
    // Converter Banco para Entity (String/ENUM → Enum)
    // ====================================================
    @Override
    public GrupoAlimentar convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        // Busca o Enum pela descrição: "Carboidratos" → CARBOIDRATOS
        for (GrupoAlimentar grupo : GrupoAlimentar.values()) {
            if (grupo.getDescricao().equals(dbData)) {
                return grupo;
            }
        }
        throw new IllegalArgumentException("Grupo não encontrado: " + dbData);
    }
}