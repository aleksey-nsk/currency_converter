package com.example.demo.service.impl;

import com.example.demo.dto.ExchangeRateDto;
import com.example.demo.entity.ExchangeRate;
import com.example.demo.repository.ExchangeRateRepository;
import com.example.demo.service.ExchangeRateService;
import com.example.demo.util.CBUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public List<ExchangeRateDto> findCurrentRate() {
        Date currentDate = new Date();
        List<ExchangeRateDto> currentRate = exchangeRateRepository.findAllByDate(currentDate)
                .stream()
                .map(it -> ExchangeRateDto.valueOf(it))
                .collect(Collectors.toList());

        if (currentRate.size() == 0) {
            log.debug("В БД отсутствует список обменных курсов на текущую дату: " + currentDate);
            List<ExchangeRate> listFromCB = CBUtil.getCurrentRate(currentDate);
            exchangeRateRepository.saveAll(listFromCB); // сохранить весь список из ЦБ в БД
            log.debug("В БД сохранён список: " + listFromCB);
            currentRate = listFromCB.stream()
                    .map(it -> ExchangeRateDto.valueOf(it))
                    .collect(Collectors.toList());
        }

        log.debug("currentRate: " + currentRate);
        return currentRate;
    }
}
