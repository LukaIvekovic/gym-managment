package hr.fer.gymmanagment.membership.repository;

import hr.fer.gymmanagment.membership.entity.Membership;
import hr.fer.gymmanagment.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Integer> {
    List<Membership> findByUser(User user);

    Optional<Membership> findFirstByUserAndEndDateAfter(User user, LocalDateTime currentDate);
}
