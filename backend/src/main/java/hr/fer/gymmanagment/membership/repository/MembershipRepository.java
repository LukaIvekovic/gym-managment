package hr.fer.gymmanagment.membership.repository;

import hr.fer.gymmanagment.membership.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
}
