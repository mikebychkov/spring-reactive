package spring.example.reactive.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.example.reactive.domain.Person;
import spring.example.reactive.repository.PersonRepository;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final PersonRepository personRepository;

    @GetMapping("/mono")
    public Mono<Person> getMono() {
        return personRepository.findById(1);
    }

    @GetMapping("/flux")
    public Flux<Person> getFlux() {
        return personRepository.findAll();
    }
}
