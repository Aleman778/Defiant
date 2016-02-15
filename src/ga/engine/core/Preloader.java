package ga.engine.core;

import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameScene;
import ga.engine.scene.SceneParser;
import ga.engine.scene.TMXParser;

public final class Preloader {
    
    public static final GameScene SCENE = SceneParser.execute("scenes/Preloader.scene");
    
    private Preloader() {
        
    }
    
    public static void loadResources() {
        ResourceManager.getBlueprint("blueprints/required/Player.blueprint");
        ResourceManager.getBlueprint("blueprints/physics/Collision.blueprint");
        ResourceManager.getBlueprint("blueprints/required/GameHud.blueprint");
        ResourceManager.getBlueprint("blueprints/entities/Ant.blueprint");
        ResourceManager.getFont("fonts/GeosansLight.ttf", 20);
        ResourceManager.getFont("fonts/GeosansLight.ttf", 37);
    }
    
    public static GameScene loadScene(String filepath) {
        if (filepath.endsWith(".scene")) {
            return SceneParser.execute(filepath);
        } else if (filepath.endsWith(".tmx")) {
            return TMXParser.execute(filepath);            
        }
        return null;
    }
}
