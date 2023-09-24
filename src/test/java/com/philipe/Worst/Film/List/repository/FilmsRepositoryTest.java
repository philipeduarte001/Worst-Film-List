package com.philipe.Worst.Film.List.repository;

import com.philipe.Worst.Film.List.model.MovieModel;
import com.philipe.Worst.Film.List.util.CsvReader;
import com.philipe.Worst.Film.List.util.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import static com.philipe.Worst.Film.List.util.FileData.getFileData;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class FilmsRepositoryTest {


    @Autowired
    private FilmsRepository filmsRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        FileReader<MovieModel> fileReader = new CsvReader<>(MovieModel.class);
        Optional<List<MovieModel>> movieList = Optional.ofNullable(
                fileReader.read(
                        new StringReader(getFileData())));

        Assertions.assertTrue(movieList.isPresent());
        movieList.get().forEach(movie -> testEntityManager.persistAndFlush(movie));
    }

    @Test
    void shouldFindWinningProducers() {
        Optional<List<String>> movieList = filmsRepository.findAllProducersByWinnerMovies();

        Assertions.assertTrue(movieList.isPresent());
        Assertions.assertTrue(movieList.get().stream().anyMatch(s -> s.contains("Allan Carr")));
        Assertions.assertTrue(movieList.get().stream().anyMatch(s -> s.contains("Steve Fargnoli")));
        Assertions.assertTrue(movieList.get().stream().anyMatch(s -> s.contains("Steven Perry")));
        Assertions.assertTrue(movieList.get().stream().anyMatch(s -> s.contains("Joel Silver")));
        Assertions.assertFalse(movieList.get().stream().anyMatch(s -> s.contains("Jerry Weintraub")));
    }

    @Test
    void shouldCountProducerWins() {
        Assertions.assertNotEquals(filmsRepository.countProducerWins("Allan Carr"), 0);
        Assertions.assertEquals(filmsRepository.countProducerWins("Allan Carr"), 1);
        Assertions.assertEquals(filmsRepository.countProducerWins("Bo Derek"), 2);
        Assertions.assertEquals(filmsRepository.countProducerWins("Steve Shagan"), 0);
    }

    @Test
    void shouldFindMoviesByWinningProducers() {
        List<MovieModel> movieList = filmsRepository.findAllByProducersContainingAndWinnerOrderByYearAsc("Allan Carr", "yes");
        Assertions.assertTrue(movieList.stream().anyMatch(movie -> movie.getTitle().equals("Can't Stop the Music")));
        Assertions.assertTrue(movieList.stream().noneMatch(movie -> movie.getTitle().equals("Where the Boys Are '84")));

        movieList = filmsRepository.findAllByProducersContainingAndWinnerOrderByYearAsc("Bo Derek", "yes");
        Assertions.assertTrue(movieList.stream().anyMatch(movie -> movie.getTitle().equals("Bolero")));
        Assertions.assertTrue(movieList.stream().anyMatch(movie -> movie.getTitle().equals("Ghosts Can't Do It")));
    }

}
