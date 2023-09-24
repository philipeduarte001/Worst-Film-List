package com.philipe.Worst.Film.List.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProducerDTO {

    private String producer;
    private Integer interval;
    private String previousWin;
    private String followingWin;

}
