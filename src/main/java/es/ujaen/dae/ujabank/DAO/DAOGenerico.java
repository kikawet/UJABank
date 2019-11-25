/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DAO;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author flo00008
 * @param <T>
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED)
public abstract class DAOGenerico<T extends Serializable> {

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> _clase;

    public DAOGenerico() {
        _clase = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void insertar(T t) {
        em.persist(t);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public T buscar(Object clave) {
        return em.find(this._clase, clave);
    }

    public T actualizar(T t) {
        return em.merge(t);
    }

    @Caching(evict = {
        @CacheEvict(value = "cacheCuentasUsuario", key = "#clave"),
        @CacheEvict(value = "cacheCuentas", key = "#clave")
    })
    public void borrar(Object clave) {
        em.remove(em.find(this._clase, clave));
    }

    @Caching(evict = {
        @CacheEvict(value = "cacheCuentasUsuario", key = "#t.getID()"),
        @CacheEvict(value = "cacheCuentas", key = "#t.getID()")
    })
    public void borrar(T t) {
        em.remove(em.merge(t));
    }

}
