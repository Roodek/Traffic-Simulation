package mapRenderer;

import mapRenderer.utils.Coord;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Crossroad {

    private Coord coord;
    private List<Street> streets = new LinkedList<>();

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    public boolean isVehicleOnCrossroad(Vehicle vehicle){
        return (Vehicle.getDistanceBetweenPoints(vehicle.getPosition(),this.coord) <= 20) ;
    }

    public Street getRandomStreetApartFromStreet(){

        return streets.get(new Random().nextInt(streets.size()-1));
        //return streets.get(3);
    }


}
