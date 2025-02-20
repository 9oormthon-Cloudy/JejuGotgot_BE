package com.cloudy.api;

import com.cloudy.api.adapter.database.PlaceEntity;
import com.cloudy.api.adapter.database.PlaceRepository;
import com.cloudy.api.dto.GetPlaceInBoxResponse;
import com.cloudy.api.dto.GetPlaceNearestResponse;
import com.cloudy.api.dto.GetPlaceResponse;
import com.cloudy.api.service.PlaceService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ApiController {

    private final PlaceRepository placeRepository;
    private final PlaceService placeService;

    public ApiController(PlaceRepository placeRepository, PlaceService placeService) {
        this.placeRepository = placeRepository;
        this.placeService = placeService;
    }

    @Hidden
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/circle")
//    public ResponseEntity<?> getCircle(@RequestParam("x") String x, @RequestParam("y") String y) {
//        var restClient = RestClient.builder().baseUrl("http://jeju.mms.gislab.co.kr/mms_new/").build();
//        return restClient.get().uri(uriBuilder -> uriBuilder.path("GEONET.getTimePopByCircle.php")
//                .queryParam("SELECT", "M_POP_00,M_POP_10,M_POP_20,M_POP_30,M_POP_40,M_POP_50,M_POP_60,M_POP_70,M_POP_80,M_POP_90,W_POP_00,W_POP_10,W_POP_20,W_POP_30,W_POP_40,W_POP_50,W_POP_60,W_POP_70,W_POP_80,W_POP_90")
//                .queryParam("X", x)
//                .queryParam("Y", y)
//                .queryParam("R", "150")
//                .build()
//        ).retrieve().toEntity(String.class);
//
////        http://jeju.mms.gislab.co.kr/mms_new/GEONET.getTimePopByCircle.php?SELECT=M_POP_00,M_POP_10,M_POP_20,M_POP_30,M_POP_40,M_POP_50,M_POP_60,M_POP_70,M_POP_80,M_POP_90,W_POP_00,W_POP_10,W_POP_20,W_POP_30,W_POP_40,W_POP_50,W_POP_60,W_POP_70,W_POP_80,W_POP_90&X=&Y=33.459134970543&R=150
//
//    }

    @GetMapping("place/nearest")
    public ResponseEntity<GetPlaceNearestResponse> getNearest(@RequestParam("x") String x, @RequestParam("y") String y) {
        var place = placeRepository.findNearest(Double.parseDouble(x), Double.parseDouble(y));
        return ResponseEntity.ok().body(new GetPlaceNearestResponse(place.getId()));
    }

    @Hidden
    @PostMapping("place")
    public ResponseEntity<?> place(@RequestBody PlaceEntity place) {
        placeRepository.save(place);

        return ResponseEntity.ok().body(place);
    }

    @GetMapping("place/{id}")
    public ResponseEntity<GetPlaceResponse> getPlaceById(@PathVariable("id") String id) {
        System.out.println(id);
        return ResponseEntity.ok().body(placeService.getPlace(Integer.parseInt(id)));
    }

    @GetMapping("place")
    public ResponseEntity<List<GetPlaceInBoxResponse>> getPlacesInBox(@RequestParam("x1") String x1, @RequestParam("y1") String y1,
                                                                      @RequestParam("x2") String x2, @RequestParam("y2") String y2) {
        return ResponseEntity.ok().body(placeService.getPlaceInBox(Double.parseDouble(x1), Double.parseDouble(x2),
                Double.parseDouble(y1), Double.parseDouble(y2)));
    }

    @PostMapping("place/{id}/bookmark")
    public ResponseEntity<Void> createBookmark(@PathVariable("id") String id) {
        placeService.addBookmark(Integer.parseInt(id));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("place/{id}/bookmark")
    public ResponseEntity<Void> deleteBookmark(@PathVariable("id") String id) {
        placeService.deleteBookmark(Integer.parseInt(id));
        return ResponseEntity.ok().build();
    }
}
