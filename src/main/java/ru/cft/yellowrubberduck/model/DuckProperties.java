package ru.cft.yellowrubberduck.model;

public class DuckProperties extends MaterialProperties {
    public String color = "yellow";
    public Double metersHeight = 3.0;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getMetersHeight() {
        return metersHeight;
    }

    public void setMetersHeight(Double metersHeight) {
        this.metersHeight = metersHeight;
    }
}
