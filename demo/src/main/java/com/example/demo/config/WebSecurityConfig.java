package com.example.demo.config;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.JwtAuthenticationFilter;

import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;

import com.example.demo.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // http 시큐리티 빌더
    http.cors() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정.
        .and()
        .csrf()// csrf는 현재 사용하지 않으므로 disable
        .disable()
        .httpBasic()// token을 사용하므로 basic 인증 disable
        .disable()
        .sessionManagement()  // session 기반이 아님을 선언
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests() // /와 /auth/** 경로는 인증 안해도 됨.
        .antMatchers("/", "/auth/**").permitAll()
        .anyRequest() // /와 /auth/**이외의 모든 경로는 인증 해야됨.
        .authenticated();

    // filter 등록.
    // 매 요청마다
    // CorsFilter 실행한 후에
    // jwtAuthenticationFilter 실행한다.
    http.addFilterAfter(
        jwtAuthenticationFilter,
        CorsFilter.class
    );
  }
}
