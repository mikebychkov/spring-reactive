package spring.example.reactive.repository;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.example.reactive.domain.Person;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class PersonRepositoryImpl implements PersonRepository {

    private final Random rnd = new Random();

    private List<String> firstNames = List.of("Ivan", "Jason", "Clint", "Maven", "Alex", "Shawn", "Simona", "Whitney", "Lily", "Janice");
    private List<String> lastNames = List.of("Ivanov", "Petrova", "Jackson", "Stivenson", "Grande", "Komarovski", "Jobs", "Hopkins", "Eastwood", "Spielberg");

    private Person generatePerson() {
        return Person.builder()
                .id(rnd.nextInt())
                .firstName(firstNames.get(rnd.nextInt(10)))
                .lastName(lastNames.get(rnd.nextInt(10)))
                .build();
    }

    private Person generatePerson(Integer id) {
        return Person.builder()
                .id(id)
                .firstName(firstNames.get(rnd.nextInt(10)))
                .lastName(lastNames.get(rnd.nextInt(10)))
                .build();
    }

    @Override
    public Mono<Person> findById(Integer id) {
        //return Mono.just(generatePerson());
        return findAll().filter(p -> p.getId().equals(id)).next();
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.fromStream(
                Stream.iterate(0, n -> n < rnd.nextInt(30), n -> n + 1).map(this::generatePerson)
        );
    }
}
