/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.config;

import es.ujaen.dae.ujabank.restapi.RESTUsuario;
import es.ujaen.dae.ujabank.servicios.ServicioDatosUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author flo00008
 */
@Configuration
public class SeguridadUJABank extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private ServicioDatosUsuario sdu;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        //Ademas de todos los usuarios en la base de datos creamos dos administradores
        auth.inMemoryAuthentication()
                .withUser("flo").roles("ADMIN","DEFAULT").password("{noop}dae")//{noop} = Contraseña sin encriptar
                .and()
                .withUser("aep").roles("ADMIN","DEFAULT").password("{noop}dae");

          auth.userDetailsService(sdu)
                  .passwordEncoder(encoder);
    }
    
    @Override
    protected void configure(HttpSecurity httpsec) throws Exception{
        httpsec.csrf().disable();
        httpsec.httpBasic();
                
        httpsec.authorizeRequests().antMatchers("/**/test").permitAll();//pruebas 
        
        //Para RESTUsuario
        String path = RESTUsuario.URI_MAPPING;
        httpsec.authorizeRequests().antMatchers(HttpMethod.POST, path).hasRole("ADMIN");//crear usuarios
        httpsec.authorizeRequests().antMatchers(HttpMethod.DELETE, path+"/{id}").hasRole("ADMIN");//borrar usuarios
        httpsec.authorizeRequests().antMatchers(path+"/**").access("hasRole('ADMIN') or (hasRole('DEFAULT') and #id == principal.username)");//RESTCuenta se incluye aqui        
    }
    
    public String encode(String contrasena){                
        return encoder.encode(contrasena);
    }
    
    public boolean matches(String contrasenaRaw, String contrasenaEncriptada){
        return encoder.matches(contrasenaRaw, contrasenaEncriptada);
    }    
}
