package ga.devkit.ui;

import ga.engine.rendering.ParticleConfiguration;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import ga.engine.xml.XMLWriter;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;

public class ParticleEditor extends Interface implements Initializable, Editor {

    @FXML
    private Canvas preview;
    @FXML
    private TitledPane previewContainer;
    private ParticleEmitter emitter;
    @FXML
    private Slider direction;
    @FXML
    private Slider spread;
    @FXML
    private Slider size;
    @FXML
    private ColorPicker color;
    @FXML
    private TextField text;
    @FXML
    private CheckBox mode;
    @FXML
    private Slider gravity;

    private final File file;
    private long lastFire = 0;
    private GraphicsContext g;
    private final XMLWriter writer = new XMLWriter();

    public ParticleEditor(File file) {
        this.file = file;
    }

    @Override
    public void save() {
        Element root = writer.createElement("particleSystem");
        writer.createElementValue(root, "direction", String.valueOf((int) direction.getValue()));
        writer.createElementValue(root, "spread", String.valueOf((int) spread.getValue()));
        writer.createElementValue(root, "size", String.valueOf((int) size.getValue()));
        //   writer.createElementValue(root, "life", String.valueOf(life));
        writer.createElementValue(root, "color", String.format("#%X", color.getValue().hashCode()));
        writer.createElementValue(root, "mode", mode.isSelected() ? "MODE_SINGLE" : "MODE_CONTINUOUS");
        writer.createElementValue(root, "gravity", String.valueOf(gravity.getValue()));
        writer.save("particles/systems/" + file.getName());
    }

    @Override
    public void load() {
        super.load();
        System.out.println("Loading " + "particles/systems/" + file.getPath().substring(file.getPath().lastIndexOf("\\") + 1) + " in particle editor");
        emitter.setConfig(ParticleEmitter.loadXMLConfig("particles/systems/" + file.getPath().substring(file.getPath().lastIndexOf("\\") + 1)));
        emitter.gameobject = new GameObject(preview.getWidth() / 2, preview.getHeight() / 2);
        emitter.gameobject.addComponent(emitter);
        emitter.transform = emitter.gameobject.transform;
        updateSliders();
        direction.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        spread.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        size.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        gravity.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        color.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
        mode.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
    }

    @Override
    public void rightContent(SplitPane sidebar) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        color.setValue(Color.BLUE);
        emitter = new ParticleEmitter();
        g = preview.getGraphicsContext2D();
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (mode.isSelected()) {
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
//        life.valueProperty().addListener((observable, newValue, oldValue) -> {
//            updatePreview();
//        });
    }

    private void updatePreview() {
        ParticleConfiguration c = emitter.getConfig();
        c.setValue("color", String.format("#%X", color.getValue().hashCode()));
        c.setValue("size", String.valueOf(size.getValue()));
        c.setValue("spread", String.valueOf(spread.getValue()));
        c.setValue("direction", String.valueOf(direction.getValue()));
        c.setValue("mode", mode.isSelected() ? "MODE_SINGLE" : "MODE_CONTINUOUS");
        c.setValue("gravity", String.valueOf(gravity.getValue()));
        emitter.setConfig(c);
//        c.setValue("life", String.valueOf(life.getValue()));
//        text.setText(String.format("new ParticleEmitter(new Vector2D(), %d, %d, %d, %s, %d, Color.web(\"%s\"));", (int) direction.getValue(), (int) spread.getValue(), (int) size.getValue(), modeString, 100, colorString));
    }

    private void updateSliders() {
        ParticleConfiguration c = emitter.getConfig();
        color.setValue(Color.web(c.getValue("color")));
        size.setValue(Double.valueOf(c.getValue("size")));
        spread.setValue(Double.valueOf(c.getValue("spread")));
        direction.setValue(Double.valueOf(c.getValue("direction")));
        gravity.setValue(Double.valueOf(c.getValue("gravity")));
        mode.setSelected(c.getValue("mode").equals("MODE_SINGLE"));
    }
}
