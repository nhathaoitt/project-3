package com.devon.building.model.request;

import com.devon.building.model.dto.AbstractDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingSearchRequest extends AbstractDTO {
      String name;
      Long floorArea;
      String district;
      String ward;
      String street;
      Long numberOfBasement;
      String direction;
      String level;
      Long rentAreaFrom;
      Long rentAreaTo;
      Long rentPriceFrom;
      Long rentPriceTo;
      String managerName;
      String managerPhoneNumber;
      Long staffId;
      List<String> typeCode;
}
