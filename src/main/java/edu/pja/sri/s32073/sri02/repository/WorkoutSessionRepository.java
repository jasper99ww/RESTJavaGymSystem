package edu.pja.sri.s32073.sri02.repository;

import edu.pja.sri.s32073.sri02.model.WorkoutSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WorkoutSessionRepository extends CrudRepository<WorkoutSession, Long> {
    List<WorkoutSession> findAll();
}