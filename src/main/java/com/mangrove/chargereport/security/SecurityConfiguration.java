package com.mangrove.chargereport.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/wh/**").hasRole("ADMIN")
                .antMatchers("/drayrpt/**").hasRole("USER")
                .antMatchers("/op/**").hasRole("CSOP")
                .anyRequest().authenticated()
                .and()
                .formLogin();

        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("surpath").password("{noop}quote@surpath").roles("ADMIN", "USER","CSOP")
                .and()
                .withUser("JamN").password("{noop}123456").roles("USER")
                .and()
                .withUser("GCE").password("{noop}123456").roles("USER")
                .and()
                .withUser("CCT").password("{noop}123456").roles("USER")
                .and()
                .withUser("UCT").password("{noop}123456").roles("USER")
                .and()
                .withUser("SeaSky").password("{noop}dray@seasky").roles("USER")
                .and()
                .withUser("RJR").password("{noop}123456").roles("USER")
                .and()
                .withUser("Galaxy").password("{noop}123456").roles("USER")
                .and()
                .withUser("Canatlas").password("{noop}123456").roles("USER")
                .and()
                .withUser("csop").password("{noop}dray@csop").roles("USER","CSOP");
    }

    /*@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login.html");
        // 如果需要排除其他静态资源, 参考如下:
        web.ignoring().antMatchers("/login.html", "/static/**");
    }*/
}
