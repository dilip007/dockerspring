package com.gb.docker.dockerspring.controller;

import com.gb.docker.dockerspring.model.Transaction;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    public Cache<Integer, Transaction> transactionCache;

    private final AtomicInteger atomicInteger = new AtomicInteger();

    @PostMapping
    public ResponseEntity transaction(@RequestBody Transaction transaction) {
        int utcDayNow = LocalDateTime.now(ZoneId.of("UTC")).getDayOfWeek().getValue();
        int utcHourNow = LocalDateTime.now(ZoneId.of("UTC")).getHour();
        int utcMinuteNow = LocalDateTime.now(ZoneId.of("UTC")).getMinute();
        int transactionDay = Instant.ofEpochMilli(transaction.getTimeStamp()).atZone(ZoneId.of("UTC")).getDayOfWeek().getValue();
        int transactionHour = Instant.ofEpochMilli(transaction.getTimeStamp()).atZone(ZoneId.of("UTC")).getHour();
        int transactionMinute = Instant.ofEpochMilli(transaction.getTimeStamp()).atZone(ZoneId.of("UTC")).getMinute();
        if (transactionDay == utcDayNow && transactionHour == utcHourNow && utcMinuteNow == transactionMinute) {
            transactionCache.put(atomicInteger.getAndIncrement(), transaction);
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/all")
    public List<Transaction> getAllTransactionUnder_60_seconds() {
        return transactionCache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }
}

