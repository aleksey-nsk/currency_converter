package com.example.demo.unit.controller;

import com.example.demo.controller.ConvertController;
import com.example.demo.dto.ConvertDto;
import com.example.demo.entity.Convert;
import com.example.demo.exception.ExchangeRateNotFoundException;
import com.example.demo.service.ConvertService;
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
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ConvertController.class)
@ActiveProfiles("test")
@Log4j2
public class ConvertControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConvertService convertService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/convert";

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
    public void saveFail() throws Exception {
        ConvertDto convertDto = ConvertDto.valueOf(createConvert());

        String convertDtoAsJson = objectMapper.writeValueAsString(convertDto);
        log.debug("convertDtoAsJson: " + convertDtoAsJson);

        Mockito.doThrow(new ExchangeRateNotFoundException(convertDto.getFromValute(), new Date()))
                .when(convertService).save(convertDto);

        mockMvc.perform(post(BASE_URL).content(convertDtoAsJson).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
