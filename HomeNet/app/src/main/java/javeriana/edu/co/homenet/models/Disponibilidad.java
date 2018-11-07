package javeriana.edu.co.homenet.models;

import java.io.Serializable;

public class Disponibilidad implements Serializable {
    private String fechaInicio;
    private String fechaFin;

    public Disponibilidad() {

    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
