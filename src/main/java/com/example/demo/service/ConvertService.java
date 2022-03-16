package com.example.demo.service;

import com.example.demo.dto.ConvertDto;

import java.util.List;

/**
 * @author Aleksey Zhdanov
 * @version 1
 */
public interface ConvertService {

    /**
     * <p>Возвращает список всех конвертаций</p>
     *
     * @return Список конвертаций
     */
    List<ConvertDto> findAll();

    /**
     * <p>Добавляет новую конвертацию в историю</p>
     *
     * @param convertDto Данные конвертации для добавления
     * @return Сохранённая в БД конвертация
     */
    ConvertDto save(ConvertDto convertDto);
}
