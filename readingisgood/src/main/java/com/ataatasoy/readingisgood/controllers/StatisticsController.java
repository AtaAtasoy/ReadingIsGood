package com.ataatasoy.readingisgood.controllers;

import com.ataatasoy.readingisgood.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService service;

    @GetMapping(value = "/statistics/monthly/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Object getMonthlyStatisticsForCustomer(@PathVariable Long id){
        return service.getUserMonthlyStatistics(id);
    }
}
