/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computacionfim.conversorhorarios;

import java.util.ArrayList;

/**
 *
 * @author oscar
 */
public class Materia {
    //CamposLlenados
    private String numeroPE;
    private String nombrePE;
    private String numeroControl;
    private String claveUA;
    private String planEstudios;
    private String nombreUA;
    private String grupo;
    private String subGrupo;
    private String numeroEmpleado;
    private String nombreEmpleado;
    private String tipo;
    private String salon;
    private String capacidad;
    private String edificio;
    private String horas;
    private String categoria;
    private String lunes;
    private String martes;
    private String miercoles;
    private String jueves;
    private String viernes;
    private String sabado;
    private String domingo;
    private String extra;

    //variables control
    private int celdasLlenadas;
    private ArrayList<String> celdas;

    public Materia() {
        celdas = new ArrayList<>();//Guardamos 
        celdasLlenadas = 0;
    }

    public Materia(String numeroPE, String nombrePE, String numeroControl, String claveUA, String planEstudios, String nombreUA, String grupo, String subGrupo, String numeroEmpleado, String nombreEmpleado, String tipo, String salon, String capacidad, String edificio, String horas, String categoria, String lunes, String martes, String miercoles, String jueves, String viernes, String sabado, String domingo, String extra, int celdasLlenadas, ArrayList<String> celdas) {
        this.numeroPE = numeroPE;
        this.nombrePE = nombrePE;
        this.numeroControl = numeroControl;
        this.claveUA = claveUA;
        this.planEstudios = planEstudios;
        this.nombreUA = nombreUA;
        this.grupo = grupo;
        this.subGrupo = subGrupo;
        this.numeroEmpleado = numeroEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.tipo = tipo;
        this.salon = salon;
        this.capacidad = capacidad;
        this.edificio = edificio;
        this.horas = horas;
        this.categoria = categoria;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.domingo = domingo;
        this.extra = extra;
    }

    public void llenarCelda(String info) {

        if (celdasLlenadas < 24) {
            celdas.add(info);
            celdasLlenadas++;
        } else {
            System.out.println("Celdas Overflow");
        }
    }

    public void llenarMateria() {
        this.numeroPE = celdas.get(0);
        this.nombrePE = celdas.get(1);
        this.numeroControl = celdas.get(2);
        this.claveUA = celdas.get(3);
        this.planEstudios = celdas.get(4);
        this.nombreUA = celdas.get(5);
        this.grupo = celdas.get(6);
        this.subGrupo = celdas.get(7);
        this.numeroEmpleado = celdas.get(8);
        this.nombreEmpleado = celdas.get(9);
        this.tipo = celdas.get(10);
        this.salon = celdas.get(11);
        this.capacidad = celdas.get(12);
        this.edificio = celdas.get(13);
        this.horas = celdas.get(14);
        this.categoria = celdas.get(15);
        this.lunes = celdas.get(16);
        this.martes = celdas.get(17);
        this.miercoles = celdas.get(18);
        this.jueves = celdas.get(19);
        this.viernes = celdas.get(20);
        this.sabado = celdas.get(21);
        this.domingo = celdas.get(22);
        this.extra = celdas.get(23);
        celdas.clear();
        celdasLlenadas = 0;
    }

    @Override
    public String toString() {
        return "Materia{" + "numeroPE=" + numeroPE + ", nombrePE=" + nombrePE + ", numeroControl=" + numeroControl + ", claveUA=" + claveUA + ", planEstudios=" + planEstudios + ", nombreUA=" + nombreUA + ", grupo=" + grupo + ", subGrupo=" + subGrupo + ", numeroEmpleado=" + numeroEmpleado + ", nombreEmpleado=" + nombreEmpleado + ", tipo=" + tipo + ", salon=" + salon + ", capacidad=" + capacidad + ", edificio=" + edificio + ", horas=" + horas + ", categoria=" + categoria + ", lunes=" + lunes + ", martes=" + martes + ", miercoles=" + miercoles + ", jueves=" + jueves + ", viernes=" + viernes + ", sabado=" + sabado + ", domingo=" + domingo + ", extra=" + extra + '}';
    }
    

    public String getNumeroPE() {
        return numeroPE;
    }

    public String getNombrePE() {
        return nombrePE;
    }

    public String getNumeroControl() {
        return numeroControl;
    }

    public String getClaveUA() {
        return claveUA;
    }

    public String getGrupo() {
        return grupo;
    }

    public String getSubGrupo() {
        return subGrupo;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public String getTipo() {
        return tipo;
    }

    public String getSalon() {
        return salon;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public String getEdificio() {
        return edificio;
    }

    public String getHoras() {
        return horas;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getLunes() {
        return lunes;
    }

    public String getMartes() {
        return martes;
    }

    public String getMiercoles() {
        return miercoles;
    }

    public String getJueves() {
        return jueves;
    }

    public String getViernes() {
        return viernes;
    }

    public String getSabado() {
        return sabado;
    }

    public String getDomingo() {
        return domingo;
    }

    public String getPlanEstudios() {
        return planEstudios;
    }

    public String getNombreUA() {
        return nombreUA;
    }

    public String getExtra() {
        return extra;
    }

    public int getCeldasLlenadas() {
        return celdasLlenadas;
    }

    public ArrayList<String> getCeldas() {
        return celdas;
    }

}
