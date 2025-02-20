package com.cloudy.api.service;

import com.cloudy.api.adapter.database.PlaceEntity;
import com.cloudy.api.adapter.database.PlaceRepository;
import com.cloudy.api.adapter.jeju.CongestionService;
import com.cloudy.api.adapter.kakao.WeatherService;
import com.cloudy.api.dto.*;
import org.springframework.data.domain.Limit;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final CongestionService congestionService;
    private final WeatherService weatherService;

    public PlaceService(PlaceRepository placeRepository, CongestionService congestionService, WeatherService weatherService) {
        this.placeRepository = placeRepository;
        this.congestionService = congestionService;
        this.weatherService = weatherService;
    }

    public GetPlaceNearestResponse getNearestPlace(double x, double y) {
        return new GetPlaceNearestResponse(placeRepository.findNearest(x, y).getId());
    }

    public Integer getDistance(double x1, double y1, double x2, double y2) {
        double dLat = Math.toRadians(y2 - y1);
        double dLon = Math.toRadians(x2 - x1);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(Math.toRadians(y1)) * Math.cos(Math.toRadians(y2)) *
                        Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (6371 * c * 1000); // 결과를 미터(m)로 변환
    }

    public GetPlaceResponse getPlace(Integer id) {
        var response = new GetPlaceResponse();
        var place = this.placeRepository.findById(id).get();

        response.setId(place.getId());
        response.setName(place.getName());
        response.setAddress(place.getAddress());
        response.setX(place.getLongitude());
        response.setY(place.getLatitude());
        response.setImage(place.getImage());
        response.setBookmark(place.getBookmark());
        var congestion = new Congestion();
        congestion.setInNow(place.getInNow());
        congestion.setOutNow(place.getOutNow());
        congestion.setIn3Avg(place.getIn3Avg());
        congestion.setOut3Avg(place.getOut3Avg());

        if (congestion.getOutNow() > 100) {
            congestion.setStatus("혼잡");
        } else if (congestion.getOutNow() > 20) {
            congestion.setStatus("보통");
        } else {
            congestion.setStatus("여유");
        }


        response.setCongestion(congestion);
        response.setWeather(place.getWeather());
        response.setTemperature(place.getTemperature());

        List<PlaceEntity> replaces = this.placeRepository.findAllByType(place.getType(), Limit.of(5));

        response.setReplace(replaces.stream().map(it -> {
            var replace = new ReplacePlace();
            replace.setId(it.getId());
            replace.setName(it.getName());
            replace.setImage(it.getImage());
            replace.setBookmark(it.getBookmark());
            replace.setDistance(getDistance(it.getLongitude(), it.getLatitude(), place.getLongitude(), place.getLatitude()));
            return replace;
        }).toList());

        return response;
    }

    public void addBookmark(Integer id) {
        var place = this.placeRepository.findById(id).get();
        place.setBookmark(true);
        placeRepository.save(place);
    }

    public void deleteBookmark(Integer id) {
        var place = this.placeRepository.findById(id).get();
        place.setBookmark(false);
        placeRepository.save(place);
    }

    public List<GetPlaceInBoxResponse> getPlaceInBox(double minX, double maxX, double minY, double maxY) {
        return this.placeRepository.findInBox(minY, maxY, minX, maxX).stream().map(it -> {
            var response = new GetPlaceInBoxResponse();
            response.setId(it.getId());
            response.setX(it.getLongitude());
            response.setY(it.getLatitude());
            response.setBookmark(it.getBookmark());

            var congestion = new Congestion();
            congestion.setInNow(it.getInNow());
            congestion.setOutNow(it.getOutNow());
            congestion.setIn3Avg(it.getIn3Avg());
            congestion.setOut3Avg(it.getOut3Avg());
            if (congestion.getOutNow() > 100) {
                congestion.setStatus("혼잡");
            } else if (congestion.getOutNow() > 20) {
                congestion.setStatus("보통");
            } else {
                congestion.setStatus("여유");
            }
            response.setStatus(congestion.getStatus());
            return response;
        }).toList();
    }

    // search place]
    public SearchPlaceResponse searchPlace(String keyword) {
        var response = new SearchPlaceResponse();
        PlaceEntity place = this.placeRepository.findInKeyword(keyword).orElse(null);
        if (place != null) {
            response.setId(place.getId());
        } else {
            response.setId(null);
        }
        return response;
    }

    // get bookmark place
    public List<GetPlaceBookmark> getPlaceBookmark() {
        return this.placeRepository.findInBookmark().stream().map(it -> {
            var place = new GetPlaceBookmark();
            place.setId(it.getId());
            place.setName(it.getName());
            place.setImage(it.getImage());
            place.setAddress(it.getAddress());
            place.setKeywords(List.of("웅장한", "귀여운", "맛있는"));
            place.setType(it.getType());
            place.setDatBefore(3);
            place.setReview(false);
            return place;
        }).toList();
    }


    // get
}
