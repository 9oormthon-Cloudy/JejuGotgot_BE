package com.cloudy.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ReplacePlace {
    Integer id;
    String name;
    String image;
    Integer distance;
    Boolean bookmark;
}
