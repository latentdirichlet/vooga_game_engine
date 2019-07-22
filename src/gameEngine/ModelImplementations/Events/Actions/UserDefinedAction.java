package gameEngine.ModelImplementations.Events.Actions;

import gameEngine.ModelImplementations.Actor;
import gameEngine.ModelImplementations.GameCharacter;
import gameEngine.ModelInterfaces.PlayerInterface;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UserDefinedAction {
    private Map<Object, Consumer<Object>> createAction(Object o, Consumer<Object> action){
        Map<Object, Consumer<Object>> reaction = new HashMap<>();
        reaction.put(o, action);
        return reaction;
    }

    public Map<Object, Consumer<Object>> DIYActorAction(Actor a, String groovyStr){
        Consumer<Object> DIYActorAction = obj -> {
            GameCharacter actor = (GameCharacter)obj;
            var binding = new Binding();
            GroovyShell gs = new GroovyShell(binding);
            binding.setVariable("actor", actor);
            gs.evaluate(groovyStr);

        };
        return createAction(a, DIYActorAction);
    }

    public Map<Object, Consumer<Object>> DIYPlayerAction(PlayerInterface pl, String groovyStr){
        Consumer<Object> DIYActorAction = obj -> {
            PlayerInterface player = (PlayerInterface)obj;
            var binding = new Binding();
            GroovyShell gs = new GroovyShell(binding);
            binding.setVariable("player", player);

            gs.evaluate(groovyStr);

        };
        return createAction(pl, DIYActorAction);
    }
}
