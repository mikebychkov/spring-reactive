package guru.springframework.sfgrestbrewery.web.functional;

import guru.springframework.sfgrestbrewery.services.BeerService;
import guru.springframework.sfgrestbrewery.web.controller.NotFoundException;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerHandlerV2 {

    private final BeerService beerService;
    private final Validator validator;

    public Mono<ServerResponse> getBeerById(ServerRequest request) {

        Long beerId = Long.valueOf(request.pathVariable("beerId"));
        Boolean showInventory = Boolean.valueOf(request.queryParam("showInventory").orElse("false"));

        return beerService.getById(beerId, showInventory)
                .flatMap(beerDto -> {
                    return ServerResponse.ok().bodyValue(beerDto);
                }).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getBeerByUPC(ServerRequest request) {

        String upc = request.pathVariable("upc");

        return beerService.getByUpc(upc)
                .flatMap(beerDto -> ServerResponse.ok().bodyValue(beerDto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveNewBeer(ServerRequest request) {

        Mono<BeerDto> beerDtoMono = request.bodyToMono(BeerDto.class).doOnNext(this::validate);

        return beerService.saveNewBeerMono(beerDtoMono)
                .flatMap(beerDto -> ServerResponse.ok().bodyValue(beerDto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private void validate(BeerDto beerDto) {

        Errors errors = new BeanPropertyBindingResult(beerDto, "beerDto");
        validator.validate(beerDto, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> updateBeer(ServerRequest request) {

        return request.bodyToMono(BeerDto.class)
                .doOnNext(this::validate)
                .flatMap(beerDto -> beerService.updateBeer(Long.valueOf(request.pathVariable("beerId")), beerDto))
                .flatMap(savedBeerDto -> {
                    log.debug("SAVED BEER ID: {}", savedBeerDto.getId());
                    if (savedBeerDto.getId() == null) {
                        return ServerResponse.notFound().build();
                    } else {
                        return ServerResponse.noContent().build();
                    }
                });
    }

    public Mono<ServerResponse> deleteBeer(ServerRequest request) {

        return beerService.deleteBeerByIdReactive(Long.valueOf(request.pathVariable("beerId")))
                .flatMap(v -> ServerResponse.ok().build())
                .onErrorResume(e -> e instanceof NotFoundException, e -> ServerResponse.notFound().build());
    }
}
