package spring.example.beerclient.dto;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class BeerDTO {

    @NonNull
    private String beerName;

    @NonNull
    private BeerStyle beerStyle;

    @NonNull
    private String upc;

    private Integer quantityOnHand;

    @NonNull
    private Double price;

    private String id;

    private Integer version;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
