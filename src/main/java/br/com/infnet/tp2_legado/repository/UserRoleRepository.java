package br.com.infnet.tp2_legado.repository;

import br.com.infnet.tp2_legado.model.UserRole;
import br.com.infnet.tp2_legado.model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}