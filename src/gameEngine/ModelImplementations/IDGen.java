package gameEngine.ModelImplementations;

public class IDGen {

    private int counter = 0;

    public int next(){
        int res = counter;
        counter++;
        return res;
    }
}
