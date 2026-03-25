package br.com.infnet.tp2_legado.repository;

import br.com.infnet.tp2_legado.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
}
