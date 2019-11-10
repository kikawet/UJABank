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
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author flo00008
 */
@Repository
@Transactional
public class DAOUsuario extends DAOGenerico<Usuario> {

//    @PersistenceContext
//    EntityManager em;
    public boolean contiene(Usuario usuario) {
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
    public List<Cuenta> getCuentas(Usuario usuario) {
        usuario = this.em.merge(usuario);
//        List<Cuenta> cuentas = new ArrayList<>();
        usuario.getCuentas().forEach((cuenta) -> {
//            cuentas.add((cuenta));
        });
        return usuario.getCuentas();
    }

}
