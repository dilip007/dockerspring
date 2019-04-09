package com.gb.docker.dockerspring.controller;

import com.gb.docker.dockerspring.model.Transaction;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {


    @Autowired
    public Cache<Integer, Transaction> transactionCache;

    @GetMapping
    public ResponseEntity<DoubleSummaryStatistics> getTransactionStatistics() {

        //List<Transaction> transactions = transactionCache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
        //DoubleSummaryStatistics statistics = transactions.stream().collect(Collectors.summarizingDouble(Transaction::getAmount));

        DoubleSummaryStatistics transationStatistics = transactionCache.asMap().entrySet().
                stream().map(entry -> entry.getValue()).collect(Collectors.summarizingDouble(Transaction::getAmount));

        /*Statistics statistics = new Statistics();
        statistics.setAvg(transationStatistics.getAverage());
        statistics.setCount(transationStatistics.getCount());
        statistics.setMax(transationStatistics.getMax());
        statistics.setMin(transationStatistics.getMin());
        statistics.setSum(transationStatistics.getSum());*/

        return new ResponseEntity<>(transationStatistics, HttpStatus.OK);
    }


}
