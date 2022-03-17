package com.example.demo.controller;

import com.example.demo.dto.ConvertDto;
import com.example.demo.service.ConvertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/convert")
@Tag(name = "Контроллер для конвертаций")
public class ConvertController {

    private final ConvertService convertService;

    @Autowired
    public ConvertController(ConvertService convertService) {
        this.convertService = convertService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Получить список всех конвертаций")
    public List<ConvertDto> findAll() {
        return convertService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить новую конвертацию в историю")
    public ConvertDto save(@RequestBody @Valid ConvertDto convertDto) {
        return convertService.save(convertDto);
    }
}
