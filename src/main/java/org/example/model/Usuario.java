package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.enums.TipoUsuario;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        if (tipo == TipoUsuario.COORDENADOR) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_PROFESSOR"),
                    new SimpleGrantedAuthority("ROLE_COORDENADOR")
            );
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + tipo.name()));

    }

    @Override
    public String getUsername(){ return email; }


    @Override
    public String getPassword(){ return senha;}


    @Override
    public boolean isAccountNonExpired(){ return true;}


    @Override
    public boolean isAccountNonLocked(){ return true;}


    @Override
    public boolean isCredentialsNonExpired(){ return true;}


    @Override
    public boolean isEnabled(){ return Boolean.TRUE.equals(ativo);}

}
