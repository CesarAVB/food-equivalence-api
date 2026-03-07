package br.com.sistema.equivalence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sistema.equivalence.entity.Alimento;
import br.com.sistema.equivalence.enums.GrupoAlimentar;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {

  // ====================================================
  // Buscar por Grupo Alimentar
  // ====================================================
  List<Alimento> findByGrupo(GrupoAlimentar grupo);

  // ====================================================
  // Buscar por Grupo Excluindo um Alimento
  // ====================================================
  List<Alimento> findByGrupoAndIdNot(GrupoAlimentar grupo, Integer id);

  // ====================================================
  // Buscar por ID e Grupo
  // ====================================================
  Alimento findByIdAndGrupo(Integer id, GrupoAlimentar grupo);

  // ====================================================
  // Buscar por Descrição (LIKE case-insensitive)
  // ====================================================
  List<Alimento> findByDescricaoContainingIgnoreCase(String descricao);

  // ====================================================
  // Contar Alimentos por Grupo
  // ====================================================
  Long countByGrupo(GrupoAlimentar grupo);

  // ====================================================
  // Verificar Existência de Alimento
  // ====================================================
  boolean existsByIdAndGrupo(Integer id, GrupoAlimentar grupo);
}