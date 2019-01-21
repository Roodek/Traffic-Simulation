package mapRenderer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.javafx.scene.traversal.Direction;
import javafx.beans.property.SimpleBooleanProperty;
import mapRenderer.utils.Coord;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Street {
    private String name;
    private Direction direction;
    private double speedLimit;
    private SimpleBooleanProperty generator = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty trafficLights = new SimpleBooleanProperty(false);
    private LinkedHashSet<Coord> coords = new LinkedHashSet<>();
    private LinkedList<Vehicle> vehiclesOnRoad = new LinkedList<>();

    public Street() {
    }

    public Street(String name, Direction direction, Double speed) {
        this.name = name;
        this.direction = direction;
        this.speedLimit = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeed(double speed) {this.speedLimit = speed;}

    public double getSpeed(){return this.speedLimit;}

    public LinkedHashSet<Coord> getCoords() {
        return coords;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setCoords(LinkedHashSet<Coord> coords) {
        this.coords = coords;
    }

    public boolean isGenerator() {
        return generator.get();
    }

    public SimpleBooleanProperty generatorProperty() {
        return generator;
    }

    public void setGenerator(boolean generator) {
        this.generator.set(generator);
    }

    public LinkedList<Vehicle> getVehiclesOnRoad(){
        return this.vehiclesOnRoad;
    }

    public void generateCar(int id){
        Vehicle vehicle = new Vehicle();
        vehicle.setStreet(this);
        vehicle.setSpeed(5);
        vehicle.id = id;
        addVehicleToStreet(vehicle);
    }

    public void addVehicleToStreet(Vehicle vehicle){
        this.vehiclesOnRoad.add(vehicle);
    }
    public void popVehicleFromStreet(Vehicle vehicle){
        this.vehiclesOnRoad.remove(vehicle);
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(double speedLimit) {
        this.speedLimit = speedLimit;
    }

    public boolean isTrafficLights() {
        return trafficLights.get();
    }

    public SimpleBooleanProperty trafficLightsProperty() {
        return trafficLights;
    }

    public void setTrafficLights(boolean trafficLights) {
        this.trafficLights.set(trafficLights);
    }

    public void setVehiclesOnRoad(LinkedList<Vehicle> vehiclesOnRoad) {
        this.vehiclesOnRoad = vehiclesOnRoad;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Street street = (Street) o;
        return Objects.equals(name, street.name) &&
                direction == street.direction &&
                Objects.equals(generator, street.generator) &&
                Objects.equals(coords, street.coords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, direction, generator, coords);
    }

    @Override
    public String toString() {
        return name + " " + direction.toString();
    }
}
