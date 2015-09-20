package ga.game.entity;

import ga.engine.scene.GameScene;

public class FriendlyAI extends AI {
    
    public FriendlyAI(GameScene scene) {
        super(scene);
        FOLLOW_DISTANCE = 64;
    }
}
