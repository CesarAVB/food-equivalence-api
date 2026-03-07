package br.com.sistema.equivalence.repository;

import br.com.sistema.equivalence.entity.Alimento;
import br.com.sistema.equivalence.enums.GrupoAlimentar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {

    // ====================================================
    // Buscar por Grupo
    // ====================================================
    List<Alimento> findByGrupo(GrupoAlimentar grupo);

    // ====================================================
    // Buscar por Descrição
    // ====================================================
    List<Alimento> findByDescricaoIgnoreCaseContaining(String descricao);

    // ====================================================
    // Grupos Distintos
    // ====================================================
    @Query("SELECT DISTINCT a.grupo FROM Alimento a ORDER BY a.grupo")
    List<GrupoAlimentar> findDistinctGruposEnum();
}