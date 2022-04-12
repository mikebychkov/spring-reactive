package guru.springframework.sfgrestbrewery.services;

import guru.springframework.sfgrestbrewery.domain.Beer;
import guru.springframework.sfgrestbrewery.repositories.BeerRepository;
import guru.springframework.sfgrestbrewery.web.controller.NotFoundException;
import guru.springframework.sfgrestbrewery.web.mappers.BeerMapper;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import guru.springframework.sfgrestbrewery.web.model.BeerPagedList;
import guru.springframework.sfgrestbrewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.relational.core.query.Query;

import java.util.stream.Collectors;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.empty;
import static org.springframework.data.relational.core.query.Query.query;
/**
 * Created by jt on 2019-04-20.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false ")
    @Override
    public Mono<BeerPagedList> listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {

        Query q = null;

        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search both
            q = query(where("beerName").is(beerName).and("beerStyle").is(beerStyle));
        } else if (!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            //search beer_service name
            q = query(where("beerName").is(beerName));
        } else if (StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search beer_service style
            q = query(where("beerStyle").is(beerStyle));
        } else {
            q = empty();
        }

        return r2dbcEntityTemplate.select(Beer.class)
                .matching(q.with(pageRequest))
                .all()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList())
                .map(beers -> {
                    return new BeerPagedList(beers, PageRequest.of(
                            pageRequest.getPageNumber(),
                            pageRequest.getPageSize()),
                            beers.size());
                });
    }

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false ")
    @Override
    public Flux<BeerDto> listBeersFlux(String beerName, BeerStyleEnum beerStyle, Pageable pageable, Boolean showInventoryOnHand) {

        Flux<Beer> beerFlux;

        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            beerFlux = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageable);
        } else if (!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            beerFlux = beerRepository.findAllByBeerName(beerName, pageable);
        } else if (StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            beerFlux = beerRepository.findAllByBeerStyle(beerStyle, pageable);
        } else {
            beerFlux = beerRepository.findAllBy(pageable);
        }

        if (showInventoryOnHand){
            return beerFlux.map(beerMapper::beerToBeerDtoWithInventory);
        } else {
            return beerFlux.map(beerMapper::beerToBeerDto);
        }
    }

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false ")
    @Override
    public Mono<BeerDto> getById(Long beerId, Boolean showInventoryOnHand) {

        if (showInventoryOnHand) {
            return beerRepository.findById(beerId).map(beerMapper::beerToBeerDtoWithInventory);
        } else {
            return beerRepository.findById(beerId).map(beerMapper::beerToBeerDto);
        }
    }

    @Override
    public Mono<BeerDto> saveNewBeer(BeerDto beerDto) {

        return beerRepository.save(beerMapper.beerDtoToBeer(beerDto)).map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> saveNewBeerMono(Mono<BeerDto> beerDtoMono) {

        return beerDtoMono.map(beerMapper::beerDtoToBeer)
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDto> updateBeer(Long beerId, BeerDto beerDto) {

        return beerRepository.findById(beerId)
                .defaultIfEmpty(Beer.builder().build())
                .map(beer -> {
                    beer.setBeerName(beerDto.getBeerName());
                    beer.setBeerStyle(BeerStyleEnum.valueOf(beerDto.getBeerStyle()));
                    beer.setPrice(beerDto.getPrice());
                    beer.setUpc(beerDto.getUpc());
                    return beer;
                }).flatMap(beer -> {
                    if (beer.getId() != null) return beerRepository.save(beer);
                    return Mono.just(beer);
                }).map(beerMapper::beerToBeerDto);
    }

    @Cacheable(cacheNames = "beerUpcCache")
    @Override
    public Mono<BeerDto> getByUpc(String upc) {

        return beerRepository.findByUpc(upc).map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<Void> deleteBeerById(Long beerId) {

        return beerRepository.deleteById(beerId);
    }

    @Override
    public Mono<Void> deleteBeerByIdReactive(Long beerId) {

        return beerRepository.findById(beerId)
                .switchIfEmpty(Mono.error(new NotFoundException()))
                .map(Beer::getId)
                .flatMap(beerRepository::deleteById);
    }
}
