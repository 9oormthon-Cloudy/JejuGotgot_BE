package com.cloudy.api.adapter.kakao;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class WeatherService {

    private final RestClient restClient = RestClient.builder().baseUrl("https://map.kakao.com/").build();

    public Pair<String, Float> getWeather(Double x, Double y) {
        Map<String, Object> response = restClient.get().uri(uriBuilder -> uriBuilder
                .path("api/dapi/point/weather")
                .queryParam("inputCoordSystem", "WGS84")
                .queryParam("outputCoordSystem", "WGS84")
                .queryParam("version", "2")
                .queryParam("service", "map.daum.net")
                .queryParam("x", x)
                .queryParam("y", y)
                .build()
        ).retrieve().body(Map.class);

        Map<String, Object> weatherInfos = (Map<String, Object>) response.get("weatherInfos");
        Map<String, Object> current = (Map<String, Object>) weatherInfos.get("current");

        return Pair.of((String)current.get("desc"), Float.parseFloat((String)current.get("temperature")));
    }
}
