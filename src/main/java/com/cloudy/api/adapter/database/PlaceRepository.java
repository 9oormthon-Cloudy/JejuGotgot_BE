package com.cloudy.api.adapter.database;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<PlaceEntity, Integer> {

    @Query(value = "select * from place p "
        + "where ST_Distance_Sphere(point(p.longitude,p.latitude), point(:x,:y)) < 5000 "
        + "order by ST_Distance_Sphere(point(p.longitude,p.latitude), point(:x,:y))",  nativeQuery = true)
    List<PlaceEntity> findByPointAndDistance(double x, double y);

    @Query(value = "select * from place p "
            + "order by ST_Distance_Sphere(point(p.longitude,p.latitude), point(:x,:y)) LIMIT 1",  nativeQuery = true)
    PlaceEntity findNearest(double x, double y);

    List<PlaceEntity> findAllByType(String type, Limit limit);

    @Query(value = "SELECT * FROM place p " +
            "WHERE latitude BETWEEN :minY AND :maxY " +
            "AND longitude BETWEEN :minX AND :maxX LIMIT 30",
            nativeQuery = true)
    List<PlaceEntity> findInBox(double minY, double maxY, double minX, double maxX);

    @Query(value = "select * FROM place p " +
            "WHERE p.bookmark = true LIMIT 30", nativeQuery = true)
    List<PlaceEntity> findInBookmark();

    @Query(value = "select * FROM place p " +
            "WHERE name = :keyword LIMIT 1", nativeQuery = true)
    Optional<PlaceEntity> findInKeyword(String keyword);
}
