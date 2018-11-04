package javeriana.edu.co.homenet.models;

public class Ubicacion {
    private double latitude;
    private double longitude;

    public  Ubicacion()
    {

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
