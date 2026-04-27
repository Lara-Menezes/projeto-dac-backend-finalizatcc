package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)  // ← Ativa @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // ==================== ENDPOINTS PÚBLICOS (SEM AUTENTICAÇÃO) ====================

                        // --------- Autenticação ---------
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/refresh").permitAll()

                        // --------- Swagger/OpenAPI ---------
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/webjars/**").permitAll()

                        // ==================== ALUNOS ====================
                        .requestMatchers(HttpMethod.POST, "/api/alunos/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/alunos", "/api/alunos/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/alunos/**").hasAnyAuthority("ALUNO", "COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/alunos/**").hasAnyAuthority("ALUNO", "COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/alunos/**").hasAnyAuthority("ALUNO", "COORDENADOR")

                        // ==================== PROFESSORES ====================
                        .requestMatchers(HttpMethod.POST, "/api/professores/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/professores", "/api/professores/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/professores/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/professores/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/professores/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")

                        // ==================== ÁREAS DE PESQUISA ====================
                        .requestMatchers(HttpMethod.GET, "/api/areas-pesquisa", "/api/areas-pesquisa/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/areas-pesquisa/create").hasAnyAuthority("COORDENADOR", "PROFESSOR")
                        .requestMatchers(HttpMethod.PUT, "/api/areas-pesquisa/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/areas-pesquisa/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/areas-pesquisa/**").hasAnyAuthority("COORDENADOR")

                        // ==================== TCCs ====================
                        .requestMatchers(HttpMethod.GET, "/api/tccs", "/api/tccs/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/tccs/create").hasAnyAuthority("ALUNO", "COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/tccs/**").hasAnyAuthority("ALUNO", "PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/tccs/**").hasAnyAuthority("ALUNO", "PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/tccs/**").hasAnyAuthority("COORDENADOR")

                        // ==================== AVALIADORES ====================
                        .requestMatchers(HttpMethod.GET, "/api/avaliadores", "/api/avaliadores/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/avaliadores/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/avaliadores/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/avaliadores/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/avaliadores/**").hasAnyAuthority("COORDENADOR")

                        // ==================== BANCAS ====================
                        .requestMatchers(HttpMethod.GET, "/api/bancas", "/api/bancas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/bancas/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/bancas/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/bancas/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/bancas/**").hasAnyAuthority("COORDENADOR")

                        // ==================== SUBMISSÕES ====================
                        .requestMatchers(HttpMethod.GET, "/api/submissoes", "/api/submissoes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/submissoes/**").hasAnyAuthority("ALUNO", "COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/submissoes/**").hasAnyAuthority("ALUNO", "COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/submissoes/**").hasAnyAuthority("ALUNO", "COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/submissoes/**").hasAnyAuthority("ALUNO", "COORDENADOR")

                        // ==================== ARQUIVOS ====================
                        .requestMatchers(HttpMethod.GET, "/api/arquivos", "/api/arquivos/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/arquivos/**").hasAnyAuthority("ALUNO", "COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/arquivos/**").hasAnyAuthority("ALUNO", "COORDENADOR")

                        // ==================== FEEDBACKS ====================
                        .requestMatchers(HttpMethod.GET, "/api/feedbacks", "/api/feedbacks/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/feedbacks/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/feedbacks/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/feedbacks/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/feedbacks/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")

                        // ==================== AVALIAÇÕES ====================
                        .requestMatchers(HttpMethod.GET, "/api/avaliacoes", "/api/avaliacoes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/avaliacoes/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/avaliacoes/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/avaliacoes/**").hasAnyAuthority("PROFESSOR", "COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/avaliacoes/**").hasAnyAuthority("COORDENADOR")

                        // ==================== USUÁRIOS ====================
                        .requestMatchers(HttpMethod.GET, "/api/usuarios", "/api/usuarios/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasAnyAuthority("COORDENADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAnyAuthority("COORDENADOR")

                        // ==================== DASHBOARD ====================
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/**").hasAnyAuthority("COORDENADOR")

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // URLs permitidas (adicione aqui suas URLs de produção)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",      // Frontend local
                "http://localhost:8080",      // Backend local
                "http://127.0.0.1:3000",      // Frontend IP local
                "http://127.0.0.1:8080",      // Backend IP local
                "http://localhost:5173"       // Vite dev server
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));

        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Headers que podem ser expostos
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With"
        ));

        // Permitir credenciais (cookies, headers de autenticação)
        configuration.setAllowCredentials(true);

        // Tempo de cache da pré-verificação CORS (em segundos)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}