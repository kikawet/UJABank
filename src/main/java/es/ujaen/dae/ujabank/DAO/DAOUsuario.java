/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DAO;

import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author flo00008
 */
@Repository
@Transactional(propagation = Propagation.SUPPORTS,readOnly = true)
public class DAOUsuario extends DAOGenerico<Usuario> {

//    @PersistenceContext
//    EntityManager em;
    public boolean contiene(Usuario usuario) {//Como los usuarios solo se consultan una vez y luego se almacenan en memoria no hago cache
        return em.contains(usuario);
    }

    /**
     * @param usuario
     * @brief actualiza t con los de la base de datos
     * @param t
     * @return
     */
//    public Usuario get(Usuario t) {
//        em.refresh(t);
//        return t;
//    }
//    public boolean propietario(Usuario usuario , Cuenta cuenta){
//        usuario.containsCuenta(cuenta);
//        return false;
//    }
    @Cacheable(value = "cacheCuentasUsuario",key = "#usuario.getID()")
    public List<Cuenta> getCuentas(Usuario usuario) {
        usuario = this.buscar(usuario.getID());
//        List<Cuenta> cuentas = new ArrayList<>();
        usuario.getCuentas().forEach((Cuenta cuenta) -> {
//            cuentas.add((cuenta));
        });
        return usuario.getCuentas();
    }

}
