package ch.zhaw.prog2.fxmlcalculator;

import javafx.scene.Parent;

import java.util.HashMap;

public interface ControlledScreens {

    void setScreenList(HashMap<String, Parent> screens);
}
