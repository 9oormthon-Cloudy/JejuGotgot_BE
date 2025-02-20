package com.cloudy.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class GetPlaceBookmark {
    Integer id;
    String name;
    String type;
    String address;
    Boolean review;
    String image;
    List<String> keywords;
    Integer datBefore;
}
