package ga.devkit.core;

import ga.devkit.ui.Core;
import javafx.scene.Scene;

public class Devkit {
    
    private Scene scene;
    
    public Devkit() {
        Core core = new Core();
        core.load();
        scene = new Scene(core.root);
    }
    
    public Scene get() {
        return scene;
    }
}
