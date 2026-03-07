package br.com.sistema.equivalence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.sistema.equivalence.entity.Alimento;
import br.com.sistema.equivalence.enums.GrupoAlimentar;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {

    // ====================================================
    // Métodos - Buscar por Grupo
    // ====================================================
    List<Alimento> findByGrupo(GrupoAlimentar grupo);

    // ====================================================
    // Métodos - Buscar por Descrição
    // ====================================================
    List<Alimento> findByDescricaoIgnoreCaseContaining(String descricao);

    // ====================================================
    // Métodos - Grupos Distintos (Retorna String)
    // ====================================================
    @Query("SELECT DISTINCT a.grupo.descricao FROM Alimento a ORDER BY a.grupo.descricao")
    List<String> findDistinctGrupos();
}