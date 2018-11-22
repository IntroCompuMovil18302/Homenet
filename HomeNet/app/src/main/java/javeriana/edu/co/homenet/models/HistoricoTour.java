package javeriana.edu.co.homenet.models;

import java.util.HashMap;
import java.util.Map;

public class HistoricoTour {

    private String idGuia;
    private String fecha;
    private String hora;
    private String moneda;
    private int precio;
    private String tour;
    private Map<String,Boolean> usuarios = new HashMap<>();

    HistoricoTour(){

    }

    HistoricoTour(String idGuia, String fecha, String hora, String moneda, int precio, String tour, Map<String, Boolean> usuarios) {
        this.idGuia = idGuia;
        this.fecha = fecha;
        this.hora = hora;
        this.moneda = moneda;
        this.precio = precio;
        this.tour = tour;
        this.usuarios = usuarios;
    }

    public HistoricoTour(){}

    public String getIdGuia() {
        return idGuia;
    }

    public void setIdGuia(String idGuia) {
        this.idGuia = idGuia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getTour() {
        return tour;
    }

    public void setTour(String tour) {
        this.tour = tour;
    }

    public Map<String, Boolean> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Map<String, Boolean> usuarios) {
        this.usuarios = usuarios;
    }
}
