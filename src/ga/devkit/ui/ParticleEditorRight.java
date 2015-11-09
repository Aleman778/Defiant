package ga.devkit.ui;

import ga.engine.rendering.ParticleConfiguration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class ParticleEditorRight extends Interface implements Initializable {

    private Editor editor;

    @FXML
    private Slider direction;
    @FXML
    private Slider spread;
    @FXML
    private Slider size;
    @FXML
    private ColorPicker color;
    @FXML
    private CheckBox mode;
    @FXML
    private Slider gravity;

    public ParticleEditorRight(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public Slider getDirection() {
        return direction;
    }

    public Slider getSpread() {
        return spread;
    }

    public Slider getSize() {
        return size;
    }

    public ColorPicker getColor() {
        return color;
    }

    public CheckBox getMode() {
        return mode;
    }

    public Slider getGravity() {
        return gravity;
    }

    public void updateSliders(ParticleConfiguration c) {
        color.setValue(Color.web(c.getValue("color")));
        size.setValue(Double.valueOf(c.getValue("size")));
        spread.setValue(Double.valueOf(c.getValue("spread")));
        direction.setValue(Double.valueOf(c.getValue("direction")));
        gravity.setValue(Double.valueOf(c.getValue("gravity")));
        mode.setSelected(c.getValue("mode").equals("MODE_SINGLE"));
    }

    private void updatePreview() {
        ParticleConfiguration c = ((ParticleEditor)editor).getConfig();
        c.setValue("color", String.format("#%X", color.getValue().hashCode()));
        c.setValue("size", String.valueOf(size.getValue()));
        c.setValue("spread", String.valueOf(spread.getValue()));
        c.setValue("direction", String.valueOf(direction.getValue()));
        c.setValue("mode", mode.isSelected() ? "MODE_SINGLE" : "MODE_CONTINUOUS");
        c.setValue("gravity", String.valueOf(gravity.getValue()));
//       c.setValue("life", String.valueOf(life.getValue()));
        ((ParticleEditor)editor).updateConfig(c);
    }
    
    public void initEvents() {
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
}
