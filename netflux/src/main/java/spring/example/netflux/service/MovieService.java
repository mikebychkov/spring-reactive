package spring.example.netflux.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import spring.example.netflux.domain.Movie;
import spring.example.netflux.domain.MovieEvent;

public interface MovieService {

    Mono<Movie> findById(String id);
    Flux<Movie> findAll();
    Flux<MovieEvent> streamMovieEvents(String id);
}
