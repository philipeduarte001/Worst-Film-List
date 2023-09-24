package com.philipe.Worst.Film.List.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class MovieModel {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "release_year")
    private String year;

    private String title;
    private String studios;
    private String producers;

    @Column(name = "WINNER")

    private String winner;

}
