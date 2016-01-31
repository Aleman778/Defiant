package ga.engine.core;

import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameScene;
import ga.engine.scene.SceneParser;

public final class Preloader {
    
    private GameScene scene;
    
    public Preloader() {
        scene = SceneParser.execute("scenes/Preloader.scene");
    }
    
    public void load() {
        ResourceManager.getBlueprint("blueprints/Player.blueprint");
    }
    
    public GameScene getScene() {
        return scene;
    }
}
