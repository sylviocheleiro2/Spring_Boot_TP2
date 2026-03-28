package br.com.infnet.tp2_legado.repository.aventura;

import br.com.infnet.tp2_legado.model.aventura.Missao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MissaoRepository extends JpaRepository<Missao, Long>, JpaSpecificationExecutor<Missao> {
}