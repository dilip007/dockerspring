package com.gb.docker.dockerspring.controller;

import com.gb.docker.dockerspring.model.Transaction;
import com.google.common.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Transaction> transaction(@RequestBody Transaction transaction) {
        transaction.setTimeStamp(System.currentTimeMillis());
        transactionCache.put(atomicInteger.getAndIncrement(), transaction);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<Transaction> getAllTransactionUnder_60_seconds() {
        return transactionCache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }
}
