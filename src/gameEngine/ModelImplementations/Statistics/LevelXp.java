package gameEngine.ModelImplementations.Statistics;

public class LevelXp {
    private double[] levelThreshold;
    private double  xp = 0;
    private int currentLevel=0;
    public static final int DEFAULT_NUMBER_FOR_CALCULATING_LEVEL_THRESHOLDS =3;

    public LevelXp(int numberOfLevels){
        levelThreshold=new double [numberOfLevels];
        setThresholds();
    }
    private void setThresholds() {
        int numLevels = levelThreshold.length;
        for (int i = 0; i < numLevels; i++) {
            levelThreshold[i] = Math.pow(DEFAULT_NUMBER_FOR_CALCULATING_LEVEL_THRESHOLDS, i) - 1;
        }
    }
    public double  addXp(double  xp){
        this.xp+=xp;
        return this.xp;
    }

    public int checkCurrentLevel(){
        int i = 0;
        while(levelThreshold[i]<xp){
            if(i==levelThreshold.length-1){
                break;
            }
            i++;
        }
        currentLevel=i-1;
        return currentLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public double  getXp() {
        return xp;
    }
}
