package playerEnvironment.controller;

import gameEngine.ModelImplementations.Position;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Path {
    private Coordinate start;
    private Coordinate end;
    private int totalFrame;
    private int counter = 0;
    private double xInterval;
    private double yInterval;

    private Queue<Coordinate> points = new LinkedList<>();
    private Queue<Integer> frames = new LinkedList<>();

    // two constructors: one for grid, one for combat
    public Path(Position sp, Position ep, int totalFrame){
        this.start = Coordinate.PosToCor(sp);
        this.end = Coordinate.PosToCor(ep);
        this.totalFrame = totalFrame;
        this.counter = 0;
        intervalHelper(this.start, this.end, totalFrame);
    }

    public Path(Coordinate cs, Coordinate ce, int totalFrame){
        this.start = cs;
        this.end = ce;
        this.totalFrame = totalFrame;
        this.counter = 0;
        intervalHelper(cs, ce, totalFrame);
    }

    private void intervalHelper(Coordinate start, Coordinate end, int totalFrame){
        xInterval = (end.getX() - start.getX()) / (double) totalFrame;
        yInterval = (end.getY() - start.getY()) / (double) totalFrame;
    }

    public boolean hasNext(){
        if(counter < totalFrame || (!points.isEmpty())){
            return true;
        }
        return false;
    }

    public Coordinate next(){
        if(counter < totalFrame){
            Coordinate res = new Coordinate(start.getX() + (counter+1)*xInterval, start.getY() + (counter+1)*yInterval);
            counter++;
            return res;
        }
        else if(!points.isEmpty()){
//            System.out.println("lolllol");
            counter = 0;
            this.start = end;
//            System.out.println("start: " + this.start);
            this.end = points.poll();
//            System.out.println("end: " + this.end);
            this.totalFrame = frames.poll();
            intervalHelper(this.start, this.end, this.totalFrame);
            return next();
        }
        return new Coordinate(start.getX() + totalFrame*xInterval, start.getY() + totalFrame*yInterval);
    }

    @Override
    public String toString() {
        return "(" + start + ", " + end + ")";
    }


    public void extendPath(Coordinate newCor, int frame){
        points.add(newCor);
        frames.add(frame);
    }

    public static void main(String[] args){
        Path p = new Path(new Coordinate(0.0, 0.0), new Coordinate(1.0, 1.0), 5);
        p.extendPath(new Coordinate(2.0, 2.0), 5);
        p.extendPath(new Coordinate(2.0, 2.0), 5);
        for(int i = 0; i < 15; i++){
//            System.out.println(p.next());
        }
    }
}
