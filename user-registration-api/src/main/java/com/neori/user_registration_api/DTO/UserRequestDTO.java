package com.neori.user_registration_api.DTO;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserRequestDTO {
	@NotEmpty(message = "El nombre no puede estar vacío")
    private String name;

    @Email(message = "El correo electrónico debe tener un formato válido")
    @NotEmpty(message = "El correo electrónico no puede estar vacío")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    private String password;
    private List<PhoneDTO> phones;

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PhoneDTO> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneDTO> phones) {
        this.phones = phones;
    }
}
