package com.devon.building.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterDTO {
    @NotBlank(message = "Full name is required")
    String fullName;

    @NotBlank(message = "User name is required")
    @JsonProperty("username")
    String userName;

    @NotBlank(message = "Password is required")
    String password;

    @JsonProperty("confirmPassword")
    @NotBlank(message = "Retype password is required")
    String retypePassword;
}
