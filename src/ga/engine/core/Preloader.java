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
        ResourceManager.getBlueprint("blueprints/required/Player.blueprint");
        ResourceManager.getBlueprint("blueprints/physics/Collision.blueprint");
        ResourceManager.getBlueprint("blueprints/required/GameHud.blueprint");
        ResourceManager.getBlueprint("blueprints/entities/Ant.blueprint");
        ResourceManager.getFont("fonts/GeosansLight.ttf", 20);
        ResourceManager.getFont("fonts/GeosansLight.ttf", 37);
    }
    
    public GameScene getScene() {
        return scene;
    }
}
