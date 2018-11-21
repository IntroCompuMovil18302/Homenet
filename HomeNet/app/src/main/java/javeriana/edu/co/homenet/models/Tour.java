package javeriana.edu.co.homenet.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javeriana.edu.co.homenet.utils.DistanceFunc;

public class Tour {

    private String id;
    private int capacidad;
    private String descripcion;
    private int duracion;
    private String fecha;
    private String idGuia;
    private String hora;
    private String moneda;
    private int precio;
    private String titulo;
    private String urlImg;
    private List<Ubicacion> recorrido = new ArrayList<>();
    private Map<String, Boolean> personas = new HashMap<>();

    public Tour(int capacidad, String descripcion, int duracion, String fecha, String hora,
                String moneda, int precio, String titulo, String urlImg, List<Ubicacion> recorrido) {
        this.capacidad = capacidad;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.fecha = fecha;
        this.hora = hora;
        this.moneda = moneda;
        this.precio = precio;
        this.titulo = titulo;
        this.urlImg = urlImg;
        this.recorrido = recorrido;
    }

    public Tour () {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdGuia() {
        return idGuia;
    }

    public void setIdGuia(String idGuia) {
        this.idGuia = idGuia;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public List<Ubicacion> getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(List<Ubicacion> recorrido) {
        this.recorrido = recorrido;
    }

    public Map<String, Boolean> getPersonas() {
        return personas;
    }

    public void setPersonas(Map<String, Boolean> personas) {
        this.personas = personas;
    }

    public void addPersona (String id, Boolean b) {
        this.personas.put(id,b);
    }

    public boolean estaCerca(double lat1, double long1, double km){
        Ubicacion ubicacion = this.getRecorrido().get(0);
        if(ubicacion!=null){
            double distance = DistanceFunc.distance(lat1,long1,ubicacion.getLatitude(),ubicacion.getLongitude());
            if(distance<=km){
                return true;
            }
            return false;
        }
        return  false;
    }
}
