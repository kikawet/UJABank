/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DAO;

import es.ujaen.dae.ujabank.DTO.Tarjeta;
import es.ujaen.dae.ujabank.entidades.Cuenta;
import es.ujaen.dae.ujabank.entidades.Ingreso;
import es.ujaen.dae.ujabank.entidades.Retiro;
import es.ujaen.dae.ujabank.entidades.Transaccion;
import es.ujaen.dae.ujabank.entidades.Transferencia;
import es.ujaen.dae.ujabank.entidades.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.hibernate.sql.Select;
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
    public Ingreso ingresar(Tarjeta origen, Cuenta destino, float cantidad) {
        destino = this.actualizar(destino);
        Ingreso ingreso = new Ingreso();
        ingreso.setFecha(new Date());
        ingreso.setCantidad(cantidad);
        ingreso.setOrigen(origen.getNumero());
        ingreso.setIDDestino(destino.getId());
        boolean ingresar = destino.ingresar(ingreso);
        return (ingresar) ? (ingreso) : (null);

    }

    public Transferencia transferir(Cuenta origen, Cuenta destino, float cantidad, String concepto) {
        origen = this.actualizar(origen);

        destino = this.actualizar(destino);

        Transferencia transferencia = new Transferencia();

        transferencia.setFecha(new Date());
        transferencia.setIDOrigen(origen.getId());
        transferencia.setIDDestino(destino.getId());
        transferencia.setCantidad(cantidad);
        transferencia.setConcepto(concepto);
        boolean transferir = origen.transferir(transferencia) && destino.transferir(transferencia); // hacer dos pasos si hace falta deshacer
        return (transferir) ? (transferencia) : (null);
    }

    public Retiro retirar(Cuenta origen, Tarjeta destino, float cantidad) {

        origen = this.actualizar(origen);

        Retiro retiro = new Retiro();

        retiro.setFecha(new Date());
        retiro.setIDOrigen(origen.getId());
        retiro.setDestino(destino.getNumero());
        retiro.setCantidad(cantidad);

        boolean retirar = origen.retirar(retiro);
        return (retirar) ? (retiro) : (null);
    }

    public List<Transaccion> consultarTransacciones(Cuenta cuenta, Date inicio, Date fin) {

        cuenta = this.actualizar(cuenta);
        List<Transaccion> transaccion = em.createQuery("SELECT t FROM Cuenta c JOIN c.historial t WHERE c.id = :id AND t.fecha BETWEEN :inicio and :fin", Transaccion.class)
                .setParameter("id", cuenta.getId())
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();
        return transaccion;
    }
}
