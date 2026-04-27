package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.TipoUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails { // ← 1. IMPLEMENTAR UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    private Boolean ativo;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Aluno aluno;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Professor professor;

    // =========================================================================
    // 2. ADICIONAR OS MÉTODOS OBRIGATÓRIOS DA INTERFACE USERDETAILS
    // =========================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // É AQUI QUE A MÁGICA ACONTECE!
        // O Spring agora sabe que o seu TipoUsuario é a "Authority" dele.
        return List.of(new SimpleGrantedAuthority(this.tipo.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // O Spring usa o email como o login principal
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Se "ativo" for null, consideramos true por segurança para não bloquear contas novas
        return this.ativo != null ? this.ativo : true;
    }
}