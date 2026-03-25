package br.com.infnet.tp2_legado.repository;


import br.com.infnet.tp2_legado.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
