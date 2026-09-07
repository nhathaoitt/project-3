package com.devon.building.model.response;

import com.devon.building.model.dto.AbstractDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDTO extends AbstractDTO {
    String fullName;
    String email;
    String phone;
    String companyName;
    String demand;
    String status;
}
