package gameEngine.ModelImplementations.Statistics;

public class Attack {
    private double attackPoints;

    public Attack(double hp){
        attackPoints =hp;
    }

    public double increaseAttackPoints(double ap){
        attackPoints+=ap;
        System.out.println("Attack increase " + attackPoints);
        return attackPoints;
    }

    public double heavyAttack(){
        return attackPoints/10;
    }
    public double lightAttack(){
        return attackPoints/20;
    }
    public double getAttackPoints() {
        return attackPoints;
    }

    public void setAttackPoints(double attackPoints) {
        this.attackPoints = attackPoints;
    }
}
