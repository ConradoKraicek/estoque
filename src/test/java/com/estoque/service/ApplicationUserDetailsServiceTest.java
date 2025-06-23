package com.estoque.service;

import com.estoque.model.Role;
import com.estoque.model.Usuario;
import com.estoque.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ApplicationUserDetailsService userDetailsService;

    @Test
    void deveCarregarUsuarioPorEmailComSucesso() {
        // Arrange
        String email = "usuario@example.com";
        Usuario usuario = new Usuario("Nome Usuario", email, "senha_encoded", Role.ADMIN);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals("senha_encoded", userDetails.getPassword());
        // Verificar se existe alguma autoridade que contenha "ADMIN" no nome
        boolean temAutoridade = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.contains("ADMIN"));
        assertTrue(temAutoridade, "Deveria ter uma autoridade contendo 'ADMIN'");
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        String email = "naoexiste@example.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(email);
        });

        verify(usuarioRepository).findByEmail(email);
    }
}
