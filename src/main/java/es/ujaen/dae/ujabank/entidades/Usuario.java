/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author axpos
 */
public class Usuario {

    private String _nombre;
    private String _dni;
    private String _domicilio;
    private Date _fNacimiento;
    private String _telefono;
    private String _email;
    private String _contrasena;
    private List<Cuenta> _cuentas;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this._dni);
        return hash;
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
        return Objects.equals(this._dni, other._dni);
    }

    public String getContrasena() {
        return _contrasena;
    }

    public void setContrasena(String contrasena) {
        this._contrasena = contrasena;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String nombre) {
        this._nombre = nombre;
    }

    public String getDni() {
        return _dni;
    }

    public void setDni(String dni) {
        this._dni = dni;
    }

    public String getDomicilio() {
        return _domicilio;
    }

    public void setDomicilio(String domicilio) {
        this._domicilio = domicilio;
    }

    public Date getfNacimiento() {
        return _fNacimiento;
    }

    public void setfNacimiento(Date fNacimiento) {
        this._fNacimiento = fNacimiento;
    }

    public String getTelefono() {
        return _telefono;
    }

    public void setTelefono(String telefono) {
        this._telefono = telefono;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    public boolean addCuenta(Cuenta c) {
        return this._cuentas.add(c);
    }

    public boolean removeCuenta(Cuenta c) {
        return this._cuentas.remove(c);
    }

    public boolean containsCuenta(Cuenta c) {
        return this._cuentas.contains(c);
    }
}
