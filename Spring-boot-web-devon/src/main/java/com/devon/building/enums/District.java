package com.devon.building.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public enum District {
    QUAN_1("Quận 1"),
    QUAN_2("Quận 2"),
    QUAN_3("Quận 3"),
    QUAN_4("Quận 4"),
    QUAN_5("Quận 5"),
    QUAN_6("Quận 6"),
    QUAN_7("Quận 7"),
    QUAN_8("Quận 8"),
    QUAN_9("Quận 9"),
    QUAN_10("Quận 10"),
    QUAN_11("Quận 11"),
    QUAN_12("Quận 12"),
    QUAN_TB("Quận Tân Bình"),
    QUAN_BT("Quận Bình Thạnh");
    private final String districtName;

    District(String districtName) {
        this.districtName = districtName;
    }

    public static Map<String, String> getDistrict() {
        Map<String, String> district = new LinkedHashMap<>();
        for (District d : District.values()) {
            district.put(d.toString(), d.getDistrictName());
        }
        return district;
    }
}
