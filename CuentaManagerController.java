/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.caduceo.cons.controllers;

import com.caduceo.cons.controllers.util.JsfUtil;
import com.caduceo.cons.entidades.Departamento;
import com.caduceo.cons.entidades.EntidadJuridica;
import com.caduceo.cons.entidades.EntidadJuridicaMinisterio;
import com.caduceo.cons.entidades.GeoCiudadMunicipio;
import com.caduceo.cons.entidades.GeoDistritoSector;
import com.caduceo.cons.entidades.GeoMunicipioDistrito;
import com.caduceo.cons.entidades.GeoPais;
import com.caduceo.cons.entidades.GeoPaisCiudad;
import com.caduceo.cons.entidades.Ministerio;
import com.caduceo.cons.entidades.TipoEntidadJuridica;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Julio_Cortorreal
 */
@ManagedBean
@Stateless
public class CuentaManagerController {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    private String Nombre;
    private String Direccion;
    private String Telefono1;
    private String Telefono2;
    private String Email;
    private String Password;
    private String Password2;
    private TipoEntidadJuridica tipoEJ;
    private GeoPais pais;
    private GeoPaisCiudad ciudad;
    private GeoCiudadMunicipio Municipio;
    private GeoMunicipioDistrito Distrito;
    private GeoDistritoSector Sector;

//    private EntidadJuridica wuser;
    /*
     @PostConstruct
     public void init() {
     wuser = new EntidadJuridica();

     }
     */
    /**
     * Creates a new instance of CuentaManagerController
     */
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getTelefono1() {
        return Telefono1;
    }

    public void setTelefono1(String Telefono1) {
        this.Telefono1 = Telefono1;
    }

    public String getTelefono2() {
        return Telefono2;
    }

    public void setTelefono2(String Telefono2) {
        this.Telefono2 = Telefono2;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    // Confirmacion de password
    public String getPassword2() {
        return Password2;
    }

    public void setPassword2(String Password2) {
        this.Password2 = Password2;
    }

    public TipoEntidadJuridica getTipoEJ() {
        return tipoEJ;
    }

    public void setTipoEJ(TipoEntidadJuridica tipoEJ) {
        this.tipoEJ = tipoEJ;
    }

    public GeoPais getPais() {
        return pais;
    }

    public void setPais(GeoPais pais) {
        this.pais = pais;
    }

    public GeoPaisCiudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(GeoPaisCiudad ciudad) {
        this.ciudad = ciudad;
    }

    public GeoCiudadMunicipio getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(GeoCiudadMunicipio Municipio) {
        this.Municipio = Municipio;
    }

    public GeoMunicipioDistrito getDistrito() {
        return Distrito;
    }

    public void setDistrito(GeoMunicipioDistrito Distrito) {
        this.Distrito = Distrito;
    }

    public GeoDistritoSector getSector() {
        return Sector;
    }

    public void setSector(GeoDistritoSector Sector) {
        this.Sector = Sector;
    }

    public String cancelar() {

        return "principal";

    }

    public String createUser() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException, NoSuchAlgorithmException {
        FacesContext context = FacesContext.getCurrentInstance();
        EntidadJuridica wuser;
        Departamento departamento;
        EntidadJuridicaMinisterio EJMinisterio;
        if ((getUserEJemail() == null) && (getUserEJnombre() == null)) {
            if (!Password.equals(Password2)) {
                FacesMessage message = new FacesMessage("La contraseña no coinciden, favor de Digitarla correctamente");
                context.addMessage(null, message);
                return null;
            }
            this.tipoEJ = getBtipoEJConsultorio();
            
            wuser = new EntidadJuridica(tipoEJ, Nombre, pais, ciudad, Municipio, Distrito, Sector, Direccion, Telefono1, Telefono2, Email, Password);
        
            
            
            try {
                utx.begin();
                em.persist(wuser);
                utx.commit();
                               try{
                                   wuser = getUserEJnombre();
                                 departamento = new Departamento(wuser, "Administracion", this.Direccion);
                                  utx.begin();
                                  em.persist(departamento);
                                  utx.commit();
                                }catch(ConstraintViolationException e){

                                 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                          "Error al crear el consultorio!",
                                          "Inesperado Error Durante la creacion del departamento. Favor de ponerse en contacto con el Fabricante");
                                  context.addMessage(null, message);
                                  Logger.getAnonymousLogger().log(Level.SEVERE,
                                          " ",
                                          "");
                                  
                                }
                                try{
                                    wuser = getUserEJnombre();
                                    EJMinisterio = new EntidadJuridicaMinisterio(getBMinisterio(), wuser );
                                    utx.begin();
                                    em.persist(EJMinisterio);
                                    utx.commit();
               
                                }catch(ConstraintViolationException e){

                                 FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                          "Error al crear el consultorio!",
                                          "Inesperado Error Durante la creacion del departamento. Favor de ponerse en contacto con el Fabricante");
                                  context.addMessage(null, message);
                                  Logger.getAnonymousLogger().log(Level.SEVERE,
                                          " ",
                                          "");
                                  return null;
                                }
             
                

                return "login";
            } catch (ConstraintViolationException e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error al crear el consultorio!",
                        "Inesperado Error Durante la creacion del consultorio. Favor de ponerse en contacto con el Fabricante");
                context.addMessage(null, message);
                Logger.getAnonymousLogger().log(Level.SEVERE,
                        " ",
                        "");
                return null;
            }
        } else if ((getUserEJemail() == null) || (getUserEJnombre() != null)) {

            JsfUtil.addErrorMessage("Nombre ya Existe en el sistema, favor de cambiar el nombre : " + Nombre);

            return null;

        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "El Email ya Existe Favor de intentar con otro Email '"
                    + Email
                    + "' already exists!  ",
                    "Please choose a different username.");
            context.addMessage(null, message);
            return null;
        }
    }

    /**
     * <p>
     * When invoked, it will invalidate the user's session and move them to the
     * login view.</p>
     *
     * @return <code>login</code>
     */
    public String logout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "login";

    }

    private EntidadJuridica getUserEJemail() {
        try {
            EntidadJuridica user = (EntidadJuridica) em.createNamedQuery("EntidadJuridica.findByEmailEntidadpublica").
                    setParameter("emailEntidadpublica", Email).getSingleResult();
            return user;
        } catch (NoResultException nre) {
            return null;
        }
    }

    
   
    private EntidadJuridica getUserEJnombre() {
        try {
            EntidadJuridica user = (EntidadJuridica) em.createNamedQuery("EntidadJuridica.findByNombreEntidadPublica").
                    setParameter("nombreEntidadPublica", Nombre).getSingleResult();
            return user;
        } catch (NoResultException nre) {
            return null;
        }
    }

    private TipoEntidadJuridica getBtipoEJConsultorio() {
        try {
            TipoEntidadJuridica tipo = (TipoEntidadJuridica) em.createNamedQuery("TipoEntidadJuridica.findByTEJId").
                    setParameter("tEJId", 6).getSingleResult();
            return tipo;
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    
      private Ministerio getBMinisterio() {
        try {
            // @NamedQuery(name = "Ministerio.findByIdMinisterio", query = "SELECT m FROM Ministerio m WHERE m.idMinisterio = :idMinisterio")
            Ministerio  ministerio = (Ministerio) em.createNamedQuery("Ministerio.findByIdMinisterio").
                    setParameter("idMinisterio", 3).getSingleResult();
            return ministerio;
        } catch (NoResultException nre) {
            return null;
        }
    }

}
