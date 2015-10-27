package ga.engine.animation;

public abstract class Animation {
    
    private int frames;
    private double speed;
    
    public Animation(int frames, double speed) {
        this.frames = frames;
        this.speed = speed;
    }
    
    public Animation(int frames) {
        this(frames, 1.0);
    }
    
    public abstract void animate(int frame);

    public int getFrames() {
        return frames;
    }

    public double getSpeed() {
        return speed;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
