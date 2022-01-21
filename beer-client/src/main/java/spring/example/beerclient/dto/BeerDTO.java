package spring.example.beerclient.dto;

import lombok.Data;
import lombok.NonNull;

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
    private String price;

    private String id;

    private Integer version;

    private String createdDate;

    private String lastModifiedDate;
}
