package com.example.demo.util;

import com.example.demo.dto.ConvertDto;

import java.util.Comparator;

public class ConvertDtoComparator implements Comparator<ConvertDto> {

    @Override
    public int compare(ConvertDto o1, ConvertDto o2) {
        return o2.getId().compareTo(o1.getId());
    }
}
