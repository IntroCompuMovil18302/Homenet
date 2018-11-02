package javeriana.edu.co.homenet.models;

public class Ubicacion {
    private long latitude;
    private long longitude;

    public  Ubicacion()
    {

    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
