package javeriana.edu.co.homenet.models;

import java.util.List;

public class Alojamiento {
    private List<String> urlImg;
    private long precio;
    private String tipo;

    // relaciones
    private String anfitrion;

    public Alojamiento()
    {

    }

    public List<String> getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(List<String> urlImg) {
        this.urlImg = urlImg;
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
