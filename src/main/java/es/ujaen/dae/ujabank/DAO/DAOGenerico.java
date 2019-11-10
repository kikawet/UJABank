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
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author flo00008
 * @param <T>
 */
@Repository
@Transactional
public abstract class DAOGenerico<T extends Serializable> {

    @PersistenceContext
    protected EntityManager em;

    private Class<T> _clase;

    public DAOGenerico() {
        _clase = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void insertar(T t) {
        em.persist(t);
    }    
    
    public T buscar(Object clave) {
        return em.find(this._clase, clave);
    }
    
    public T actualizar(T t) {
        return em.merge(t);
    }

    public void borrar(Object clave) {
        em.remove(em.find(this._clase, clave));
    }

    public void borrar(T t) {
        em.remove(em.merge(t));
    }

}
