/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author axpos
 */
@Entity
public class Usuario implements Serializable {

    private String nombre;
    @Id
    private String dni;
    private String domicilio;    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fNacimiento;
    private String telefono;
    private String email;
    private String contrasena;
    
    @OneToMany(mappedBy = "propietario",cascade = {CascadeType.PERSIST,CascadeType.REMOVE })//,orphanRemoval = true)
    private final List<Cuenta> cuentas;

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.dni);
        return hash;
    }

    public Usuario() {
        this.cuentas = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        return Objects.equals(this.dni, other.dni);
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public Date getfNacimiento() {
        return fNacimiento;
    }

    public void setfNacimiento(Date fNacimiento) {
        this.fNacimiento = fNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean addCuenta(Cuenta c) {
        return this.cuentas.add(c);
    }

    public boolean removeCuenta(Cuenta c) {
        return this.cuentas.remove(c);
    }

//    public boolean containsCuenta(Cuenta c) {
//        return this.cuentas.contains(c);
//    }
}
