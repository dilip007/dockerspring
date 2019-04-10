package com.gb.docker.dockerspring.controller;

import com.gb.docker.dockerspring.model.Transaction;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
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
    public ResponseEntity transaction(@RequestBody Transaction transaction) throws InterruptedException {

        Timestamp from = Timestamp.from(Instant.now());
        long now = from.getTime();
        long timeStampFromTransaction = transaction.getTimeStamp();
        long diff = now - timeStampFromTransaction;
        long second = (diff / 1000) % 60;
        //long diff2 = now > timeStampFromTransaction ? now - timeStampFromTransaction : timeStampFromTransaction-now;
        if (second < 60) {
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

//long timeStampFromTransaction =  Timestamp.from(Instant.now()).getTime();
/* long millis = diff % 1000;
        long minute = (diff / (1000 * 60)) % 60;
        long hour = (diff / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
        System.out.println("time = " + time);*/
