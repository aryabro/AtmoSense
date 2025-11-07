package edu.uiuc.cs427app;

import com.google.gson.annotations.SerializedName;

public class WeatherData {
    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private Weather[] weather;

    @SerializedName("wind")
    private Wind wind;

    public Main getMain() {
        return main;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public static class Main {
        @SerializedName("temp")
        private double temp;

        @SerializedName("humidity")
        private int humidity;

        public double getTemp() {
            return temp;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static class Weather {
        @SerializedName("main")
        private String main;

        @SerializedName("description")
        private String description;

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class Wind {
        @SerializedName("speed")
        private double speed;

        @SerializedName("deg")
        private double deg;

        public double getSpeed() {
            return speed;
        }

        public double getDeg() {
            return deg;
        }
    }
}
