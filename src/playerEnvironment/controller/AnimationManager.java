package playerEnvironment.controller;
import gameEngine.ModelImplementations.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationManager {

    private Map<Integer, Sprite> defaultMap = new HashMap<>();
    private Map<Integer, Animation> animationMap = new HashMap<>();
    private Map<Integer, Boolean> activateMap = new HashMap<>();
    private Map<Integer, Animation> defaultAnimationMap = new HashMap<>();

    public void setObjectAnimation(int id, Animation animation){
        registerID(id);
        if(!activateMap.get(id)){
            animationMap.put(id, animation);
        }
    }

    public void setObjectDefault(int id, Sprite sprite){
        registerID(id);
        defaultMap.put(id, sprite);
    }

    public void setObjectIdleAnimation(int id, Animation animation){
        defaultAnimationMap.put(id, animation);
    }

    public Sprite getObjectSprite(int id){
        Sprite res = getActivatedAnimation(id);
        if(res != null){
            return res;
        }
        res = getDefaultAnimation(id);
        if(res != null){
            return res;
        }
        return defaultMap.get(id);
    }

    private Sprite getActivatedAnimation(int id){
        registerID(id);
        if(activateMap.get(id)){
            Animation an = animationMap.get(id);
            if(an.hasNext()){
                return an.next();
            }
            else{
                an.reset();
                deactivateAnimation(id);
            }
        }
        return null;
    }

    private Sprite getDefaultAnimation(int id){
        if(defaultAnimationMap.containsKey(id)){
            Animation an = defaultAnimationMap.get(id);
            if(an.hasNext()){
                return an.next();
            }
            else{
                an.reset();
                return an.next();
            }
        }
        return null;
    }

    public boolean isAnimating(int id){
        registerID(id);
        return activateMap.get(id);
    }

    public void activateAnimation(int id){
        if(animationMap.containsKey(id)) {
            activateMap.put(id, true);
        }
    }

    public void deactivateAnimation(int id){
        if(animationMap.containsKey(id)) {
            activateMap.put(id, false);
        }
    }

    private void registerID(int id){
        if(!activateMap.containsKey(id)){
            activateMap.put(id, false);
        }
    }

    public void clear(){
        defaultMap.clear();
        animationMap.clear();
        activateMap.clear();
        defaultAnimationMap.clear();
    }

}
