package com.philipe.Worst.Film.List.controller;

import com.philipe.Worst.Film.List.dto.IntervalAwardslDTO;
import com.philipe.Worst.Film.List.model.MovieModel;
import com.philipe.Worst.Film.List.service.FilmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping(value = "/public/v1")
public class WorstFilmsController {

    private final FilmsService filmsService;

    public WorstFilmsController(FilmsService filmsService) {
        this.filmsService = filmsService;
    }

    @GetMapping("/films-min-max-interval")
    public ResponseEntity<?> getMinMaxWinningInterval() {
        try {
            IntervalAwardslDTO intervalAwardslDTO = filmsService.getMinMaxIntervalDTO();
            Link selfLink = WebMvcLinkBuilder.linkTo(WorstFilmsController.class)
                    .slash("films-min-max-interval")
                    .withSelfRel();
            EntityModel<IntervalAwardslDTO> entityModel = EntityModel.of(intervalAwardslDTO, selfLink);
            return ResponseEntity.ok(entityModel);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("It was not possible to obtain the producers with the highest and lowest award range");
        }
    }

    @GetMapping("/films")
    public ResponseEntity<?> getAllFilms(Pageable pageable) {
        try {
            Page<MovieModel> filmsPage = filmsService.getAllFilms(pageable);
            List<EntityModel<MovieModel>> filmsWithLinks = filmsPage
                    .stream()
                    .map(film -> EntityModel.of(film,
                            WebMvcLinkBuilder.linkTo(WorstFilmsController.class)
                                    .slash("films")
                                    .slash(film.getId())
                                    .withSelfRel()
                    ))
                    .collect(Collectors.toList());

            PagedModel<EntityModel<MovieModel>> resources = PagedModel.of(filmsWithLinks,
                    new PagedModel.PageMetadata(filmsPage.getSize(), filmsPage.getNumber(), filmsPage.getTotalElements()));

            return ResponseEntity.ok(resources);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("It was not possible to retrieve the list of films: " + exception.getMessage());
        }
    }
}
