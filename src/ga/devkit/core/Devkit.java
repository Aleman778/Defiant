package ga.devkit.core;

import ga.devkit.ui.Core;
import ga.engine.input.Input;
import javafx.scene.Scene;

public class Devkit {
    
    private Scene scene;
    
    public Devkit() {
        Core core = new Core();
        core.load();
        scene = new Scene(core.root);
        Input input = new Input(scene);
    }
    
    public Scene get() {
        return scene;
    }
}
