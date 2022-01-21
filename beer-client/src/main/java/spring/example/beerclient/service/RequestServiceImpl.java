package spring.example.beerclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import spring.example.beerclient.dto.BeerDTO;
import spring.example.beerclient.dto.BeerListDTO;
import spring.example.beerclient.dto.BeerStyle;

import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final WebClient webClient;

    @Autowired
    public RequestServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080/api/v1").build();
    }

    @Override
    public Mono<BeerListDTO> listBeers(Integer pageNumber, Integer pageSize, String beerName, BeerStyle beerStyle, Boolean showInventoryOnHand) {

        return webClient.get().uri(uriBuilder ->
            uriBuilder.path("/beer")
                    .queryParam("pageNumber", pageNumber)
                    .queryParam("pageSize", pageSize)
                    .queryParam("beerName", beerName)
                    .queryParam("beerStyle", beerStyle)
                    .queryParam("showInventoryOnHand", showInventoryOnHand)
                    .build()
        ).retrieve().bodyToMono(BeerListDTO.class);
    }

    @Override
    public Mono<BeerDTO> beerById(String id, Boolean showInventoryOnHand) {

        //return webClient.get().uri("/beer/{id}", id).retrieve().bodyToMono(BeerDTO.class);
        return webClient.get().uri(
                uriBuilder -> uriBuilder.path("/beer/{id}")
                .queryParamIfPresent("showInventoryOnHand", Optional.ofNullable(showInventoryOnHand))
                .build(id)
        ).retrieve().bodyToMono(BeerDTO.class);
    }
}
