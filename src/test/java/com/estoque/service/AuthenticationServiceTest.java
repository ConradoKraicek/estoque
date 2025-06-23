package com.estoque.service;

import com.estoque.dto.AuthenticationRequest;
import com.estoque.dto.AuthenticationResponse;
import com.estoque.dto.RegisterRequest;
import com.estoque.model.Role;
import com.estoque.model.Usuario;
import com.estoque.repository.UsuarioRepository;
import com.estoque.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void deveRegistrarUsuarioComSucesso() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Teste User", "teste@example.com", "senha123", Role.ADMIN);

        when(passwordEncoder.encode("senha123")).thenReturn("senha123_encoded");
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("jwt_token");

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());

        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(jwtService).generateToken(any(Usuario.class));
    }

    @Test
    void deveAutenticarUsuarioComSucesso() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("teste@example.com", "senha123");
        Usuario usuario = new Usuario("Teste User", "teste@example.com", "senha123_encoded", Role.ADMIN);

        when(usuarioRepository.findByEmail("teste@example.com")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("jwt_token");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt_token", response.getToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("teste@example.com", "senha123"));
        verify(usuarioRepository).findByEmail("teste@example.com");
        verify(jwtService).generateToken(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("naoexiste@example.com", "senha123");

        when(usuarioRepository.findByEmail("naoexiste@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            authenticationService.authenticate(request);
        });

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("naoexiste@example.com", "senha123"));
        verify(usuarioRepository).findByEmail("naoexiste@example.com");
        verify(jwtService, never()).generateToken(any(Usuario.class));
    }
}
