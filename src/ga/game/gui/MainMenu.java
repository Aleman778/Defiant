package ga.game.gui;

import ga.engine.core.Application;
import ga.engine.input.Input;
import ga.engine.rendering.ParticleConfiguration;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;

public class MainMenu extends GameComponent {
    
    public long now = System.nanoTime() + 10;
    public int nowdir = 1;
    public long nowfade = System.nanoTime() + 50;
    public int nowfadedir = 1;
    public long time = 0;
    public long lasttime = System.nanoTime();
    
    private AudioClip musicClip;
    
    public MainMenu() {
        musicClip = ResourceManager.getAudio("audio/music/FL4-120.mp3");
        musicClip.setCycleCount(AudioClip.INDEFINITE);
    }

    @Override
    public void start() {
        musicClip.play(0.4);
    }

    @Override
    public void awoke() {
        ParticleEmitter starEmitter = ParticleEmitter.loadXML("particles/systems/Stars.psystem");
        GameObject objectEmitter = new GameObject("partsystem");
        objectEmitter.addComponent(starEmitter);
        gameobject.addChild(objectEmitter);
        starEmitter.start();
        ParticleConfiguration config = new ParticleConfiguration();
    }
    
    @Override
    public void render(GraphicsContext g) {
        time = lasttime - System.nanoTime();
        lasttime = System.nanoTime();
        double alpha = Math.min(Math.max((now - System.nanoTime()) / 500000000.0, -1), 1);
        double fade = Math.min(Math.max((nowfade - System.nanoTime()) / 500000000.0, -1), 1);
        double w = Application.getWidth();
        double h = Application.getHeight();
        
        //Set text align
        g.setTextAlign(TextAlignment.CENTER);
        
        //Draw Background
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, Application.getWidth(), Application.getWidth());
        
        //Draw Title
        g.setFont(ResourceManager.getFont("fonts/GeosansLight.ttf", 120));
        g.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.REFLECT,
                new Stop(0, new Color(1, 1, 1, Math.abs(alpha))),
                new Stop(1, new Color(0, 0, 0, Math.abs(alpha)))));
        g.fillText("Defiant", w / 2, 120 - 100 - alpha * nowdir * 100);
        g.setFill(new Color(0.3, 0.3, 0.3, Math.abs(alpha)));
        g.setFont(ResourceManager.getFont("fonts/GeosansLight.ttf", 20));
        g.fillText("Gymnasie Arbete 2015 - 2016", w / 2, 150 - 100 - alpha * nowdir * 100);
        
        //Draw Menu
        g.setFont(ResourceManager.getFont("fonts/GeosansLight.ttf", 37));
        g.fillText("Press enter to play", w / 2, h - 64);
    }

    @Override
    public void update() {
        if (Input.getKeyPressed(KeyCode.ENTER)) {
            musicClip.stop();
            Application.setScene("scenes/TL.tmx");
        }
    }
    
    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES_NONE;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
    }

    @Override
    public GameComponent instantiate() {
        return new MainMenu();
    }
    
}