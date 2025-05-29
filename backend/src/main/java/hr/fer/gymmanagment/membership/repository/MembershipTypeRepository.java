package hr.fer.gymmanagment.membership.repository;

import hr.fer.gymmanagment.membership.entity.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipTypeRepository extends JpaRepository<MembershipType, Integer> {
}
