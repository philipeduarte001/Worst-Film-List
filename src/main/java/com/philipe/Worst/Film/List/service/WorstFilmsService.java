package com.philipe.Worst.Film.List.service;


import com.philipe.Worst.Film.List.model.MovieModel;
import com.philipe.Worst.Film.List.util.CsvReader;
import com.philipe.Worst.Film.List.util.FileReader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorstFilmsService {

    private final FilmsService filmsService;

    public WorstFilmsService(FilmsService filmsService) {
        this.filmsService = filmsService;
    }

    public void loadAndPersistCsvFile(String path) {
        FileReader<MovieModel> fileReader = new CsvReader<>(MovieModel.class);
        Optional<List<MovieModel>> movieList = Optional.ofNullable(fileReader.read(path));
        movieList.ifPresent(movies -> filmsService.save(movies));
    }

}
