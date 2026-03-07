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
    // Métodos - Buscar por Grupo (Enum)
    // ====================================================
    List<Alimento> findByGrupo(GrupoAlimentar grupo);

    // ====================================================
    // Métodos - Buscar por Descrição
    // ====================================================
    List<Alimento> findByDescricaoIgnoreCaseContaining(String descricao);

    // ====================================================
    // Métodos - Grupos Distintos (Query SQL Nativa)
    // ====================================================
    @Query(value = "SELECT DISTINCT grupo FROM tbl_substituicao ORDER BY grupo", nativeQuery = true)
    List<String> findDistinctGrupos();
}