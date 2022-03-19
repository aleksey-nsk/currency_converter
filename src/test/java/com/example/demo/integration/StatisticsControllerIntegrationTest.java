package com.example.demo.integration;

import com.example.demo.dto.StatisticsDto;
import com.example.demo.entity.Statistics;
import com.example.demo.repository.StatisticsRepository;
import com.example.demo.util.StatisticsDtoComparator;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Log4j2
public class StatisticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StatisticsRepository statisticsRepository;

    private static final String BASE_URL = "/api/v1/statistics";

    @AfterEach
    void tearDown() {
        statisticsRepository.deleteAll();
    }

    private Statistics saveStatisticsInDB() {
        String fromValute = "Валюта_" + RandomStringUtils.randomAlphabetic(10);
        String toValute = "Валюта_" + RandomStringUtils.randomAlphabetic(10);
        Integer amountConvert = 1;
        BigDecimal averageRate = new BigDecimal(RandomStringUtils.randomNumeric(3));
        BigDecimal sumConvert = new BigDecimal(RandomStringUtils.randomNumeric(4));

        Statistics statistics = new Statistics();
        statistics.setFromValute(fromValute);
        statistics.setToValute(toValute);
        statistics.setAmountConvert(amountConvert);
        statistics.setAverageRate(averageRate);
        statistics.setSumConvert(sumConvert);
        log.debug("statistics: " + statistics);

        Statistics savedStatistics = statisticsRepository.save(statistics);
        log.debug("savedStatistics: " + savedStatistics);
        return savedStatistics;
    }

    @Test
    @DisplayName("Успешный поиск всей статистики по конвертациям")
    public void findAllSuccess() throws Exception {
        Statistics saved1 = saveStatisticsInDB();
        Statistics saved2 = saveStatisticsInDB();
        assertThat(statisticsRepository.findAll().size()).isEqualTo(2);

        List<Statistics> list = new ArrayList<>();
        list.add(saved1);
        list.add(saved2);
        log.debug("list: " + list);

        List<StatisticsDto> sortedList = list.stream()
                .map(it -> StatisticsDto.valueOf(it))
                .sorted(new StatisticsDtoComparator())
                .collect(Collectors.toList());
        log.debug("sortedList: " + sortedList);

        String savedAsJson = objectMapper.writeValueAsString(sortedList);
        log.debug("savedAsJson: " + savedAsJson);

        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(savedAsJson, true));
        assertThat(statisticsRepository.findAll().size()).isEqualTo(2);
    }
}
