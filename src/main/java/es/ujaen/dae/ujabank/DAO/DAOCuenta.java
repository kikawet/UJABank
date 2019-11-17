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
import java.util.Date;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lopez
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class DAOCuenta extends DAOGenerico<Cuenta> {

    @CacheEvict(value = "cacheCuentasUsuario", key = "#propietario.getID()")
    public Cuenta crear(float saldo, Usuario propietario) {
        Cuenta cuenta = new Cuenta(saldo, propietario);
        propietario = this.em.merge(propietario);
        propietario.addCuenta(cuenta);
        this.em.persist(cuenta);
        return cuenta;
    }

    @Cacheable(value = "cacheCuentas", key = "#id")//Tengo que sobreescrubir este metodo para poder cachear solo cuentas
    public Cuenta buscar(int id) {
        return super.buscar(id);
    }

    @Caching(evict = {
        @CacheEvict(value = "cacheCuentasUsuario", key = "#destino.getPropietario().getID()"),
        @CacheEvict(value = "cacheCuentas", key = "#destino.getID()")
    })
    public Ingreso ingresar(Tarjeta origen, Cuenta destino, float cantidad) {
        destino = this.actualizar(destino);
        Ingreso ingreso = new Ingreso();
        ingreso.setFecha(new Date());
        ingreso.setCantidad(cantidad);
        ingreso.setOrigen(origen.getNumero());
        ingreso.setIDDestino(destino.getID());

        boolean ingresar = destino.ingresar(ingreso);
        return (ingresar) ? (ingreso) : (null);

    }

    @Caching(evict = {
        //Caches del origen
        @CacheEvict(value = "cacheCuentasUsuario", key = "#origen.getPropietario().getID()"),
        @CacheEvict(value = "cacheCuentas", key = "#origen.getID()"),
        //Caches del destino
        @CacheEvict(value = "cacheCuentasUsuario", key = "#destino.getPropietario().getID()"),
        @CacheEvict(value = "cacheCuentas", key = "#destino.getID()")
    })
    public Transferencia transferir(Cuenta origen, Cuenta destino, float cantidad, String concepto) {

        origen = this.actualizar(origen);
        destino = this.actualizar(destino);

        Transferencia transferencia = new Transferencia();

        transferencia.setFecha(new Date());
        transferencia.setIDOrigen(origen.getID());
        transferencia.setIDDestino(destino.getID());
        transferencia.setCantidad(cantidad);
        transferencia.setConcepto(concepto);
        this.em.persist(transferencia);
        this.em.flush();

        boolean transferir = origen.transferir(transferencia) && destino.transferir(transferencia);

        return (transferir) ? (transferencia) : (null);
    }

    @Caching(evict = {
        @CacheEvict(value = "cacheCuentasUsuario", key = "#origen.getPropietario().getID()"),
        @CacheEvict(value = "cacheCuentas", key = "#origen.getID()")
    })
    public Retiro retirar(Cuenta origen, Tarjeta destino, float cantidad) {

        origen = this.actualizar(origen);

        Retiro retiro = new Retiro();

        retiro.setFecha(new Date());
        retiro.setIDOrigen(origen.getID());
        retiro.setDestino(destino.getNumero());
        retiro.setCantidad(cantidad);

        boolean retirar = origen.retirar(retiro);
        return (retirar) ? (retiro) : (null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Transaccion> consultarTransacciones(Cuenta cuenta, Date inicio, Date fin) {

        cuenta = this.buscar(cuenta.getID());
        List<Transaccion> transaccion = em.createQuery("SELECT t FROM Cuenta c JOIN c.historial t WHERE c.id = :id AND t.fecha BETWEEN :inicio and :fin", Transaccion.class)
                .setParameter("id", cuenta.getID())
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();
        return transaccion;
    }
}
