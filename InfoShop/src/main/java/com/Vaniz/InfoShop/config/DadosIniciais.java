package com.Vaniz.InfoShop.config;

import com.Vaniz.InfoShop.model.Perfil;
import com.Vaniz.InfoShop.model.Usuario;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DadosIniciais {

    @Bean
    CommandLineRunner criarAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.existsByEmail("admin@infoshop.com")) return;

            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@infoshop.com");
            admin.setTelefone(11999999999L);
            admin.setSenha(encoder.encode("admin123")); // troque depois
            admin.setNivel(Perfil.ADMIN);
            repo.save(admin);
        };
    }
}