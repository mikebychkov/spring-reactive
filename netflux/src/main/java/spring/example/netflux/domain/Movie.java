package spring.example.netflux.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
//@RequiredArgsConstructor
@Builder
public class Movie {

    private String id;

    @NonNull
    private String title;
}
