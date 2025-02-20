package com.cloudy.api.adapter.database;

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
}
