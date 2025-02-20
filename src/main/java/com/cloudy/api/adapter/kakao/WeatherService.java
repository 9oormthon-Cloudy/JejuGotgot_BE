package com.cloudy.api.adapter.kakao;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("krmp-proxy.9rum.cc", 3128));
        factory.setProxy(proxy);
        this.restTemplate = new RestTemplate(factory);
//        this.restTemplate = new RestTemplate();
    }

//    private final RestClient restClient = RestClient.builder().baseUrl("https://map.kakao.com/").build();

    public Pair<String, Float> getWeather(Double x, Double y) {

        String uri = UriComponentsBuilder.fromHttpUrl("https://map.kakao.com/api/dapi/point/weather")
                .queryParam("inputCoordSystem", "WGS84")
                .queryParam("outputCoordSystem", "WGS84")
                .queryParam("version", "2")
                .queryParam("service", "map.daum.net")
                .queryParam("x", x)  // x 값에 맞게 설정
                .queryParam("y", y)  // y 값에 맞게 설정
                .toUriString();  // 전체 URL을 String 형태로 반환

        Map<String, Object> response = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                Map.class).getBody();


//        Map<String, Object> response = restClient.get().uri(uriBuilder -> uriBuilder
//                .path("api/dapi/point/weather")
//                .queryParam("inputCoordSystem", "WGS84")
//                .queryParam("outputCoordSystem", "WGS84")
//                .queryParam("version", "2")
//                .queryParam("service", "map.daum.net")
//                .queryParam("x", x)
//                .queryParam("y", y)
//                .build()
//        ).retrieve().body(Map.class);

        Map<String, Object> weatherInfos = (Map<String, Object>) response.get("weatherInfos");
        Map<String, Object> current = (Map<String, Object>) weatherInfos.get("current");

        return Pair.of((String)current.get("desc"), Float.parseFloat((String)current.get("temperature")));
    }
}
