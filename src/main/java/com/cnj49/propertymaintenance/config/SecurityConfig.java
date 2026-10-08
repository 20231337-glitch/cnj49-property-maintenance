package com.cnj49.propertymaintenance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Cau hinh xac thuc/phan quyen 3 vai tro: ADMIN, MANAGER, STAFF (muc 6).
 * Mat khau luon duoc bam BCrypt, khong bao gio luu plaintext.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Quy tac duoc xet tu tren xuong, gap quy tac dau tien khop thi dung.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        // Xoa du lieu: chi ADMIN.
                        .requestMatchers(HttpMethod.POST,
                                "/properties/*/delete", "/units/*/delete", "/contractors/*/delete",
                                "/categories/*/delete", "/expenses/*/delete", "/maintenance/*/delete",
                                "/maintenance/*/quotations/*/delete").hasRole("ADMIN")
                        // Duyet, dieu phoi, nghiem thu, chi phi va du lieu danh muc: MANAGER hoac ADMIN.
                        .requestMatchers(HttpMethod.POST,
                                "/maintenance/*/quotations/*/approve", "/maintenance/*/quotations/*/reject",
                                "/maintenance/*/close", "/maintenance/*/cancel", "/maintenance/*/status",
                                "/workorders", "/workorders/*/cancel", "/inspections",
                                "/expenses", "/expenses/*", "/properties", "/properties/*",
                                "/units", "/units/*", "/contractors", "/contractors/*",
                                "/categories", "/categories/*").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET,
                                "/workorders/create", "/inspections/create",
                                "/expenses/create", "/expenses/*/edit", "/properties/create", "/properties/*/edit",
                                "/units/create", "/units/*/edit", "/contractors/create", "/contractors/*/edit",
                                "/categories/create", "/categories/*/edit").hasAnyRole("MANAGER", "ADMIN")
                        // Con lai (xem du lieu, tao/sua yeu cau, nhap bao gia, cap nhat tien do): moi tai khoan.
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                // Trang loi 403/404/500 tu dung khong bi chan boi CSRF khi hien thi.
                .exceptionHandling(ex -> ex.accessDeniedPage("/errors/403"));
        return http.build();
    }
}
