package gameEngine.ModelImplementations;

/**
 * represents direction of a moveable object
 * currently supports 4 directions: north, south, east, west
 */
public class Direction {
    private int xdir;
    private int ydir;

    public Direction(int xdir, int ydir){
        setDirection(xdir, ydir);
    }

    public Direction(String dir){
        setDirection(dir);
    }

    private void invariant(){
        if(Math.abs(xdir) + Math.abs(ydir) != 1){
            throw new IllegalStateException("invalid direction!");
        }
    }

    public int getXdir() {
        return xdir;
    }

    public int getYdir() {
        return ydir;
    }

    public void setDirection(int xdir, int ydir){
        this.xdir = xdir;
        this.ydir = ydir;
        invariant();
    }

    public void reverseDirection(){
        setDirection(this.xdir*-1, this.ydir*-1);
        invariant();
    }

    @Override
    public String toString() {
        if(xdir > 0){
            return "east";
        }
        else if(xdir < 0){
            return "west";
        }
        else if(ydir < 0){
            return "north";
        }
        else if(ydir > 0){
            return "south";
        }
        else{
            return "null";
        }
    }

        public void south(){
        this.xdir = 0;
        this.ydir = 1;
    }

    public void north(){
        this.xdir = 0;
        this.ydir = -1;
    }

    public void east(){
        this.xdir = 1;
        this.ydir = 0;
    }

    public void west(){
        this.xdir = -1;
        this.ydir = 0;
    }

    public void setDirection(String input){
        String tmp = input.toLowerCase();
        switch(tmp){
            case "north":
                this.xdir = 0;
                this.ydir = -1;
                break;
            case "south":
                this.xdir = 0;
                this.ydir = 1;
                break;
            case "west":
                this.xdir = -1;
                this.ydir = 0;
                break;
            case "east":
                this.xdir = 1;
                this.ydir = 0;
                break;
            default:
                throw new IllegalArgumentException("direction input not recognized");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Direction)){
            return false;
        }
        Direction tmp = (Direction)obj;
        return tmp.getXdir() == this.getXdir() && tmp.getYdir() == this.getYdir();
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 37 * result + xdir;
        result = 37 * result + ydir;
        return result;
    }
}
