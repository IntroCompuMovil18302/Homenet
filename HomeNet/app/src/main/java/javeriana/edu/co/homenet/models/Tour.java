package javeriana.edu.co.homenet.models;

import java.util.List;

public class Tour {
    private int capacidad;
    private String descripcion;
    private int duracion;
    private String fecha;
    private String hora;
    private String moneda;
    private int precio;
    private String titulo;
    private String urlImg;
    private List<Ubicacion> recorrido;

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
}
