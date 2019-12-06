/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.validators;

import es.ujaen.dae.ujabank.DTO.DTOUsuario;
import es.ujaen.dae.ujabank.DTO.Mapper;
import es.ujaen.dae.ujabank.entidades.Usuario;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import es.ujaen.dae.ujabank.anotaciones.ValidarToken;

/**
 *
 * @author flo00008
 */
@Aspect
@Component
public class TokenManager implements ConstraintValidator<ValidarToken, String> {

    private static final Map<UUID, String> tokensActivos = new HashMap<>();

    @Override
    public void initialize(ValidarToken a) {
        ConstraintValidator.super.initialize(a); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        return t != null && tokensActivos.get(UUID.fromString(t)) != null;//Si lanza error al convertir tambien es false
    }

    private void limpiarToken(String id, UUID token) {
        tokensActivos.entrySet().removeIf((entry) -> {
            return id.equals(entry.getValue()) && !token.equals(entry.getKey());
        });
    }

    @AfterReturning(pointcut = "@annotation(es.ujaen.dae.ujabank.anotaciones.Login) and args(usuarioDTO)", returning = "responseEntity")
    public void addToken(DTOUsuario usuarioDTO, Object responseEntity) {
        Usuario usuario = Mapper.usuarioMapper(usuarioDTO);
        ResponseEntity re = (ResponseEntity) responseEntity;

        if (re.getStatusCode().equals(HttpStatus.OK)) {
            UUID token = (UUID) re.getBody();
            String id = usuario.getID();
            tokensActivos.put(token, id);
            limpiarToken(id, token);
        }
    }

    @AfterReturning(pointcut = "@annotation(es.ujaen.dae.ujabank.anotaciones.Logout) and args(token)", returning = "responseEntity")
    public void removeToken(String token, Object responseEntity) {
        ResponseEntity re = (ResponseEntity) responseEntity;

        if (re.getStatusCode().equals(HttpStatus.OK)) {
            UUID token2 = UUID.fromString(token);
            String dni = tokensActivos.remove(token2);
        }
    }

}
