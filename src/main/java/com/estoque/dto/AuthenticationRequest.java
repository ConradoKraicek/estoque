package com.estoque.dto;


public record AuthenticationRequest(String email, String password) {
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
