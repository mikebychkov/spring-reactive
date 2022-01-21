package spring.example.beerclient.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
public class BeerListDTO {

    private List<BeerDTO> content;
    private JsonNode pageable;
    private Integer totalPages;
    private Boolean last;
    private Integer totalElements;
    private Integer size;
    private Integer number;
    private Integer numberOfElements;
    private JsonNode sort;
    private Boolean first;
}
