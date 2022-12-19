package ru.mephi.tsis.bootlegamazon.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception
    {
        security.httpBasic().disable();
    }



//    @Bean
//    public PasswordEncoder encoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.authorizeRequests().antMatchers("/style/**").permitAll();
//
//        httpSecurity.authorizeRequests()
//                //Доступ только для не зарегистрированных пользователей
//                .antMatchers("/items/**").permitAll()
//                //.antMatchers("/items/**").not().fullyAuthenticated()
//                //Доступ только для пользователей с ролью Администратор и Менеджер
//                //.antMatchers("/category/**").hasAuthority("Администратор")
//                //.antMatchers("/category/**").hasAuthority("Менеджер")
//                //Доступ разрешен всем пользователей
//                //Все остальные страницы требуют аутентификации
//                .anyRequest().authenticated()
//                .and()
//                //Настройка для входа в систему
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
//
//    }



}


