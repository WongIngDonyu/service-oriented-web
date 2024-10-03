package com.web.serviceorientedweb.repositories;

import com.web.serviceorientedweb.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    Optional<Person> findByFirstNameAndLastNameAndPatronymic(String firstName, String lastName, String patronymic);
    List<Person> findByPhone(String phoneNumber);
}
