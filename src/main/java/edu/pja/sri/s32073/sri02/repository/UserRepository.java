package edu.pja.sri.s32073.sri02.repository;

import edu.pja.sri.s32073.sri02.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
}
