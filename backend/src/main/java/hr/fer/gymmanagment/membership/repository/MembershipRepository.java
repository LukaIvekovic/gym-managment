package hr.fer.gymmanagment.membership.repository;

import hr.fer.gymmanagment.membership.entity.Membership;
import hr.fer.gymmanagment.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    List<Membership> findByUser(User user);
}
