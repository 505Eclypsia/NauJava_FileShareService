package ru.Vlad.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.Vlad.NauJava.entity.User;

import java.util.Optional;

@RepositoryRestResource(path = "users")
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String userName);

}
