package com.ataatasoy.readingisgood.services;

import com.ataatasoy.readingisgood.models.Statistics;
import com.ataatasoy.readingisgood.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsRepository repository;

    public List<Statistics> getUserMonthlyStatistics(Long customerId){
        return repository.findMonthlyStatisticsForCustomer(customerId);
    }
}
