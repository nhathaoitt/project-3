package com.devon.building.model.response;

import com.devon.building.model.dto.AbstractDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BuildingSearchResponse extends AbstractDTO {
    String name;
    String address;
    Long numberOfBasement;
    String managerName;
    String managerPhone;
    Long floorArea;
    String rentArea;
    Long price;
    String serviceFee;
    String brokerageFee;
}
