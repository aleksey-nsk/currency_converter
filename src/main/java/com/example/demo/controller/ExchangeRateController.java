package com.example.demo.controller;

import com.example.demo.dto.ExchangeRateDto;
import com.example.demo.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currentRate")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExchangeRateDto> findCurrentRate() {
        return exchangeRateService.findCurrentRate();
    }
}
