package playerEnvironment.controller;

import java.util.ArrayList;
import java.util.List;

public class TextBoxHelper {
    private List<String> textList = new ArrayList<>();
    private final int characterLimit = 102;
    private int counter = 0;

    public void receiveText(String fullText){
        this.generateTextList(fullText);
    }

    private void generateTextList(String fullText){
        String[] tmpArr = fullText.split(" ");
        int sum = 0;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < tmpArr.length; i++){
            builder.append(tmpArr[i]);
            builder.append(" ");
            sum += tmpArr[i].length() + 1;
            if(sum > characterLimit || i >= tmpArr.length-1){
                textList.add(builder.toString());
                builder = new StringBuilder();
                sum = 0;
            }
        }
    }

    public boolean hasNext(){
        return counter < textList.size();
    }

    public String nextText(){
        String res = textList.get(counter);
        counter++;
        return res;
    }

    public static void main(String[] args){
        TextBoxHelper tx = new TextBoxHelper();
        tx.receiveText("I started a new job. My boss said: 'Hi, my name is Rebecca, " +
                "but people call me Becky.' I said: 'My name is Kyle, but people call me Dick.' " +
                "She said: 'How do you get dick from kyle?' " +
                "I replied: 'You just ask nicely. '");
//        System.out.println(tx.nextText());
    }

}
