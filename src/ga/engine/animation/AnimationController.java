package ga.engine.animation;

import ga.engine.scene.GameComponent;
import java.util.HashMap;

public class AnimationController extends GameComponent {
    
    private final HashMap<String, Animation> animations;
    private double frame;
    private String state;
    
    public AnimationController() {
        animations = new HashMap<>();
        state = "";
    }
    
    public void setState(String state) {
        if (!this.state.equals(state)) {
            this.frame = 0;
        }
        this.state = state;
    }
    
    public String getState() {
        return state;
    }
    
    public void addAnimation(String state, Animation animation) {
        animations.put(state, animation);
    }
    
    @Override
    public void update() {
        Animation animation = animations.get(state);
        if (animation == null)
            return;
        
        frame += animation.getSpeed();
        if (frame >= animation.getFrames()) {
            frame = 0;
        }
        else if (frame < 0) {
            frame = animation.getFrames() - 1;
        }
        animation.animate((int) frame);
    }
}
