package com.cloudy.api.adapter.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "place")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceEntity {

    @Id
    Integer id;

    String name;

    String type;

    String address;

    String image;

    Double latitude;

    Double longitude;

    Boolean bookmark;

    @Column(name = "out_now")
    Integer outNow;

    @Column(name = "in_now")
    Integer inNow;

    @Column(name = "out_3avg")
    Integer out3Avg;

    @Column(name = "in_3avg")
    Integer in3Avg;

    String weather;

    Float temperature;

    String keyword1;

    String keyword2;

    String keyword3;
}
