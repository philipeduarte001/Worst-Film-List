package com.philipe.Worst.Film.List.repository;

import com.philipe.Worst.Film.List.model.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FilmsRepository extends JpaRepository<MovieModel, Integer> {

    @Query(value = "SELECT producers FROM MovieModel WHERE winner = 'yes' GROUP BY producers")
    Optional<List<String>> findAllProducersByWinnerMovies();

    @Query(value = "SELECT count(id) FROM MovieModel WHERE winner = 'yes' AND producers LIKE CONCAT('%', ?1, '%')")
    Integer countProducerWins(String producer);

    List<MovieModel> findAllByProducersContainingAndWinnerOrderByYearAsc(String producer, String winner);

}
