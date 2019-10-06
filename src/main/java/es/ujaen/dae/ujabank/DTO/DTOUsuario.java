/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.DTO;

import java.util.Date;

/**
 *
 * @author axpos
 */
public class DTOUsuario {

    private String _nombre;
    private String _dni;
    private String _domicilio;
    private Date _fNacimiento;
    private String _telefono;
    private String _email;

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

}
