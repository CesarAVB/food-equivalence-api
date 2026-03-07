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
    // Buscar por Grupo - Ordenado alfabeticamente
    // ====================================================
    @Query("SELECT a FROM Alimento a WHERE a.grupo = ?1 ORDER BY a.descricao ASC")
    List<Alimento> findByGrupo(GrupoAlimentar grupo);

    // ====================================================
    // Buscar por Descrição - Ordenado alfabeticamente
    // ====================================================
    @Query("SELECT a FROM Alimento a WHERE LOWER(a.descricao) LIKE LOWER(CONCAT('%', ?1, '%')) ORDER BY a.descricao ASC")
    List<Alimento> findByDescricaoIgnoreCaseContaining(String descricao);

    // ====================================================
    // Grupos Distintos - Ordenado
    // ====================================================
    @Query(value = "SELECT DISTINCT grupo FROM tbl_substituicao ORDER BY grupo", nativeQuery = true)
    List<String> findDistinctGrupos();
}