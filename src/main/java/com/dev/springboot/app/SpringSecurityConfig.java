package com.dev.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//TODO Actualizar implementacion de Spring Security, Esta forma esta obsoleta
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
			.antMatchers("/ver/**").hasAnyRole("USER")
			.antMatchers("/uploads/**").hasAnyRole("USER")
			.antMatchers("/form/**").hasAnyRole("ADMIN")
			.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
			.antMatchers("/factura/**").hasAnyRole("ADMIN")
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.and()
			.logout().permitAll();
	}

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		PasswordEncoder encoder = passwordEncoder();
		UserBuilder users =  User.builder().passwordEncoder(encoder::encode);
		
		builder.inMemoryAuthentication()
			.withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
			.withUser(users.username("user").password("12345").roles("USER"));
	}
}
