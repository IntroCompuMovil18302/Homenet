package javeriana.edu.co.homenet.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Ubicacion  implements Serializable {

    private String id;
    private String descripcion;
    private double latitude;
    private double longitude;
    private String direccion;
    private String titulo;

    public  Ubicacion()
    {

    }

    public Ubicacion(double latitude, double longitude, String titulo) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.titulo = titulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
