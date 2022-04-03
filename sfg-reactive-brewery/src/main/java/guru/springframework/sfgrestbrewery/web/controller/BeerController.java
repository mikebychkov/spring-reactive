package guru.springframework.sfgrestbrewery.web.controller;

import guru.springframework.sfgrestbrewery.services.BeerService;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import guru.springframework.sfgrestbrewery.web.model.BeerPagedList;
import guru.springframework.sfgrestbrewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 2019-04-20.
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    private final BeerService beerService;

    @GetMapping(produces = { "application/json" }, path = "beer")
    public ResponseEntity<Mono<BeerPagedList>> listBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "beerName", required = false) String beerName,
                                                         @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
                                                         @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {

//        if (showInventoryOnHand == null) {
//            showInventoryOnHand = false;
//        }
//
//        if (pageNumber == null || pageNumber < 0){
//            pageNumber = DEFAULT_PAGE_NUMBER;
//        }
//
//        if (pageSize == null || pageSize < 1) {
//            pageSize = DEFAULT_PAGE_SIZE;
//        }
//
//        BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);
//
//        return ResponseEntity.ok(Mono.just(beerList));

        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = { "application/json" }, path = "beer-flux")
    public ResponseEntity<Flux<BeerDto>> listBeersFlux(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "beerName", required = false) String beerName,
                                                   @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
                                                   @RequestParam(value = "showInventoryOnHand", required = false, defaultValue = "false") Boolean showInventoryOnHand) {

        Flux<BeerDto> beerFlux = beerService.listBeersFlux(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);

        return ResponseEntity.ok(beerFlux);
    }

    @GetMapping("beer/{beerId}")
    public ResponseEntity<Mono<BeerDto>> getBeerById(@PathVariable("beerId") Long beerId,
                                                     @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }
        return ResponseEntity.ok(beerService.getById(beerId, showInventoryOnHand));
    }

    @GetMapping("beerUpc/{upc}")
    public ResponseEntity<Mono<BeerDto>> getBeerByUpc(@PathVariable("upc") String upc) {

        return ResponseEntity.ok(beerService.getByUpc(upc));
    }

    @PostMapping(path = "beer")
    public ResponseEntity<Mono<BeerDto>> saveNewBeer(@RequestBody @Validated BeerDto beerDto) {

        BeerDto savedBeer = beerService.saveNewBeer(beerDto);

        return ResponseEntity.ok(Mono.just(savedBeer));
    }

    @PutMapping("beer/{beerId}")
    public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") Long beerId, @RequestBody @Validated BeerDto beerDto) {

        // TODO

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("beer/{beerId}")
    public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") Long beerId) {

        beerService.deleteBeerById(beerId);

        return ResponseEntity.noContent().build();
    }

}
