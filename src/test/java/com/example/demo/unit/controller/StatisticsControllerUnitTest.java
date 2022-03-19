package com.example.demo.unit.controller;

import com.example.demo.controller.StatisticsController;
import com.example.demo.dto.StatisticsDto;
import com.example.demo.entity.Statistics;
import com.example.demo.service.StatisticsService;
import com.example.demo.util.StatisticsDtoComparator;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatisticsController.class)
@ActiveProfiles("test")
@Log4j2
public class StatisticsControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticsService statisticsService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/statistics";

    private Statistics createStatistics(Long id) {
        String fromValute = "Валюта_" + RandomStringUtils.randomAlphabetic(10);
        String toValute = "Валюта_" + RandomStringUtils.randomAlphabetic(10);
        Integer amountConvert = 1;
        BigDecimal averageRate = new BigDecimal(RandomStringUtils.randomNumeric(3));
        BigDecimal sumConvert = new BigDecimal(RandomStringUtils.randomNumeric(4));

        Statistics statistics = new Statistics();
        statistics.setId(id);
        statistics.setFromValute(fromValute);
        statistics.setToValute(toValute);
        statistics.setAmountConvert(amountConvert);
        statistics.setAverageRate(averageRate);
        statistics.setSumConvert(sumConvert);

        log.debug("statistics: " + statistics);
        return statistics;
    }

    @Test
    @DisplayName("Успешный поиск всей статистики по конвертациям")
    public void findAllSuccess() throws Exception {
        Statistics stat1 = createStatistics(1L);
        Statistics stat2 = createStatistics(2L);
        Statistics stat3 = createStatistics(3L);

        List<Statistics> list = new ArrayList<>();
        list.add(stat1);
        list.add(stat2);
        list.add(stat3);
        log.debug("list: " + list);

        List<StatisticsDto> sortedList = list.stream()
                .map(it -> StatisticsDto.valueOf(it))
                .sorted(new StatisticsDtoComparator())
                .collect(Collectors.toList());
        log.debug("sortedList: " + sortedList);

        String savedAsJson = objectMapper.writeValueAsString(sortedList);
        log.debug("savedAsJson: " + savedAsJson);

        Mockito.doReturn(sortedList)
                .when(statisticsService).findAll();

        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(savedAsJson, true));
    }
}
