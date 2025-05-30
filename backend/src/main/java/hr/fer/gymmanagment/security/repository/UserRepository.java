package hr.fer.gymmanagment.security.repository;

import hr.fer.gymmanagment.security.entity.Role;
import hr.fer.gymmanagment.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findUsersByRole(Role role);
}
