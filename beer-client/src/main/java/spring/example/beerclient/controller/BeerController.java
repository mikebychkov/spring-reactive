package spring.example.beerclient.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import spring.example.beerclient.dto.BeerDTO;
import spring.example.beerclient.dto.BeerListDTO;
import spring.example.beerclient.dto.BeerStyle;
import spring.example.beerclient.service.RequestService;

@RestController
@RequestMapping("/beer")
@RequiredArgsConstructor
public class BeerController {

    private final RequestService requestService;

    @GetMapping
    public Mono<BeerListDTO> listBeers(@RequestParam(defaultValue = "0", required = false) Integer pageNumber,
                                       @RequestParam(defaultValue = "25", required = false) Integer pageSize,
                                       @RequestParam(required = false) String beerName,
                                       @RequestParam(required = false) BeerStyle beerStyle,
                                       @RequestParam(defaultValue = "false", required = false) Boolean showInventoryOnHand) {

        return requestService.listBeers(pageNumber, pageSize, beerName, beerStyle, showInventoryOnHand);
    }

    @GetMapping("/{id}")
    public Mono<BeerDTO> beerById(@PathVariable String id,
                                  @RequestParam(defaultValue = "false", required = false) Boolean showInventoryOnHand) {

        return requestService.beerById(id, showInventoryOnHand);
    }
}
