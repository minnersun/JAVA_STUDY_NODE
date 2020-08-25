package com.springboot.mapper.datasource2;

import com.springboot.entity.datasource2.House;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseMap {
    List<House> getAllHouse();
}
