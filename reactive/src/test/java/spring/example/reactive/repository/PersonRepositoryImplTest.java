package spring.example.reactive.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import spring.example.reactive.domain.Person;

import java.util.List;

class PersonRepositoryImplTest {

    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void findByIdBlock() {
        System.out.println("Mono #1.1");
        Mono<Person> mono = personRepository.findById(1);
        Person person = mono.block();
        System.out.println(person);
    }

    @Test
    void findByIdSubscribe() {
        System.out.println("Mono #1.2");

        Mono<Person> mono = personRepository.findById(1);

        StepVerifier.create(mono).expectNextCount(1).verifyComplete(); // REACTIVE TYPES TESTING TOOL

        //mono.subscribe(System.out::println);
    }

    @Test
    void findByIdMap() {
        System.out.println("Mono #1.3");
        Mono<Person> mono = personRepository.findById(1);
        mono.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void findAllBlock() {
        System.out.println("Flux #2.1");
        Flux<Person> flux = personRepository.findAll();
        Person person = flux.blockFirst();
        System.out.println(person);
    }

    @Test
    void findAllSubscribe() {
        System.out.println("Flux #2.2");
        Flux<Person> flux = personRepository.findAll();
        flux.subscribe(System.out::println);
    }

    @Test
    void findAllMap() {
        System.out.println("Flux #2.3");
        Flux<Person> flux = personRepository.findAll();
        flux.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void findAllCollectList() {
        System.out.println("Flux #2.4");
        Flux<Person> flux = personRepository.findAll();
        Mono<List<Person>> mono = flux.collectList();
        mono.map(List::size).subscribe(System.out::println);
    }

    @Test
    void findAllFilter() {
        System.out.println("Flux #2.5");
        Flux<Person> flux = personRepository.findAll();
        Mono<List<Person>> mono = flux.filter(p -> "Simona".equals(p.getFirstName())).collectList();
        mono.subscribe(System.out::println);
    }

    @Test
    void findAllFilterAndError() {
        System.out.println("Flux #2.6");
        Flux<Person> flux = personRepository.findAll();
        Mono<Person> mono = flux.single();
        mono.doOnError(e -> System.out.println("I FUCKING CRUSHED"))
                .onErrorReturn(new Person())
                .subscribe(System.out::println);
    }

    @Test
    void findAllFilterAndSilentError() {
        System.out.println("Flux #2.7");
        Flux<Person> flux = personRepository.findAll().filter(p -> "Sinona".equals(p.getFirstName()));
        Mono<Person> mono = flux.next();
        mono.subscribe(System.out::println);
    }
}