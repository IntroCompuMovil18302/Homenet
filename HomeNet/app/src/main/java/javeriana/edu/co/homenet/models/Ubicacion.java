package javeriana.edu.co.homenet.models;

import java.io.Serializable;

public class Ubicacion  implements Serializable {
    private double latitude;
    private double longitude;
    private String direccion;

    public  Ubicacion()
    {

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
