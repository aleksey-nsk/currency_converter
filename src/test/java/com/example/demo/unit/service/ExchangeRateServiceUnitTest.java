package com.example.demo.unit.service;

import com.example.demo.dto.ExchangeRateDto;
import com.example.demo.entity.ExchangeRate;
import com.example.demo.repository.ExchangeRateRepository;
import com.example.demo.service.ExchangeRateService;
import com.example.demo.service.impl.ExchangeRateServiceImpl;
import com.example.demo.util.ExchangeRateComparator;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ExchangeRateServiceImpl.class)
@ActiveProfiles("test")
@Log4j2
public class ExchangeRateServiceUnitTest {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @MockBean
    private ExchangeRateRepository exchangeRateRepository;

    private ExchangeRate createRate(Long id) {
        Date currentDate = new Date();
        String idCB = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        String numCode = RandomStringUtils.randomNumeric(3);
        String charCode = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        Integer nominal = 100;
        String name = "Валюта_" + RandomStringUtils.randomAlphabetic(10);
        BigDecimal value = new BigDecimal(RandomStringUtils.randomNumeric(3));

        ExchangeRate rate = new ExchangeRate(id, currentDate, idCB, numCode, charCode, nominal, name, value);
        log.debug("rate: " + rate);

        return rate;
    }

    @Test
    @DisplayName("Успешный поиск обменных курсов (данные берутся из базы)")
    public void findCurrentRateFromDBSuccess() {
        ExchangeRate created1 = createRate(1L);
        ExchangeRate created2 = createRate(2L);
        ExchangeRate created3 = createRate(3L);

        List<ExchangeRate> list = new ArrayList<>();
        list.add(created1);
        list.add(created2);
        list.add(created3);
        log.debug("list: " + list);

        List<ExchangeRate> sortedList = list.stream()
                .sorted(new ExchangeRateComparator())
                .collect(Collectors.toList());
        log.debug("sortedList: " + sortedList);

        List<ExchangeRateDto> sortedDtoList = sortedList.stream()
                .map(it -> ExchangeRateDto.valueOf(it))
                .collect(Collectors.toList());
        log.debug("sortedDtoList: " + sortedDtoList);

        Mockito.doReturn(sortedList)
                .when(exchangeRateRepository).findAllByDate(Mockito.any());

        List<ExchangeRateDto> actual = exchangeRateService.findCurrentRate();
        log.debug("actual: " + actual);

        assertThat(actual).size().isEqualTo(3);
        assertThat(actual).isEqualTo(sortedDtoList);
    }

    @Test
    @DisplayName("Успешный поиск обменных курсов (данные берутся с сайта ЦБ)")
    public void findCurrentRateFromCBSuccess() {
        Mockito.doReturn(Collections.emptyList())
                .when(exchangeRateRepository).findAllByDate(Mockito.any());

        List<ExchangeRateDto> actual = exchangeRateService.findCurrentRate();
        log.debug("actual: " + actual);

        // 34 пришло из ЦБ + российский рубль = 35
        assertThat(actual).size().isEqualTo(35);
    }
}
