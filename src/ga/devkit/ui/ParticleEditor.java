package ga.devkit.ui;

import com.sun.javafx.geom.Dimension2D;
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
import javafx.scene.control.TitledPane;
import org.w3c.dom.Element;

public class ParticleEditor extends Interface implements Initializable, Editor {

    @FXML
    private Canvas preview;
    @FXML
    private TitledPane previewContainer;
    private ParticleEmitter emitter;

    private final File file;
    private long lastFire = 0;
    private GraphicsContext g;
    private Dimension2D editorSize;
    private final XMLWriter writer = new XMLWriter();
    private ParticleEditorRight right;

    public ParticleEditor(Dimension2D size, File file) {
        this.file = file;
        editorSize = size;
        right = new ParticleEditorRight(this);
        right.load();
    }

    @Override
    public void save() {
        Element root = writer.createElement("particleSystem");
        writer.createElementValue(root, "direction", String.valueOf((int) right.getDirection().getValue()));
        writer.createElementValue(root, "spread", String.valueOf((int) right.getSpread().getValue()));
        writer.createElementValue(root, "size", String.valueOf((int) right.getSize().getValue()));
        //   writer.createElementValue(root, "life", String.valueOf(life));
        writer.createElementValue(root, "color", String.format("#%X", right.getColor().getValue().hashCode()));
        writer.createElementValue(root, "mode", right.getMode().isSelected() ? "MODE_SINGLE" : "MODE_CONTINUOUS");
        writer.createElementValue(root, "gravity", String.valueOf(right.getGravity().getValue()));
        writer.save("particles/systems/" + file.getName());
    }

    @Override
    public void load() {
        super.load();
        System.out.println("Loading " + "particles/systems/" + file.getPath().substring(file.getPath().lastIndexOf("\\") + 1) + " in particle editor");
        emitter = ParticleEmitter.loadXML("particles/systems/" + file.getPath().substring(file.getPath().lastIndexOf("\\") + 1));
        emitter.gameobject = new GameObject(preview.getWidth() / 2, preview.getHeight() / 2);
        emitter.gameobject.addComponent(emitter);
        emitter.transform = emitter.gameobject.transform;
        preview.setWidth(editorSize.width);
        preview.setHeight(editorSize.height);
        g = preview.getGraphicsContext2D();
        right.updateSliders(emitter.getConfig());
        right.initEvents();
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (right.getMode().isSelected()) {
                    if (now > lastFire + 1000000000) {
                        lastFire = now;
                        emitter.fire(20);
                    }
                }
                g.clearRect(0, 0, preview.getWidth(), preview.getHeight());
                emitter.update();
                emitter.physicsUpdate(new HashSet<>());
                emitter.render(g);
            }
        }.start();
    }

    @Override
    public void rightContent(SplitPane sidebar) {
        sidebar.getItems().add(right.root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void updateConfig(ParticleConfiguration c) {
        emitter.setConfig(c);
    }

    public ParticleConfiguration getConfig() {
        return emitter.getConfig();
    }
}
