package com.gwakkili.devbe.config;
import com.gwakkili.devbe.security.filter.RefreshTokenAuthenticationFilter;
import com.gwakkili.devbe.security.service.JwtService;
import com.gwakkili.devbe.security.filter.mailPasswordAuthenticationFilter;
import com.gwakkili.devbe.security.filter.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CorsConfigurationSource configurationSource;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final JwtService jwtService;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(@Qualifier("corsConfigurationSource") CorsConfigurationSource configurationSource,
                          AuthenticationSuccessHandler authenticationSuccessHandler,
                          AuthenticationFailureHandler authenticationFailureHandler,
                          AuthenticationEntryPoint authenticationEntryPoint,
                          JwtService jwtService, AccessDeniedHandler accessDeniedHandler){
        this.configurationSource = configurationSource;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtService = jwtService;
    }    //jwt 방식

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors-> cors.configurationSource(configurationSource))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(FormLoginConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .addFilter(jwtAuthenticationFilter(authenticationManager))
                .exceptionHandling(handler-> handler
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilter(jwtAuthorizationFilter(authenticationManager))
                .addFilterBefore(refreshTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

    public mailPasswordAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager){
        return new mailPasswordAuthenticationFilter(authenticationManager,authenticationSuccessHandler, authenticationFailureHandler);
    }

    public JwtAuthorizationFilter jwtAuthorizationFilter(AuthenticationManager authenticationManager){
        return new JwtAuthorizationFilter(authenticationManager, jwtService, authenticationEntryPoint);
    }

    public RefreshTokenAuthenticationFilter refreshTokenAuthenticationFilter(){
        return new RefreshTokenAuthenticationFilter(jwtService, authenticationSuccessHandler, authenticationFailureHandler);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
