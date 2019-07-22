package gameEngine.ModelImplementations.Events;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class DIYCondition extends Condition implements Observer {

    String groov;
    Binding binding = new Binding();
    GroovyShell gs = new GroovyShell(binding);

    public DIYCondition(String groov, List<Object> list1, List<Object> list2){
        super();
        list2.remove(list2.size()-1);

        this.groov = groov;
        binding.setVariable("list1", list1);
        binding.setVariable("list2", list2);

    }


    @Override
    public void update(Observable o, Object arg) {
        reset();
        binding.setVariable("obj", o);
        gs.evaluate(groov);
        if((boolean)gs.getVariable("update"))
            setConditionMet();

    }
}
