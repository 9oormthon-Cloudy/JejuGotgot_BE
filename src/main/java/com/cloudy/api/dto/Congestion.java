package com.cloudy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Congestion {
    private Integer outNow;
    private Integer inNow;
    private Integer out3Avg;
    private Integer in3Avg;
    private String status;
}
