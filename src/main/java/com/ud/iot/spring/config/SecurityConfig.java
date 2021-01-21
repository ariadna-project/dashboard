package com.ud.iot.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.ud.iot.spring.security.DashboardAuthenticationFailureHandler;
import com.ud.iot.spring.security.DashboardAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	private static final String ZUL_FILES = "/zkau/web/**/*.zul";
    private static final String[] ZK_RESOURCES = {
            "/zkau/web/**/js/**",
            "/zkau/web/**/zkmax/css/**",
            "/zkau/web/**/zul/css/**",
            "/zkau/web/**/font/**",
            "/zkau/web/**/img/**"
    };
    private static final String LOGIN_ERROR_URL = "/loginError";
    private static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";
    private static final String[] AUTH_PAGES = { "/login**", "/logout**", LOGIN_ERROR_URL + "**"};
    private static final String[] SECURED_PAGES = { "/dashboard" };
    
    @Autowired private DashboardAuthenticationProvider authProvider;
    @Autowired private DashboardAuthenticationFailureHandler dashboardAuthenticationFailureHandler;

    @Bean
    public String forwardFailureUrl() {
    	return LOGIN_ERROR_URL;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        
        http.authorizeRequests()
            .antMatchers(ZUL_FILES).denyAll() // block direct access to zul files
            .antMatchers(HttpMethod.GET, ZK_RESOURCES).permitAll() // allow zk resources
            .regexMatchers(HttpMethod.GET, REMOVE_DESKTOP_REGEX).permitAll() // allow desktop cleanup
            .requestMatchers(req -> "rmDesktop".equals(req.getParameter("cmd_0"))).permitAll() // allow desktop cleanup from ZATS
            .mvcMatchers(AUTH_PAGES).permitAll()
            .mvcMatchers(SECURED_PAGES).hasRole("ADMIN").anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .failureHandler(this.dashboardAuthenticationFailureHandler)
            .defaultSuccessUrl("/", true)
            .and()
            .logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }
	 
    @Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(this.authProvider);
	}
}
