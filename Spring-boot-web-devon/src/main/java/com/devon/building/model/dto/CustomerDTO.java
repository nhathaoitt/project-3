package com.devon.building.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO extends AbstractDTO{

    @NotBlank(message = "Full name must not be blank")
    private String fullName;
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must not be less than 10 digits")
    private String phone;
    private String demand;
    private String status;

}
