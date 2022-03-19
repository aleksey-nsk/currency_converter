package com.example.demo.unit.service;

import com.example.demo.dto.StatisticsDto;
import com.example.demo.entity.Statistics;
import com.example.demo.repository.StatisticsRepository;
import com.example.demo.service.StatisticsService;
import com.example.demo.service.impl.StatisticsServiceImpl;
import com.example.demo.util.StatisticsDtoComparator;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = StatisticsServiceImpl.class)
@ActiveProfiles("test")
@Log4j2
public class StatisticsServiceUnitTest {

    @Autowired
    private StatisticsService statisticsService;

    @MockBean
    private StatisticsRepository statisticsRepository;

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
    public void findAllSuccess() {
        Statistics stat1 = createStatistics(1L);
        Statistics stat2 = createStatistics(2L);
        Statistics stat3 = createStatistics(3L);

        List<Statistics> list = new ArrayList<>();
        list.add(stat1);
        list.add(stat2);
        list.add(stat3);
        log.debug("list: " + list);

        List<StatisticsDto> sortedDtoList = list.stream()
                .map(it -> StatisticsDto.valueOf(it))
                .sorted(new StatisticsDtoComparator())
                .collect(Collectors.toList());
        log.debug("sortedDtoList: " + sortedDtoList);

        Mockito.doReturn(list)
                .when(statisticsRepository).findAll();

        List<StatisticsDto> actual = statisticsService.findAll();
        log.debug("actual: " + actual);

        assertThat(actual).size().isEqualTo(3);
        assertThat(actual).isEqualTo(sortedDtoList);
    }
}
