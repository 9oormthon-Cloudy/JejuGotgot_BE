package com.cloudy.api.adapter.jeju;

import com.cloudy.api.dto.Congestion;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class CongestionService {

    private final RestClient restClient = RestClient.builder().baseUrl("http://jeju.mms.gislab.co.kr/mms_new/").build();

    public Congestion getCongestion(Double x, Double y) {
        List<Map<String, Object>> response = restClient.get().uri(uriBuilder -> uriBuilder.path("GEONET.getTimePopByCircle.php")
                .queryParam("SELECT", "M_POP_00,M_POP_10,M_POP_20,M_POP_30,M_POP_40,M_POP_50,M_POP_60,M_POP_70,M_POP_80,M_POP_90,W_POP_00,W_POP_10,W_POP_20,W_POP_30,W_POP_40,W_POP_50,W_POP_60,W_POP_70,W_POP_80,W_POP_90")
                .queryParam("X", x.toString())
                .queryParam("Y", y.toString())
                .queryParam("R", "150")
                .build()
        ).retrieve().body(List.class);

        System.out.println(response);

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
