package com.belous.api.security;


import com.belous.api.security.filter.CustomAuthenticationFilter;
import com.belous.api.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by Vincenzo Racca
 *
 * versions the same or later than Spring Boot 2.7.0
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests((authz) -> authz
                        .antMatchers(HttpMethod.POST, "/login/**","/registration/**").permitAll()
                        .antMatchers("/users/**").hasAuthority("ROLE_ADMIN")
                        .antMatchers(HttpMethod.POST,"/api/goods/**").hasAuthority("ROLE_ADMIN")
                        .antMatchers(HttpMethod.DELETE,"/api/goods/**").hasAuthority("ROLE_ADMIN")
                        .antMatchers(HttpMethod.PUT,"/api/goods/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()

                )
                .addFilter(new CustomAuthenticationFilter(authenticationManager))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .headers().cacheControl();
              /*  .headers().frameOptions().sameOrigin()
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**","api/orders/**")
                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**","/api/orders/**").permitAll()
                .antMatchers(HttpMethod.POST,"/h2-console/**","/api/orders/**").permitAll()
                .anyRequest().permitAll();*/

        return http.build();
    }

}
