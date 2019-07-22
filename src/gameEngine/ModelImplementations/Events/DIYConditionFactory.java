package gameEngine.ModelImplementations.Events;

import gameEngine.ModelInterfaces.ConditionFactory;

import java.util.ArrayList;
import java.util.List;

public class DIYConditionFactory implements ConditionFactory {

    @Override
    public Condition createNewCondition(List<List<Object>> arguments) {
//        String className1 = (String) arguments.get(0).get(0);
        String groov = (String) arguments.get(1).get(arguments.get(1).size()-1);
//        String className2 = "";
//        Class class1 = null;
//        Class class2 = null;
//        if(arguments.get(0).size()>2)
//            className2 = (String) arguments.get(0).get(2);
//        try{
//            class1 = Class.forName(className1);
//            if (!className2.isEmpty())
//                class2= Class.forName(className2);
//        }
//        catch (ClassNotFoundException e)
//        {
//
//        }
        List<Object> list2 = new ArrayList<>();
        if (arguments.size()>1)
            list2 = arguments.get(1);

        return new DIYCondition( groov, arguments.get(0), list2);

    }


}
