package com.cloudy.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;


@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/circle")
    public ResponseEntity<?> getCircle(@RequestParam("x") String x, @RequestParam("y") String y) {
        var restClient = RestClient.builder().baseUrl("http://jeju.mms.gislab.co.kr/mms_new/").build();
        return restClient.get().uri(uriBuilder -> uriBuilder.path("GEONET.getTimePopByCircle.php")
                .queryParam("SELECT", "M_POP_00,M_POP_10,M_POP_20,M_POP_30,M_POP_40,M_POP_50,M_POP_60,M_POP_70,M_POP_80,M_POP_90,W_POP_00,W_POP_10,W_POP_20,W_POP_30,W_POP_40,W_POP_50,W_POP_60,W_POP_70,W_POP_80,W_POP_90")
                .queryParam("X", x)
                .queryParam("Y", y)
                .queryParam("R", "150")
                .build()
        ).retrieve().toEntity(String.class);

//        http://jeju.mms.gislab.co.kr/mms_new/GEONET.getTimePopByCircle.php?SELECT=M_POP_00,M_POP_10,M_POP_20,M_POP_30,M_POP_40,M_POP_50,M_POP_60,M_POP_70,M_POP_80,M_POP_90,W_POP_00,W_POP_10,W_POP_20,W_POP_30,W_POP_40,W_POP_50,W_POP_60,W_POP_70,W_POP_80,W_POP_90&X=&Y=33.459134970543&R=150

    }
}
