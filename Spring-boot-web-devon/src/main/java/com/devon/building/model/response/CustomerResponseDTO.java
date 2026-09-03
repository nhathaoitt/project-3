package com.devon.building.model.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomerResponseDTO {
    private String fullName;
    private String email;
    private String phone;
    private String demand;
    private String status;
}
