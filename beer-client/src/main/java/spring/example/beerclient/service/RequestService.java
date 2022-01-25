package spring.example.beerclient.service;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import spring.example.beerclient.dto.BeerDTO;
import spring.example.beerclient.dto.BeerListDTO;
import spring.example.beerclient.dto.BeerStyle;

public interface RequestService {

    Mono<BeerListDTO> listBeers(Integer pageNumber, Integer pageSize, String beerName, BeerStyle beerStyle, Boolean showInventoryOnHand);

    Mono<BeerDTO> beerById(String id, Boolean showInventoryOnHand);

    Mono<BeerDTO> beerByUpc(String upc);

    Mono<ResponseEntity> addBeer(BeerDTO body);

    Mono<ResponseEntity> updateBeer(String id, BeerDTO body);

    Mono<ResponseEntity> deleteBeer(String id);
}
