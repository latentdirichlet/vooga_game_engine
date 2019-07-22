package playerEnvironment.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Animation {

    private SpriteSheet ss;
    private List<Integer> frameList;
    private List<Sprite> spriteList;
    private boolean endless = false;
    private int posidx = 0;
    private int counter = 0;

    /**
     * input specifications
     * @param frameList a list of frames on which the rendered image changes
     *                  should be in ascending order and should have no repetition
     * @param spriteList a list of sprite to paint on the screen
     */
    public Animation(List<Integer> frameList, List<Sprite> spriteList){
        this.frameList = frameList;
        this.spriteList = spriteList;
        invariant();
    }

    public Animation(List<Sprite> spriteList, int totalFrame){
        this.spriteList = spriteList;
        int spriteNum = spriteList.size();
        frameList = new ArrayList<>();
        for(int i = 1; i <= spriteNum; i++){
            frameList.add(((Double)(totalFrame * (i/(double)spriteNum))).intValue());
        }
    }

    public Animation(List<Integer> frameList, List<Sprite> spriteList, boolean endless){
        this.frameList = frameList;
        this.spriteList = spriteList;
        this.endless = endless;
        invariant();
    }

    private void invariant(){
        if(!(frameList.size() == spriteList.size())){
            throw new IllegalStateException("FrameList size should be the same as that of SpriteList");
        }
    }

    public Sprite getCurrentSprite(int currentFrame){
        for(int i = posidx; i < frameList.size(); i++){
            if(currentFrame <= frameList.get(i)){
                return spriteList.get(i);
            }
        }
        return null;
    }

    public boolean hasNext(){
        if(posidx < spriteList.size() || endless){
            return true;
        }
        return false;
    }

    public Sprite next(){
        if(posidx < spriteList.size()){
            Sprite res = spriteList.get(posidx);
            counter++;
            if(counter >= frameList.get(posidx)){
                posidx++;
            }
            return res;
        }
        return null;
    }

    public void reset(){
        posidx = 0;
        counter = 0;
    }

    public List<Integer> getFrameList() {
        List<Integer> res = new ArrayList<>();
        for(int i = 0; i < this.frameList.size(); i++){
            res.add(this.frameList.get(i));
        }
        return res;
    }

    public List<Sprite> getSpriteList() {
        List<Sprite> res = new ArrayList<>();
        for(int i = 0; i < this.spriteList.size(); i++){
            res.add(this.spriteList.get(i));
        }
        return res;
    }

    // append a2 to the end of a1
    public static Animation combineAnimation(Animation a1, Animation a2){
        List<Integer> frameList1 = a1.getFrameList();
        List<Integer> frameList2 = a2.getFrameList();
        List<Sprite> spriteList1 = a1.getSpriteList();
        List<Sprite> spriteList2 = a2.getSpriteList();

        int lastFrame = frameList1.get(frameList1.size()-1);

        for(int i = 0; i < frameList2.size(); i++){
            frameList1.add(lastFrame + frameList2.get(i));
            spriteList1.add(spriteList2.get(i));
        }
        return new Animation(frameList1, spriteList1);
    }

    public int getTotalFrame(){
        return frameList.get(frameList.size()-1);
    }
}
