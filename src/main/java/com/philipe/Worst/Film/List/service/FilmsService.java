package com.philipe.Worst.Film.List.service;

import com.philipe.Worst.Film.List.dto.IntervalAwardslDTO;
import com.philipe.Worst.Film.List.dto.ProducerDTO;
import com.philipe.Worst.Film.List.model.MovieModel;
import com.philipe.Worst.Film.List.repository.FilmsRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmsService {

    private final FilmsRepository filmsRepository;

    public FilmsService(FilmsRepository filmsRepository) {
        this.filmsRepository = filmsRepository;
    }

    public void save(List<MovieModel> movieList) {
        filmsRepository.saveAll(movieList);
    }

    public IntervalAwardslDTO getMinMaxIntervalDTO() {
        return filterMinMaxIntervalDTO(
                getAllProducersDTO(
                        filterTwiceWinningProducers(
                                filmsRepository.findAllProducersByWinnerMovies().orElseGet(ArrayList::new))));
    }

    private IntervalAwardslDTO filterMinMaxIntervalDTO(List<ProducerDTO> producersDTO) {
        if (producersDTO.isEmpty()) {
            return new IntervalAwardslDTO();
        }

        int minInterval = Collections.min(producersDTO, Comparator.comparingInt(ProducerDTO::getInterval)).getInterval();
        int maxInterval = Collections.max(producersDTO, Comparator.comparingInt(ProducerDTO::getInterval)).getInterval();

        IntervalAwardslDTO intervalAwardslDTO = new IntervalAwardslDTO();
        intervalAwardslDTO.setMin(producersDTO.stream().filter(producerDTO -> producerDTO.getInterval() == minInterval).collect(Collectors.toList()));
        intervalAwardslDTO.setMax(producersDTO.stream().filter(producerDTO -> producerDTO.getInterval() == maxInterval).collect(Collectors.toList()));

        return intervalAwardslDTO;
    }

    private List<ProducerDTO> getAllProducersDTO(List<String> twiceWinningProducers) {
        List<ProducerDTO> producersDTO = new ArrayList<>();

        twiceWinningProducers.forEach(producerName -> {
            List<MovieModel> producerMovies = filmsRepository.findAllByProducersContainingAndWinnerOrderByYearAsc(producerName, "yes");
            producersDTO.addAll(getProducerDTO(producerMovies, producerName));
        });

        return producersDTO;
    }

    private List<String> filterTwiceWinningProducers(List<String> winnerProducers) {
        List<String> twiceWinningProducers = new ArrayList<>();

        winnerProducers.forEach(producers ->  {
            for (String producerName : producers.split(", | and ")) {
                if (twiceWinningProducers.contains(producerName)) {
                    continue;
                }
                if (filmsRepository.countProducerWins(producerName) > 1) {
                    twiceWinningProducers.add(producerName);
                }
            }
        });

        return twiceWinningProducers;
    }

    private List<ProducerDTO> getProducerDTO(List<MovieModel> producerMovies, String producerName) {
        List<ProducerDTO> producersDTO = new ArrayList<>();
        for (int i = 0; i < producerMovies.size() - 1; i++) {
            MovieModel previousWin = producerMovies.get(i);
            MovieModel followingWin = producerMovies.get(i + 1);

            Integer interval = Integer.parseInt(followingWin.getYear()) - Integer.parseInt(previousWin.getYear());

            producersDTO.add(
                    new ProducerDTO(
                            producerName,
                            interval,
                            previousWin.getYear(),
                            followingWin.getYear()));
        }

        return producersDTO;
    }

    public Page<MovieModel> getAllFilms(Pageable pageable) {
        return filmsRepository.findAll(pageable);
    }
}
