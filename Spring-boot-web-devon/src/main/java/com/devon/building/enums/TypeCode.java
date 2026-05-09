package com.devon.building.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public enum TypeCode {
    TANG_TRET("Tầng trệt"),
    NOI_THAT("Nội thất"),
    NGUYEN_CAN("Nguyên căn");

    private final String name;

    TypeCode(String name) {
        this.name = name;
    }

    public static Map<String, String> getTypeCode() {
        Map<String, String> typeCode = new LinkedHashMap<>();
        for (TypeCode type : TypeCode.values()) {
            typeCode.put(type.toString(), type.getName());
        }
        return typeCode;
    }
}
