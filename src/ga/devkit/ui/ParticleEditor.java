package ga.devkit.ui;

import com.sun.javafx.geom.Dimension2D;
import com.sun.javafx.geom.Rectangle;
import ga.engine.rendering.ParticleConfiguration;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameObject;
import ga.engine.xml.XMLWriter;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.BlendMode;
import org.w3c.dom.Element;

public class ParticleEditor extends Interface implements Initializable, Editor {

    @FXML
    private Canvas preview;
    
    private final ParticleSettings settings;
    private final File file;
    private final XMLWriter writer = new XMLWriter();

    public ParticleEmitter emitter;
    private long lastFire = 0;
    private GraphicsContext g;
    private Dimension2D editorSize;

    public ParticleEditor(Dimension2D size, File file) {
        this.file = file;
        editorSize = size;
        settings = new ParticleSettings(this);
        settings.load();
    }

    @Override
    public void save() {
        Element root = writer.createElement("particleSystem");
        writer.createElementValue(root, "direction", String.valueOf((int) settings.getDirection().getValue()));
        writer.createElementValue(root, "spread", String.valueOf((int) settings.getSpread().getValue()));
        writer.createElementValue(root, "size", String.valueOf((int) settings.getSize().getValue()));
        writer.createElementValue(root, "sizeEnd", String.valueOf((int) settings.getSizeEnd().getValue()));
        writer.createElementValue(root, "sizeStep", String.valueOf((int) settings.getSizeStep().getValue()));
        writer.createElementValue(root, "life", String.valueOf((int)settings.getLife().getValue()));
        writer.createElementValue(root, "color", String.format("#%X", settings.getColor().getValue().hashCode()));
        writer.createElementValue(root, "mode", settings.getMode().isSelected() ? "MODE_SINGLE" : "MODE_CONTINUOUS");
        writer.createElementValue(root, "gravity", String.valueOf(settings.getGravity().getValue()));
        writer.createElementValue(root, "velocity", String.valueOf(settings.getVelocity().getValue()));
        writer.createElementValue(root, "velocityMin", String.valueOf(settings.getVelocityMin().getValue()));
        writer.createElementValue(root, "velocityMax", String.valueOf(settings.getVelocityMax().getValue()));
        writer.createElementValue(root, "velocityStep", String.valueOf(settings.getVelocityStep().getValue()));
        writer.createElementValue(root, "rate", String.valueOf(settings.getRate().getValue()));
        writer.createElementValue(root, "particleShape", settings.getSquare().isSelected() ? "square" : "circle");
        writer.createElementValue(root, "shape", String.valueOf(settings.getAreaBox().getText()));
        writer.createElementValue(root, "colorMid", String.format("#%X", settings.getColorMid().getValue().hashCode()));
        writer.createElementValue(root, "colorEnd", String.format("#%X", settings.getColorEnd().getValue().hashCode()));
        writer.createElementValue(root, "colorPoint", String.valueOf(settings.getColorPoint().getValue()));
        writer.createElementValue(root, "random", String.valueOf(settings.getRandom().getValue()));
        writer.save("particles/systems/" + file.getName());
    }

    @Override
    public void load() {
        super.load();
        System.out.println("Loading " + "particles/systems/" + file.getPath().substring(file.getPath().lastIndexOf("\\") + 1) + " in particle editor");
        emitter = ParticleEmitter.loadXML("particles/systems/" + file.getPath().substring(file.getPath().lastIndexOf("\\") + 1));
        emitter.gameobject = new GameObject(editorSize.width / 2, editorSize.height / 2);
        emitter.gameobject.addComponent(emitter);
        emitter.transform = emitter.gameobject.transform;
        preview.setWidth(editorSize.width);
        preview.setHeight(editorSize.height);
        g = preview.getGraphicsContext2D();
        settings.updateSliders(emitter.getConfig());
        settings.updatePreview();
        int x = Integer.parseInt((settings.areaBox.getText().split(",")[0].trim())),
                y = Integer.parseInt(settings.areaBox.getText().split(",")[1].trim());
        emitter.setShape(new Rectangle(-x / 2, -y / 2, x, y));
        emitter.physics(false);
        settings.initEvents();
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (settings.getMode().isSelected()) {
                    if (now > lastFire + 1000000000) {
                        lastFire = now;
                        emitter.fire();
                    }
                }
                emitter.interpolate = settings.getInterpolate().isSelected();
                g.setGlobalBlendMode(BlendMode.SRC_OVER);
                g.clearRect(0, 0, preview.getWidth(), preview.getHeight());
                emitter.update();
                emitter.physicsUpdate(new HashSet<>());
                if (settings.getBlendAdd().isSelected()) {
                    g.setGlobalBlendMode(BlendMode.ADD);
                } else {
                    g.setGlobalBlendMode(BlendMode.SRC_OVER);
                }
                emitter.render(g);
            }
        }.start();
    }

    @Override
    public void rightContent(SplitPane sidebar) {
        sidebar.getItems().add(settings.root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void updateConfig(ParticleConfiguration c) {
        int x = Integer.parseInt((c.getValue("shape").split(",")[0].trim())),
                y = Integer.parseInt(c.getValue("shape").split(",")[1].trim());
        emitter.setConfig(c);
        emitter.setShape(new Rectangle(-x / 2, -y / 2, x, y));
    }

    public ParticleConfiguration getConfig() {
        return emitter.getConfig();
    }

    @Override
    public File getFile() {
        return file;
    }
}
