package com.example.demo.integration;

import com.example.demo.dto.ExchangeRateDto;
import com.example.demo.entity.ExchangeRate;
import com.example.demo.repository.ExchangeRateRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Log4j2
public class ExchangeRateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private static final String BASE_URL = "/api/v1/rate";
    private static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1_000;

    @AfterEach
    void tearDown() {
        exchangeRateRepository.deleteAll();
    }

    private ExchangeRate saveRateInDB(Date date) {
        String idCB = RandomStringUtils.randomAlphanumeric(7).toUpperCase();
        String numCode = RandomStringUtils.randomNumeric(3);
        String charCode = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        Integer nominal = 100;
        String name = "Валюта_" + RandomStringUtils.randomAlphabetic(10);
        BigDecimal value = new BigDecimal(RandomStringUtils.randomNumeric(3));

        ExchangeRate rate = new ExchangeRate();
        rate.setDate(date);
        rate.setIdCB(idCB);
        rate.setNumCode(numCode);
        rate.setCharCode(charCode);
        rate.setNominal(nominal);
        rate.setName(name);
        rate.setValue(value);
        log.debug("rate: " + rate);

        ExchangeRate savedRate = exchangeRateRepository.save(rate);
        log.debug("savedRate: " + savedRate);
        return savedRate;
    }

    @Test
    @DisplayName("Успешный поиск всех обменных курсов на текущую дату")
    public void findCurrentRateSuccess() throws Exception {
        Date currentDate = new Date();
        Date yesterdayDate = new Date(currentDate.getTime() - MILLISECONDS_IN_DAY);
        log.debug("currentDate: " + currentDate);
        log.debug("yesterdayDate: " + yesterdayDate);

        ExchangeRateDto yesterday1 = ExchangeRateDto.valueOf(saveRateInDB(yesterdayDate));
        ExchangeRateDto yesterday2 = ExchangeRateDto.valueOf(saveRateInDB(yesterdayDate));

        assertThat(exchangeRateRepository.findAll().size()).isEqualTo(2);

        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].date").isNotEmpty())
                .andExpect(jsonPath("$.[0].idCB").isNotEmpty())
                .andExpect(jsonPath("$.[0].numCode").isNotEmpty())
                .andExpect(jsonPath("$.[0].charCode").isNotEmpty())
                .andExpect(jsonPath("$.[0].nominal").isNumber())
                .andExpect(jsonPath("$.[0].name").isString())
                .andExpect(jsonPath("$.[0].value").isNumber());

        // 34 пришло из ЦБ + российский рубль + 2 вчерашних курса = 37
        assertThat(exchangeRateRepository.findAll().size()).isEqualTo(37);
    }
}
