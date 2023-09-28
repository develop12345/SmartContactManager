package com.project.smartcontactmanager.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig{

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new CustomUserDetailServices();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider dap() {
		DaoAuthenticationProvider d= new DaoAuthenticationProvider();
		d.setUserDetailsService(getUserDetailsService());
		d.setPasswordEncoder(passwordEncoder());
		return d;
	}
	
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		
		httpSecurity.authorizeRequests().requestMatchers("/user/**").hasRole("USER").requestMatchers("/**").permitAll().and().formLogin().loginPage("/login").loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").and().csrf().disable();
		return httpSecurity.build();
	}
}
