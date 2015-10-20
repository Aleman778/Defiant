package ga.devkit.ui;

import ga.engine.rendering.ParticleEmitter;
import javafx.scene.paint.Color;

public class ParticleEmitterEditor extends ParticleEmitter {

    public ParticleEmitterEditor(float direction, float spread, float size, int mode, float life, Color color) {
        super(direction, spread, size, mode, life, color);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setLife(float life) {
        this.life = life;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public void setSpread(float spread) {
        this.spread = spread;
    }

    public void setSize(float size) {
        this.size = size;
    }
}
