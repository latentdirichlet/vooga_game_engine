package gameEngine.ModelImplementations;

public class Size {

    private int height;
    private int width;

    public Size(int w, int h){
        width = w;
        height = h;
    }

    void setHeight(int h){
        height = h;
    }

    void setWidth(int w){
        width = w;
    }

    int getHeight(){
        return height;
    }

    int getWidth(){
        return  width;
    }
}
