package br.com.sistema.equivalence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sistema.equivalence.entity.Alimento;
import br.com.sistema.equivalence.enums.GrupoAlimentar;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Integer> {

    Optional<Alimento> findById(Integer id);

    List<Alimento> findByGrupo(GrupoAlimentar grupo);

    List<Alimento> findByGrupoAndIdNot(GrupoAlimentar grupo, Integer id);
}