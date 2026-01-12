package de.htwg.in.schneider.saitenweise.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Für APIs oft deaktiviert
            .cors(Customizer.withDefaults()) // Nutzt die corsConfigurationSource Bean unten
            .authorizeHttpRequests(auth -> auth
                // Health Check öffentlich lassen
                .requestMatchers("/actuator/health").permitAll()

                // Spezifische API-Endpunkte (Explizit für Übersichtlichkeit)
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/projects/**").authenticated()
                .requestMatchers("/api/task/**").authenticated()

                // Fallback: Alles unter /api braucht Login (JWT)
                .requestMatchers("/api/**").authenticated()

                // Alles andere (z.B. statische Dateien, falls vorhanden) erlauben oder blockieren
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter()))
            );

        return http.build();
    }

    /**
     * WICHTIG: Damit das Frontend (Port 5173) auf das Backend (Port 8081) zugreifen darf.
     * Ohne das werden Requests vom Browser blockiert (CORS-Fehler).
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Erlaube das Vue-Frontend
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        
        // Erlaube gängige HTTP-Methoden
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Erlaube alle Header (wichtig für Authorization Header mit Bearer Token)
        configuration.setAllowedHeaders(List.of("*"));
        
        // Erlaube Credentials (falls Cookies/Auth-Header nötig sind)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}