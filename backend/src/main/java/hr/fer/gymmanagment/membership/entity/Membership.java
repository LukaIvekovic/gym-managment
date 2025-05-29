package hr.fer.gymmanagment.membership.entity;

import hr.fer.gymmanagment.security.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "MEMBERSHIPS")
public class Membership {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "MEMBERSHIP_TYPE_ID", nullable = false)
    private MembershipType membershipType;

    @Column(name = "START_DATE", nullable = false)
    private LocalDateTime startDate;
}
