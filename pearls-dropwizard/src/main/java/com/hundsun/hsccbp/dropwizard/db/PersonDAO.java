package com.hundsun.hsccbp.dropwizard.db;

import io.dropwizard.hibernate.AbstractDAO;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import com.hundsun.hsccbp.dropwizard.core.Person;

public class PersonDAO extends AbstractDAO<Person> {
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Person create(Person person) {
        return persist(person);
    }

    public List<Person> findAll() {
        return list(namedQuery("com.example.helloworld.core.Person.findAll"));
    }
}
