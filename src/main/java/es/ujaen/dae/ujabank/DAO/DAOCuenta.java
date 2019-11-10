/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DAO;

import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Usuario;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author lopez
 */
@Repository
@Transactional
public class DAOCuenta extends DAOGenerico<Cuenta> {

//    @PersistenceContext
//    EntityManager em;
    //CRUD
    public Cuenta crear(float saldo, Usuario propietario) {
        Cuenta cuenta = new Cuenta(saldo, propietario);
        propietario = this.em.merge(propietario);
        propietario.addCuenta(cuenta);        
        this.em.persist(cuenta);
        return cuenta;
    }

//    
//    public Cuenta buscar(int id){
//        return em.find(Cuenta.class, id);
//    }
//    
//    public void actualizar(Cuenta cuenta){
//        em.merge(cuenta);
//    }
//    
//    public void borrar(int id){
//        Cuenta cuenta = em.find(Cuenta.class, id);
//        em.remove(cuenta);
//    }
}
