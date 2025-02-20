package com.cloudy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class GetPlaceResponse {
    Integer id;
    String name;
    String address;
    String image;
    Double x;
    Double y;
    String weather;
    Float temperature;
    Boolean bookmark;
    Congestion congestion;
    List<ReplacePlace> replace;
}
