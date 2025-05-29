package hr.fer.gymmanagment.groupclass.entity;

import hr.fer.gymmanagment.security.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "GROUP_CLASSES")
public class GroupClass {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private GroupClassType type;

    @Column(name = "DATE_TIME", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "DURATION", nullable = false)
    private Integer duration;

    @Column(name = "MAX_PARTICIPANTS", nullable = false)
    private Integer maxParticipants;

    @ManyToMany
    @JoinTable(
        name = "CLASS_INSTRUCTORS",
        joinColumns = @JoinColumn(name = "CLASS_ID"),
        inverseJoinColumns = @JoinColumn(name = "INSTRUCTOR_ID")
    )
    private Set<User> instructors = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "CLASS_PARTICIPANTS",
        joinColumns = @JoinColumn(name = "CLASS_ID"),
        inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
    private Set<User> participants = new HashSet<>();
}
