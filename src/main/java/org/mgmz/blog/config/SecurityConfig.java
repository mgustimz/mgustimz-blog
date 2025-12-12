package org.mgmz.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    // 2. Added 'throws Exception' (Required for http.build())
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) {
        http.authorizeHttpRequests(requests -> requests
                        .requestMatchers("/admin/**").authenticated()
                        .requestMatchers("/", "/blog", "/about", "/contact", "/rss", "/login",
                                "/css/**", "/js/**", "/images/**", "/tag/**", "/search", "/sitemap.xml")
                        .permitAll()
                        .requestMatchers("/{slug}")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin", true)
                        .permitAll()
                )
                .rememberMe(remember -> remember
                        .key("superSecretKeyForBearBlog")
                        .tokenValiditySeconds(1209600)
                        .userDetailsService(userDetailsService)
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(request ->
                                request.getMethod().equals("GET") &&
                                        request.getRequestURI().equals("/logout")
                        )
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(admin);
    }
}