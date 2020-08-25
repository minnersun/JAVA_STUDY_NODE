package com.springboot.service;

import com.springboot.entity.datasource2.House;
import com.springboot.mapper.datasource2.HouseMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {
    @Autowired
    private HouseMap houseMap;

    public List<House> getAllHouse(){
        return houseMap.getAllHouse();
    }
}
