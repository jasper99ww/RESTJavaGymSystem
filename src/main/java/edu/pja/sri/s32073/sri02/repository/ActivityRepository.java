package edu.pja.sri.s32073.sri02.repository;

import edu.pja.sri.s32073.sri02.model.Activity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
    List<Activity> findAll();
}

