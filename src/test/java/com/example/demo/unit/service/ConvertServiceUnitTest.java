package com.example.demo.unit.service;

import com.example.demo.dto.ConvertDto;
import com.example.demo.entity.Convert;
import com.example.demo.exception.ExchangeRateNotFoundException;
import com.example.demo.repository.ConvertRepository;
import com.example.demo.repository.ExchangeRateRepository;
import com.example.demo.service.ConvertService;
import com.example.demo.service.StatisticsService;
import com.example.demo.service.impl.ConvertServiceImpl;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ConvertServiceImpl.class)
@ActiveProfiles("test")
@Log4j2
public class ConvertServiceUnitTest {

    @Autowired
    private ConvertService convertService;

    @MockBean
    private ExchangeRateRepository exchangeRateRepository;

    @MockBean
    private ConvertRepository convertRepository;

    @MockBean
    private StatisticsService statisticsService;

    private Convert createConvert() {
        String fromValute = "Валюта_" + RandomStringUtils.randomAlphabetic(10);
        BigDecimal amount = new BigDecimal(RandomStringUtils.randomNumeric(4));
        String toValute = "Валюта_" + RandomStringUtils.randomAlphabetic(10);

        Convert convert = new Convert();
        convert.setFromValute(fromValute);
        convert.setAmount(amount);
        convert.setToValute(toValute);

        log.debug("convert: " + convert);
        return convert;
    }

    @Test
    @DisplayName("Конвертация не добавлена (не найден обменный курс)")
    public void saveFail() {
        ConvertDto convertDto = ConvertDto.valueOf(createConvert());

        Mockito.doReturn(Optional.empty())
                .when(exchangeRateRepository).findFirstByNameAndDate(Mockito.any(), Mockito.any());

        try {
            convertService.save(convertDto);
            throw new RuntimeException("Ожидаемое исключение не было выброшено");
        } catch (ExchangeRateNotFoundException e) {
            log.debug(e.getMessage());
            assertThat(e.getMessage()).contains(convertDto.getFromValute());
        }
    }
}
