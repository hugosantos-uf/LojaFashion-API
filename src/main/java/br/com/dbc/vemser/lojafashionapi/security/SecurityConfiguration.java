package br.com.dbc.vemser.lojafashionapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenService tokenService;
    private static final String ADMIN = "ADMIN";
    private static final String CLIENTE = "CLIENTE";
    private static final String MARKETING = "MARKETING";


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers("/", "/auth", "/cliente", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/usuario").hasRole(ADMIN)
                        .antMatchers(HttpMethod.GET, "/usuario/logado").authenticated()
                        .antMatchers(HttpMethod.PUT, "/usuario/trocar-senha").authenticated()
                        .antMatchers(HttpMethod.DELETE, "/usuario/**").hasRole(ADMIN) // Apenas ADMIN pode desativar contas

                        // PRODUTO
                        .antMatchers(HttpMethod.POST, "/produto").hasAnyRole(ADMIN, MARKETING)
                        .antMatchers(HttpMethod.PUT, "/produto/**").hasAnyRole(ADMIN, MARKETING)
                        .antMatchers(HttpMethod.DELETE, "/produto/**").hasAnyRole(ADMIN, MARKETING)
                        .antMatchers("/produto/**").permitAll()

                        // CATEGORIA
                        .antMatchers(HttpMethod.POST, "/categoria").hasAnyRole(ADMIN, MARKETING)
                        .antMatchers(HttpMethod.PUT, "/categoria/**").hasAnyRole(ADMIN, MARKETING)
                        .antMatchers(HttpMethod.DELETE, "/categoria/**").hasAnyRole(ADMIN, MARKETING)
                        .antMatchers("/categoria/**").permitAll()

                        // PEDIDO
                        .antMatchers(HttpMethod.POST, "/pedido").hasRole(CLIENTE)
                        .antMatchers(HttpMethod.PUT, "/pedido/**/status").hasRole(ADMIN)
                        .antMatchers(HttpMethod.DELETE, "/pedido/**").hasRole(ADMIN)
                        .antMatchers("/pedido/por-cliente/**").hasAnyRole(ADMIN, CLIENTE)
                        .antMatchers("/pedido/**").hasRole(ADMIN)

                        // RELATORIO
                        .antMatchers("/relatorio/**").hasRole(ADMIN)

                        // QUALQUER OUTRA REQUISIÇÃO
                        .anyRequest().authenticated()
                );
        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**");
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}