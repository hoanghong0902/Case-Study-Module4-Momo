package com.codegym.demo.configuration;

import com.codegym.demo.constraint.ERole;
import com.codegym.demo.security.JwtAuthenticationEntryPoint;
import com.codegym.demo.security.JwtRequestFilter;
import com.codegym.demo.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests().antMatchers("/login", "/api/login", "/api/register").permitAll().


				// all other requests need to be authenticated
				antMatchers(HttpMethod.GET, "/admin").hasAnyRole(ERole.ADMIN.toString()).
//				antMatchers(HttpMethod.GET, "/home").hasAnyRole(ERole.USER.toString(),ERole.ADMIN.toString()).
				antMatchers(HttpMethod.GET, "/api/profile").hasAnyRole(ERole.USER.toString(),ERole.ADMIN.toString()).
				antMatchers(HttpMethod.PUT, "/api/edit").hasAnyRole(ERole.USER.toString(),ERole.ADMIN.toString()).
				antMatchers(HttpMethod.DELETE, "/api/delete").hasAnyRole(ERole.ADMIN.toString()).
				antMatchers(HttpMethod.POST, "/api/transferMoneyToMomo").hasAnyRole(ERole.USER.toString(),ERole.ADMIN.toString()).
				antMatchers(HttpMethod.POST, "/api/transferMoneyToBank").hasAnyRole(ERole.USER.toString(),ERole.ADMIN.toString()).
				antMatchers(HttpMethod.GET, "/api/admin/getAll").hasAnyRole(ERole.ADMIN.toString()).
				antMatchers(HttpMethod.GET, "/api/admin/getAllUsers").hasAnyRole(ERole.ADMIN.toString()).
				antMatchers(HttpMethod.GET, "/api/admin/viewUserInfo/**").hasAnyRole(ERole.ADMIN.toString()).
				antMatchers(HttpMethod.PUT, "/api/admin/edit/**").hasAnyRole(ERole.ADMIN.toString()).
				antMatchers(HttpMethod.DELETE, "/api/admin/delete/**").hasAnyRole(ERole.ADMIN.toString()).

				anyRequest().permitAll().and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
