package com.estoque.dto;


public record AuthenticationResponse(String token) {
    public String getToken() {
        return this.token();
    }
}
