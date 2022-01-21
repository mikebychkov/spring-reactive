package spring.example.netflux.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import spring.example.netflux.domain.Movie;
import spring.example.netflux.repository.MovieRepository;

@Component
@RequiredArgsConstructor
public class InitMovies implements CommandLineRunner {

    private final MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        movieRepository.deleteAll().thenMany(
                Flux.just("The Shawshank Redemption (1994)",
                        "The Godfather (1972)",
                        "The Dark Knight (2008)",
                        "12 Angry Men (1957)",
                        " Schindler's List (1993)",
                        "The Lord of the Rings: The Return of the King (2003)",
                        "Pulp Fiction (1994)")
                        //.map(Movie::new)
                        .map(t -> Movie.builder().title(t).build())
                        .flatMap(movieRepository::save)
        ).subscribe(null, null, () -> {
            movieRepository.findAll().subscribe(System.out::println);
        });
    }
}
