package ru.practicum.user.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findUsersByIdIn(int[] ids, Pageable pageable);

    Optional<User> findByName(String name);
}
