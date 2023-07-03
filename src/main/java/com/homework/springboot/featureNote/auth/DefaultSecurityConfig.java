package com.homework.springboot.featureNote.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class DefaultSecurityConfig {

    private final CustomAuthenticationProvider authenticationProvider;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     Allows configuring a PortMapper that is available from getSharedObject(Class). Other provided SecurityConfigurer objects use this configured PortMapper as a default PortMapper when redirecting from HTTP to HTTPS or from HTTPS to HTTP (for example when used in combination with requiresChannel(). By default Spring Security uses a PortMapperImpl which maps the HTTP port 8080 to the HTTPS port 8443 and the HTTP port of 80 to the HTTPS port of 443.
     Example Configuration
     The following configuration will ensure that redirects within Spring Security from HTTP of a port of 9090 will redirect to HTTPS port of 9443 and the HTTP port of 80 to the HTTPS port of 443.
     @Configuration
     @EnableWebSecurity
     public class PortMapperSecurityConfig {

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
     .requiresChannel((requiresChannel) ->
     requiresChannel
     .anyRequest().requiresSecure()
     )
     .portMapper((portMapper) ->
     portMapper
     .http(9090).mapsTo(9443)
     .http(80).mapsTo(443)
     );
     return http.build();
     }

     @Bean
     public UserDetailsService userDetailsService() {
     UserDetails user = User.withDefaultPasswordEncoder()
     .username("user")
     .password("password")
     .roles("USER")
     .build();
     return new InMemoryUserDetailsManager(user);
     }
     }

     Params:
     portMapperCustomizer – the Customizer to provide more options for the PortMapperConfigurer
     Returns:
     the HttpSecurity for further customizations
     Throws:
     Exception –
     See Also:
     requiresChannel()
     * */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws  Exception {
       httpSecurity
               .authorizeHttpRequests()
               .anyRequest()
               .authenticated()
               .and()
               .httpBasic()
               .and()
               .formLogin(Customizer.withDefaults());
       return httpSecurity.build();
    }

    @Autowired
    public void injectCustomAuthProvider(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }
}
