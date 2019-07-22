package playerEnvironment.controller;

import gameEngine.ModelImplementations.Position;

public class Coordinate implements Comparable {
    private double x;
    private double y;

    public Coordinate(double x, double y){
        this.x = x;
        this.y = y;
    }

    public static Coordinate PosToCor(Position pos){
        return new Coordinate((double)pos.getX(), (double)pos.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Coordinate)){
            return -1;
        }
        return ((Double)x).compareTo(((Coordinate) o).getX());
    }
}
