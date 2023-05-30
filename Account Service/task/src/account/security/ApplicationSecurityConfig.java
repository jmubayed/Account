package account.security;

//import account.component.CustomAccessDenied;
//import account.component.CustomAuthenticationFailure;
//import account.component.EntryPointTwo;
//import account.component.entryPoint;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoderConfig encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder.passwordEncoder());
    }

//    @Autowired
//    private CustomAccessDenied customAccessDenied;

//    @Autowired
//    private CustomAuthenticationFailure customAuthenticationFailure;

    @Autowired
    private LoginAttemptService loginAttemptService;

//    @Autowired
//    private entryPoint entryPoint;
//
//    @Autowired
//    private EntryPointTwo entryPointTwo;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .mvcMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyRole("USER", "ADMINISTRATOR", "ACCOUNTANT")
                .mvcMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyRole("USER", "ACCOUNTANT")
                .mvcMatchers(HttpMethod.POST, "/api/acct/payments").hasRole("ACCOUNTANT")
                .mvcMatchers(HttpMethod.PUT, "/api/acct/payments").hasRole("ACCOUNTANT")
                .mvcMatchers(HttpMethod.PUT, "/api/admin/user/role").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.GET, "/api/admin/user/").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.PUT, "/api/admin/user/access").hasRole("ADMINISTRATOR")
                .mvcMatchers(HttpMethod.GET, "/api/security/events/").hasRole("AUDITOR")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(endpointAuthenticationEntryPoint())
////                .accessDeniedHandler(customAccessDenied)
//                .authenticationEntryPoint(entryPointTwo)
//                .and().formLogin().failureHandler(entryPoint)
//                .authenticationEntryPoint(entryPointTwo)
//                .and().formLogin().failureHandler(entryPoint)
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public AuthenticationEntryPoint endpointAuthenticationEntryPoint() {
        System.out.println("-----------------------------------");

        return (request, response, authException) ->  response.sendError(HttpStatus.UNAUTHORIZED.value(), "se jodio");
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .httpBasic()
//                .and()
//                .authorizeHttpRequests()
//                .antMatchers(HttpMethod.POST, "/api/auth/signup").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/auth/changepass").hasAnyRole("USER", "ADMINISTRATOR", "ACCOUNTANT")
//                .antMatchers(HttpMethod.GET, "/api/empl/payment").hasAnyRole("USER", "ACCOUNTANT")
//                .antMatchers(HttpMethod.POST, "/api/acct/payments").hasRole("ACCOUNTANT")
//                .antMatchers(HttpMethod.PUT, "/api/acct/payments").hasRole("ACCOUNTANT")
//                .antMatchers(HttpMethod.PUT, "/api/admin/user/role").hasRole("ADMINISTRATOR")
//                .antMatchers(HttpMethod.DELETE, "/api/admin/user/**").hasRole("ADMINISTRATOR")
//                .antMatchers(HttpMethod.GET, "/api/admin/user/").hasRole("ADMINISTRATOR")
//                .antMatchers(HttpMethod.PUT, "/api/admin/user/access").hasRole("ADMINISTRATOR")
//                .antMatchers(HttpMethod.GET, "/api/security/events/").hasRole("AUDITOR")
//                .anyRequest().permitAll()
////                .and()
////                .exceptionHandling()
//////                .accessDeniedHandler(customAccessDenied)
////                .authenticationEntryPoint(entryPointTwo)
////                .and().formLogin().failureHandler(entryPoint)
////                .authenticationEntryPoint(entryPointTwo)
////                .and().formLogin().failureHandler(entryPoint)
//                .and()
//                .csrf().disable()
//                .headers().frameOptions().disable()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        return http.build();
//    }
}