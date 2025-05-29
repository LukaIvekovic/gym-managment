package hr.fer.gymmanagment.groupclass.repository;

import hr.fer.gymmanagment.groupclass.entity.GroupClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupClassRepository extends JpaRepository<GroupClass, Integer> {
    @Query("SELECT gc FROM GroupClass gc WHERE gc.dateTime >= current_timestamp")
    List<GroupClass> findAllIncoming();
}
