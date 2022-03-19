package com.example.demo.unit.controller;

import com.example.demo.controller.ExchangeRateController;
import com.example.demo.dto.ExchangeRateDto;
import com.example.demo.entity.ExchangeRate;
import com.example.demo.service.ExchangeRateService;
import com.example.demo.util.ExchangeRateComparator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ExchangeRateController.class)
@ActiveProfiles("test")
@Log4j2
public class ExchangeRateControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/rate";

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
    @DisplayName("Успешный поиск всех обменных курсов на текущую дату")
    public void findCurrentRateSuccess() throws Exception {
        ExchangeRate created1 = createRate(1L);
        ExchangeRate created2 = createRate(2L);

        List<ExchangeRate> list = new ArrayList<>();
        list.add(created1);
        list.add(created2);
        log.debug("list: " + list);

        List<ExchangeRateDto> sortedList = list.stream()
                .sorted(new ExchangeRateComparator())
                .map(it -> ExchangeRateDto.valueOf(it))
                .collect(Collectors.toList());
        log.debug("sortedList: " + sortedList);

        String expectedJson = objectMapper.writeValueAsString(sortedList);
        log.debug("expectedJson: " + expectedJson);

        Mockito.doReturn(sortedList)
                .when(exchangeRateService).findCurrentRate();

        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson, true));
    }
}
