package guru.springframework.sfgrestbrewery.web.functional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BeerRouterConfig {

    public static final String BEER_V2_URL_SAVE = "/api/v2/beer";
    public static final String BEER_V2_URL_UPDATE = "/api/v2/beer/{beerId}";
    public static final String BEER_V2_URL_ID = "/api/v2/beer";
    public static final String BEER_V2_URL_ID_ROUTE = "/api/v2/beer/{beerId}";
    public static final String BEER_V2_URL_UPC = "/api/v2/beer-upc";
    public static final String BEER_V2_URL_UPC_ROUTE = "/api/v2/beer-upc/{upc}";

    @Bean
    public RouterFunction<ServerResponse> beerRoutesV2(BeerHandlerV2 handler) {

        return route()
                .POST(BEER_V2_URL_SAVE, accept(APPLICATION_JSON), handler::saveNewBeer)
                .PUT(BEER_V2_URL_UPDATE, accept(APPLICATION_JSON), handler::updateBeer)
                .GET(BEER_V2_URL_ID_ROUTE, accept(APPLICATION_JSON), handler::getBeerById)
                .GET(BEER_V2_URL_UPC_ROUTE, accept(APPLICATION_JSON), handler::getBeerByUPC)
                .DELETE(BEER_V2_URL_UPDATE, accept(APPLICATION_JSON), handler::deleteBeer)
                .build();
    }
}
