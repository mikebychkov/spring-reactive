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
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicBoolean;

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

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        if (pageNumber == null || pageNumber < 0){
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        Mono<BeerPagedList> beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);

        return ResponseEntity.ok(beerList);
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

        Mono<BeerDto> rsl = beerService.getById(beerId, showInventoryOnHand);


        // VERSION 1

//        AtomicBoolean aBool = new AtomicBoolean(false);
//        rsl.hasElement().subscribe(aBool::set);
//        if (aBool.get()) {
//            return ResponseEntity.ok(rsl);
//        } else {
//            return ResponseEntity.notFound().build();
//        }

        // VERSION 2

//        AtomicBoolean aBool = new AtomicBoolean(false);
//        rsl.hasElement().subscribe(aBool::set);
//        if (!aBool.get()) {
//            throw new NotFoundException();
//        }

        // VERSION 3

        return ResponseEntity.ok(
                rsl.defaultIfEmpty(BeerDto.builder().build())
                        .doOnNext(beerDto -> {
                            if (beerDto.getId() == null) throw new NotFoundException();
                        })
        );
    }

    @ExceptionHandler
    ResponseEntity<Void> handleNotFound(NotFoundException ex){
        return ResponseEntity.notFound().build();
    }

    @GetMapping("beerUpc/{upc}")
    public ResponseEntity<Mono<BeerDto>> getBeerByUpc(@PathVariable("upc") String upc) {

        return ResponseEntity.ok(beerService.getByUpc(upc));
    }

    @PostMapping(path = "beer")
    public ResponseEntity<Mono<BeerDto>> saveNewBeer(@RequestBody @Validated BeerDto beerDto) {

        Mono<BeerDto> savedBeer = beerService.saveNewBeer(beerDto);

        return ResponseEntity.ok(savedBeer);
    }

    @PutMapping("beer/{beerId}")
    //public ResponseEntity<Mono<BeerDto>> updateBeerById(@PathVariable("beerId") Long beerId, @RequestBody @Validated BeerDto beerDto) {
    public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") Long beerId, @RequestBody @Validated BeerDto beerDto) {

        //return ResponseEntity.ok(beerService.updateBeer(beerId, beerDto));

        AtomicBoolean aBool = new AtomicBoolean(false);

        beerService.updateBeer(beerId, beerDto).map(bd -> bd.getId() == null).subscribe(aBool::set);

        if (aBool.get()) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("beer/{beerId}")
    public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") Long beerId) {

        beerService.deleteBeerById(beerId).subscribe();

        return ResponseEntity.ok().build();
    }

}
