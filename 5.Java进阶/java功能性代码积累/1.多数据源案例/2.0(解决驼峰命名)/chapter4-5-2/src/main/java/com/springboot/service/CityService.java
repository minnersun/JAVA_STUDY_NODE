package com.springboot.service;

import com.springboot.entity.datasource.City;
import com.springboot.mapper.datasource.CityMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityMap cityMap;

    public List<City> getAllCitys(){
        return cityMap.getAllCity();
    }
}
