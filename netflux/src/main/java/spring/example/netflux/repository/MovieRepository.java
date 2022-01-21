package spring.example.netflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import spring.example.netflux.domain.Movie;

public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
}
