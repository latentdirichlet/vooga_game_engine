package playerEnvironment.controller;

import gameEngine.ModelImplementations.GameCharacter;
import gameEngine.ModelImplementations.Monster;
import playerEnvironment.view.GameDisplay;

public class DisplayControl {

    private GameDisplay display;

    public DisplayControl(GameDisplay gd){
        this.display = gd;
    }

    public void displayTextBox(String message){
        display.enterTextBoxMode(message);
    }

    public void displayNewMap(){
        display.switchMap();
    }

    public void endGame(){
        display.endCombat();
    }

    public void initiateCombat(){
        display.initiateCombatDisplay();
    }
}
