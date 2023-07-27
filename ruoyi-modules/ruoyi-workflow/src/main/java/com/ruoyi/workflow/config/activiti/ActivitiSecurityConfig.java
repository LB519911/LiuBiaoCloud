package com.ruoyi.workflow.config.activiti;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

/**
 * Security框架配置
 */
@EnableWebSecurity
public class ActivitiSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 关闭csrf防护
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()

                //定制url访问权限
                .authorizeRequests()

                //无需登录即可访问
                .antMatchers("/**").permitAll()

                //需要特定权限
//                .antMatchers("/sysUser/**","/sysAuthority/**").hasAnyAuthority("ROLE_ADMIN","ROLE_SA")

                //其他接口登录才能访问
//                .anyRequest().authenticated()
                .and()
        ;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                return new User("admin", passwordEncoder().encode("123456"),
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"), new SimpleGrantedAuthority("GROUP_activitiTeam")));
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}