package com.springboot.controller;


import com.springboot.entity.datasource.City;
import com.springboot.entity.datasource2.House;
import com.springboot.service.CityService;
import com.springboot.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    private CityService cityService;

    @Autowired
    private HouseService houseService;

    @GetMapping("/testDataSource")
    public Map testDataSource() {
        Map map = new HashMap();
        List<City> cityList = cityService.getAllCitys();
        List<House> houseList = houseService.getAllHouse();
        map.put("cityList", cityList);
        map.put("houseList",houseList);
        return map;
    }
}
