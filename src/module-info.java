module voogasalad_voogalicious {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;
    requires java.scripting;
    exports playerEnvironment.view;
    exports gameEngine.ModelImplementations;
    exports authoringEnvironment;
    exports authoringEnvironment.backend.gameDynamics to javafx.graphics;
    opens authoringEnvironment.backend.gameBuilder to xstream;
    opens authoringEnvironment.backend.gameDynamics to groovy.all;
    opens gameEngine.ModelImplementations to xstream;
    opens gameEngine.ModelInterfaces to xstream, groovy.all;
    opens gameEngine.ModelImplementations.Statistics to xstream;
    opens gameEngine.ModelImplementations.Events to xstream;
    opens authoringEnvironment.backend.groovy to xstream, groovy.all;
    opens playerEnvironment.view to xstream;
    requires xstream;
    requires groovy.all;
    requires java.datatransfer;
//    requires junit;

}
