package com.springboot.mapper.datasource;

import com.springboot.entity.datasource.City;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityMap {
    List<City> getAllCity();
}
