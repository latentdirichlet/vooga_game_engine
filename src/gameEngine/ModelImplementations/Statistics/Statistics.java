package gameEngine.ModelImplementations.Statistics;

import gameEngine.ModelImplementations.Events.Condition;

/**
 * @author aks64
 * this class keeps track of all the stats for the game. Class ensures flexibility of indiviudal stats while providing a
 * single place to access all stats
 *
 */
public class Statistics {
    private Health healthManager;
    private Attack attackManager;
    private LevelXp levelXpManager;
    private Armor armorManager;

    private double mana;
    private double defense;

    //private Map<Integer, Double> lvlExpMap;

    /**
     * constructor to define values for stats
     * @param hp
     * @param ap
     * @param numLevels
     */
    public Statistics(double hp, double ap, int numLevels){
        setUpHealth(hp);
        setUpAttack(ap);
        setUpLevelXp(numLevels);
    }

    /**
     * default constructor
     */
    public Statistics(){
        setUpHealth(100.0, 150.0);
        setUpAttack(20.0);
        setUpLevelXp(10);
        setUpArmor();
    }

    /**
     * will take one or two parameters and set health and max health for health.
     * @param hp
     */
    private void setUpHealth(Double... hp){
        if(hp.length>1){
            healthManager= new Health(hp[0],hp[1]);
        }else{
            healthManager = new Health(hp[0]);
        }
    }

    /**
     * the class will increase the actors health by the amount that is passed in as the parameter
     * @param amount
     */
    public void increaseHP(Double amount){
        healthManager.addHealth(amount);
    }
    /**
     * the class will decrease the actors health by the amount that is passed in as the parameter
     * @param amount
     */
    public void decreaseHP(Double amount){
        healthManager.getDamage(amount);
    }
    /**
     * the class will set the actors health to the amount that is passed in as the parameter
     * @param amount
     */
    public void setHP(Double amount){
        healthManager.setHealthPoints(amount);
    }

    /**
     * increase the maximum health an actor by the amount that is passed in as the parameter.
     * @param amount
     */
    public void increaseMaxHealth(Double amount){
        healthManager.increaseMaxHealth(amount);
    }

    /**
     * will return current health points of the actor
     * @return
     */
    public double getHealth() {
        return healthManager.getHealthPoints();
    }
    /**
     * will return the maximum health points of the actor
     * @return
     */
    public double getMaxHealth() {
        return healthManager.getMaxHealth();
    }

    /**
     * will initialize Attack stat with the value passed in as a parameter
     * @param ap
     */
    private void setUpAttack(Double ap){
        attackManager=new Attack(ap);
    }

    /**
     * the class will increase the actors attack by the amount that is passed in as the parameter
     * @param amount
     */
    public void increaseAttackPoints(Double amount){
        attackManager.increaseAttackPoints(amount);
    }
    /**
     * will return current attack points of the actor
     * @return
     */
    public double getAttackpoints(){
        return attackManager.getAttackPoints();
    }

    /**
     * will initialize Armor
     */
    private void setUpArmor(){
        armorManager = new Armor(0);
    }
    /**
     * the class will increase the actor's armor by the amount that is passed in as the parameter
     * @param amount
     */
    public void increaseArmor(Double amount){
        armorManager.increaseArmor(amount);
    }
    /**
     * the class will increase the actor's armor by 10 points
     *
     */
    public void increaseArmor(){
        armorManager.increaseArmor();
    }

    /**
     * will return Armor of the Actor
     * @return
     */
    public double getArmor(){
        return armorManager.getArmor();
    }

    /**
     * will initialize LevelXp using number of levels as parameter
     *
     * @param numLevels
     */
    private void setUpLevelXp(int numLevels){
        levelXpManager=new LevelXp(numLevels);

    }

    /**
     * will increase experience by the amount that is passed in as the parameter
     * @param amount
     */
    public void increaseExp(Double amount){
        levelXpManager.addXp(amount);
        levelXpManager.checkCurrentLevel();
    }

    /**
     * will return current experience points of the Actor
     * @return
     */
    public double getExp(){
        return levelXpManager.getXp();
    }
    /**
     * will return Actor's current level
     * @return
     */
    public double getLevel() {
        return levelXpManager.getCurrentLevel();
    }


    public void addPropertyListeners(Condition c) {
        healthManager.addObserver(c);
    }

}
