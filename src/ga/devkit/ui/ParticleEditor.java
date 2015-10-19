package ga.devkit.ui;

import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.scene.GameObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;

public class ParticleEditor extends Interface implements Initializable {
    
    @FXML
    private Canvas preview;
    @FXML
    private TitledPane previewContainer;
    private ParticleEmitterEditor emitter;
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
    private Slider life;
    private long lastFire = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        color.setValue(Color.BLACK);
        emitter = new ParticleEmitterEditor((float) direction.getValue(), (float) spread.getValue(), (float) size.getValue(), ParticleEmitter.MODE_CONTINUOUS, 100, color.getValue());
        GameObject object = new GameObject(preview.getWidth() / 2, preview.getHeight() / 2).addComponent(emitter);
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (mode.isSelected()) {
                    if (now > lastFire + 1000000000) {
                        lastFire = now;
                        emitter.fire();
                    }
                }
                emitter.update();
            }
        }.start();
        direction.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        spread.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        size.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        color.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
        mode.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
        life.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        updatePreview();
    }
    
    private void updatePreview() {
        emitter.setColor(color.getValue());
        emitter.setSize((float) size.getValue());
        emitter.setSpread((float) spread.getValue());
        emitter.setDirection((float) direction.getValue());
        emitter.setMode(mode.isSelected() ? 1 : 0);
        emitter.setLife((float) life.getValue());
        String colorString = String.format("#%X", color.getValue().hashCode());
        if (colorString.length() > 7) {
            colorString = colorString.substring(0, 7);
        }
        String modeString = mode.isSelected() ? "ParticleEmitter.MODE_SINGLE" : "ParticleEmitter.MODE_CONTINUOUS";
        text.setText(String.format("new ParticleEmitter(new Vector2D(), %d, %d, %d, %s, %d, Color.web(\"%s\"));", (int) direction.getValue(), (int) spread.getValue(), (int) size.getValue(), modeString, (int) life.getValue(), colorString));
    }
}