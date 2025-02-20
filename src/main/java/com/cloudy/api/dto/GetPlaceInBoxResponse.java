package com.cloudy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class GetPlaceInBoxResponse {
    private Integer id;
    private Double x;
    private Double y;
    private String status;
    private Boolean bookmark;
}
