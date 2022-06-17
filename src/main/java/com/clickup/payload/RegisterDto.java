package com.clickup.payload;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {
    @NotNull(message = "fullName bush bulishi mumkin emas")
    private String fullName;
    @NotNull(message = "email bush bulmasin")
    private String email;
    @NotNull(message = "passwordni kiriting")
    private String password;


}
