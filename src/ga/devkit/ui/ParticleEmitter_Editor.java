package ga.devkit.ui;

import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class ParticleEmitter_Editor extends ParticleEmitter {

    public ParticleEmitter_Editor(Canvas canvas, Vector2D position, float direction, float spread, float size, int mode, float life, Color color) {
        super(position, direction, spread, size, mode, life, color);
        this.canvas = canvas;
        this.graphics = canvas.getGraphicsContext2D();
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
