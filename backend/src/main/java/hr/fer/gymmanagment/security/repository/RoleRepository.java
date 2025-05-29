package hr.fer.gymmanagment.security.repository;

import hr.fer.gymmanagment.security.entity.Role;
import hr.fer.gymmanagment.security.entity.pojo.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleEnum name);
}
