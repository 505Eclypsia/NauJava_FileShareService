package ru.Vlad.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.Vlad.NauJava.entity.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
}
