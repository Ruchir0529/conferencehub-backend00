package com.conferencehub.dto;

import jakarta.validation.constraints.*;

public class SignupRequestDTO {

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private String institution;
    private String phone;

    public SignupRequestDTO() {}

    public SignupRequestDTO(String name, String email, String password, String institution, String phone) {
        this.name = name; this.email = email; this.password = password;
        this.institution = institution; this.phone = phone;
    }

    public String getName()                        { return name; }
    public void setName(String name)               { this.name = name; }
    public String getEmail()                       { return email; }
    public void setEmail(String email)             { this.email = email; }
    public String getPassword()                    { return password; }
    public void setPassword(String password)       { this.password = password; }
    public String getInstitution()                 { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }
    public String getPhone()                       { return phone; }
    public void setPhone(String phone)             { this.phone = phone; }
}
