package com.philipe.Worst.Film.List.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import com.philipe.Worst.Film.List.dto.IntervalAwardslDTO;
import com.philipe.Worst.Film.List.dto.ProducerDTO;
import com.philipe.Worst.Film.List.service.FilmsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;

@ExtendWith(MockitoExtension.class)
public class WorstFilmsControllerTest {

    @InjectMocks
    private WorstFilmsController worstFilmsController;

    @Mock
    private FilmsService filmsService;

    @Test
    public void testGetMinMaxWinningInterval() throws Exception {

        IntervalAwardslDTO intervalAwardslDTO = new IntervalAwardslDTO();
        intervalAwardslDTO.setMin(Arrays.asList(new ProducerDTO("Producer1", 2, "2000", "2002")));
        intervalAwardslDTO.setMax(Arrays.asList(new ProducerDTO("Producer2", 5, "1995", "2000")));

        when(filmsService.getMinMaxIntervalDTO()).thenReturn(intervalAwardslDTO);

        ResponseEntity<?> responseEntity = worstFilmsController.getMinMaxWinningInterval();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        EntityModel<IntervalAwardslDTO> entityModel = (EntityModel<IntervalAwardslDTO>) responseEntity.getBody();
        assertNotNull(entityModel);
        assertEquals(intervalAwardslDTO, entityModel.getContent());

        assertTrue(entityModel.getLinks().hasLink("self"));

        verify(filmsService, times(1)).getMinMaxIntervalDTO();
    }
}