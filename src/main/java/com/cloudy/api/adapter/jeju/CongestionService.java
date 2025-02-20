package com.cloudy.api.adapter.jeju;

import com.cloudy.api.dto.Congestion;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;

@Service
public class CongestionService {

    private final RestTemplate restTemplate;

    public CongestionService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("krmp-proxy.9rum.cc", 3128));
        factory.setProxy(proxy);
        this.restTemplate = new RestTemplate(factory);
//        this.restTemplate = new RestTemplate();
    }
//
//    private final RestClient restClient = RestClient.builder().baseUrl("http://jeju.mms.gislab.co.kr/mms_new/")..build();

    public Congestion getCongestion(Double x, Double y) {
//        List<Map<String, Object>> response = restClient.get().uri(uriBuilder -> uriBuilder.path("GEONET.getTimePopByCircle.php")
//                .queryParam("SELECT", "M_POP_00,M_POP_10,M_POP_20,M_POP_30,M_POP_40,M_POP_50,M_POP_60,M_POP_70,M_POP_80,M_POP_90,W_POP_00,W_POP_10,W_POP_20,W_POP_30,W_POP_40,W_POP_50,W_POP_60,W_POP_70,W_POP_80,W_POP_90")
//                .queryParam("X", x.toString())
//                .queryParam("Y", y.toString())
//                .queryParam("R", "150")
//                .build()
//        ).retrieve().body(List.class);

        String uri = UriComponentsBuilder.fromHttpUrl("http://jeju.mms.gislab.co.kr/mms_new/GEONET.getTimePopByCircle.php")
                .queryParam("SELECT", "M_POP_00,M_POP_10,M_POP_20,M_POP_30,M_POP_40,M_POP_50,M_POP_60,M_POP_70,M_POP_80,M_POP_90,W_POP_00,W_POP_10,W_POP_20,W_POP_30,W_POP_40,W_POP_50,W_POP_60,W_POP_70,W_POP_80,W_POP_90")
                .queryParam("X", x.toString())  // x 값에 맞게 설정
                .queryParam("Y", y.toString())  // y 값에 맞게 설정
                .queryParam("R", "150")
                .toUriString();  // 전체 URL을 String 형태로 반환

        // GET 요청 보내기 및 응답 받기
        List<Map<String, Object>> response = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                List.class).getBody();

        if (!response.isEmpty()) {
            Congestion congestion = new Congestion();
            for (Map<String, Object> entry : response) {
                String time = entry.get("TIME").toString();

                if ("NOW".equals(time)) {
                    congestion.setInNow((Integer) entry.get("IN_POP"));
                    congestion.setOutNow((Integer) entry.get("OUT_POP"));
                } else if ("3AVG".equals(time)) {
                    congestion.setIn3Avg((Integer) entry.get("IN_POP"));
                    congestion.setOut3Avg((Integer) entry.get("OUT_POP"));
                }
            }
            if (congestion.getOutNow() > 100) {
                congestion.setStatus("혼잡");
            } else if (congestion.getOutNow() > 20) {
                congestion.setStatus("보통");
            } else {
                congestion.setStatus("여유");
            }


            return congestion;
        }
        else {
            throw new RuntimeException();
        }
    }
}
