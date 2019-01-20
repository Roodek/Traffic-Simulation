package mapRenderer.utils;

import javafx.scene.shape.Circle;

import java.util.Objects;

public class Coord {
    private double x;
    private double y;



    public Coord() {
    }

    public Coord(Circle circle) {
        this.x = circle.getCenterX();
        this.y = circle.getCenterY();
    }
    public Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public static double kmh2ms(double speed) {
        return speed * 0.277777778;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return Double.compare(coord.x, x) == 0 &&
                Double.compare(coord.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
