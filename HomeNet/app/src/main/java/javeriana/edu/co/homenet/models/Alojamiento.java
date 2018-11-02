package javeriana.edu.co.homenet.models;

import java.util.List;

public class Alojamiento {
    private String id;
    private List<String> urlImgs;
    private long precio;
    private String tipo;
    private String descripcion;

    // relaciones
    private String anfitrion;
    private List<Disponibilidad> disponibilidades;
    private Ubicacion ubicacion;

    public Alojamiento()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<String> getUrlImgs() {
        return urlImgs;
    }

    public void setUrlImgs(List<String> urlImgs) {
        this.urlImgs = urlImgs;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }

    public void setDisponibilidades(List<Disponibilidad> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }

    public String getAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(String anfitrion) {
        this.anfitrion = anfitrion;
    }

    public List<String> getUrlImg() {
        return urlImgs;
    }

    public void setUrlImg(List<String> urlImg) {
        this.urlImgs = urlImg;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(long precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}