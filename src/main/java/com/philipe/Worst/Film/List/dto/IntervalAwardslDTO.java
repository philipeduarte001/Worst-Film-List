package com.philipe.Worst.Film.List.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IntervalAwardslDTO {
    public IntervalAwardslDTO() {
        min = new ArrayList<>();
        max = new ArrayList<>();
    }

    private List<ProducerDTO> min;
    private List<ProducerDTO> max;

}
