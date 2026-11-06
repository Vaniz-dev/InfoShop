package com.Vaniz.InfoShop.service;

import com.Vaniz.InfoShop.model.Perfil;
import com.Vaniz.InfoShop.model.Usuario;
import com.Vaniz.InfoShop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastroService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public Usuario registrar(String nome, String email, Long telefone, String senhaPura) {
        if (repo.existsByEmail(email)) {
            throw new IllegalArgumentException("Este email já está cadastrado");
        }

        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setTelefone(telefone);
        u.setSenha(encoder.encode(senhaPura));
        u.setNivel(Perfil.USER);
        return repo.save(u);
    }
}