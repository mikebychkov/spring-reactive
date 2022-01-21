package spring.example.reactive.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.example.reactive.domain.Person;

public interface PersonRepository {

    Mono<Person> findById(Integer id);
    Flux<Person> findAll();
}
