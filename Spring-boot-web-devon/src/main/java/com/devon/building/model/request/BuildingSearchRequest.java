package com.devon.building.model.request;

import com.devon.building.model.dto.AbstractDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BuildingSearchRequest extends AbstractDTO {
    String name;
    Long floorArea;
    String district;
    String street;
    String ward;
    Long numberOfBasement;
    String direction;
    String level;
    Long rentAreaFrom;
    Long rentAreaTo;
    Long rentPriceFrom;
    Long rentPriceTo;
    String managerName;
    String managerPhone;
    Long staffId;
    List<String> typeCode;
}
