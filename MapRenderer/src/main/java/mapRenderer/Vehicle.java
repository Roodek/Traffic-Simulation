package mapRenderer;

import mapRenderer.utils.Coord;

import java.util.LinkedList;

public class Vehicle {
    private Coord pos;

    private double speed;
    private double slowDownProbability = 0;
    private double laneChangeProbability = 0;
    private double nextVehicleDistance = 0;
    int id;

    double speedlimit;
    private double safeDistance;
    private Vehicle vehicleInFront;
    private Street street;



    public Vehicle(Coord pos, double speed, int id){
        this.pos = pos;
        this.speed = speed;
        this.id = id;
    }
    public Vehicle(){}

    public void updateSpeed(){
        //updateSafeDistance();
        //if (this.nextVehicleDistance<5) this.speed=0;
        if (this.vehicleInFront == null)
        {
            if (this.speed > this.speedlimit || this.safeDistance > this.nextVehicleDistance) this.speed = this.slowDown();
            else this.speed = this.accelerate();
            return;
        }
        else if(this.nextVehicleDistance >= this.safeDistance && this.speed < this.speedlimit)//&& this.speed < this.speedlimit )
            this.speed = this.accelerate();
        else if(this.nextVehicleDistance < this.safeDistance){
            this.speed = this.slowDown();
        }
    }

    public void setSafeDistance(double safeDistance) {
        this.safeDistance = safeDistance;
    }

    private void updateSafeDistance(){
        double x=0;
        if(this.vehicleInFront != null) x=this.vehicleInFront.safeDistance/2.0;
        this.safeDistance = 10 - x + this.speed*this.speed/(2*(20/this.id));
    }
    private double accelerate(){
        double acceleration = Math.max(0, this.speed+1);
        return Math.min(this.speedlimit, this.speed+=acceleration);
    }
    private double slowDown(){ return Math.max(0,this.speed-=5.5/this.id); }


    public void calcNextVehicleDistance(){
        if(this.vehicleInFront == null){
            this.nextVehicleDistance = 0;
        }else{
            this.nextVehicleDistance = getDistanceBetweenPoints(this.getPosition(),vehicleInFront.getPosition());
        }
    }

    public void changeRoadAtCrossroad(Crossroad crossroad){
        if(this.pos.getX() == crossroad.getCoord().getX() && this.pos.getY() == crossroad.getCoord().getY()){
            this.street.popVehicleFromStreet(this);
            Street street ;
            if (crossroad.getFirstStreet().equals(this.street)){
                street = crossroad.getSecondStreet();
            }
            else{
                street = crossroad.getFirstStreet();
            }
            this.setStreet(street);
        }
    }

    public void setPosition(Coord pos){
        this.pos = pos;
    }
    Coord getPosition() { return this.pos; }

    public void setNextVehicleDistance(double nextVehicleDistance) {
        this.nextVehicleDistance = nextVehicleDistance;
    }

    public void setLaneChangeProbability(double laneChangeProbability) {
        this.laneChangeProbability = laneChangeProbability;
    }

    public void setSlowDownProbability(double slowDownProbability) {
        this.slowDownProbability = slowDownProbability;
    }
    public void setVehicleInFront(Vehicle vehicle){
        this.vehicleInFront = vehicle;
        this.nextVehicleDistance = getDistanceBetweenPoints(this.getPosition(),vehicle.getPosition());
    }

    public double getLaneChangeProbability() {
        return laneChangeProbability;
    }

    public double getSlowDownProbability() {
        return slowDownProbability;
    }

    public void setStreet(Street street) {
        this.street = street;
        this.speedlimit = street.getSpeed();
        LinkedList<Coord> coords = new LinkedList<>(street.getCoords());
        this.setPosition(coords.getFirst());
        if(street.getVehiclesOnRoad().size() > 0) {
            this.vehicleInFront = street.getVehiclesOnRoad().getLast();
            calcNextVehicleDistance();
        }

    }
    public Street getStreet() {
        return this.street;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public double getNextVehicleDistance(){return this.nextVehicleDistance;}

    static double getDistanceBetweenPoints(Coord a, Coord b){
        return Math.sqrt(Math.pow(a.getX() - b.getX(),2) + Math.pow(a.getY() - b.getY(),2));
    }
    public void onRoad(){
        LinkedList<Coord> coords = new LinkedList<>(street.getCoords());
        if(this.pos.equals(coords.getLast())){
            this.street.popVehicleFromStreet(this);
        }

    }
}
