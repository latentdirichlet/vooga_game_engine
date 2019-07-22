package gameEngine.ModelImplementations.Statistics;

/**
 * @author mpz5
 */
public class Armor {
    public static final double DEFAULT_ARMOR_INCREASE = 10;
    double armor;


    public Armor(double pArmor){
        armor = pArmor;
    }

    public void increaseArmor(){
        increaseArmor(DEFAULT_ARMOR_INCREASE);
    }

    public void increaseArmor(double amount){
        armor = armor + amount;
    };

    public double getArmor(){
        return armor;
    }
}
