package pranasabda.id.cuacarajawali.model;

/**
 * Created by prana on 05/03/18.
 */

public class Cuaca {
    private String dt;
    private String temp;
    private String weather;
    private String humidity;

    public Cuaca(String dt, String temp, String weather, String humidity) {
        this.dt = dt;
        this.temp = temp;
        this.weather = weather;
        this.humidity = humidity;

    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getHumadity() {
        return humidity;
    }

    public void setHumadity(String humadity) {
        this.humidity = humadity;
    }
}
