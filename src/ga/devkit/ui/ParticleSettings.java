package ga.devkit.ui;

import com.sun.javafx.geom.Rectangle;
import ga.engine.rendering.ParticleConfiguration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

public class ParticleSettings extends Interface implements Initializable {

    private Editor editor;

    @FXML
    public Slider direction;
    public Slider spread;
    public Slider size;
    public Slider sizeEnd;
    public Slider sizeStep;
    public ColorPicker color;
    public CheckBox mode;
    public Slider gravity;
    public Slider velocity;
    public Slider velocityStep;
    public Slider life;
    public Slider rate;
    public RadioButton point;
    public RadioButton line;
    public RadioButton area;

    public ParticleSettings(Editor editor) {
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

    public Slider getSizeEnd() {
        return sizeEnd;
    }

    public Slider getSizeStep() {
        return sizeStep;
    }

    public Slider getVelocity() {
        return velocity;
    }

    public Slider getVelocityStep() {
        return velocityStep;
    }

    public Slider getLife() {
        return life;
    }

    public Slider getRate() {
        return rate;
    }

    public RadioButton getPoint() {
        return point;
    }

    public RadioButton getLine() {
        return line;
    }

    public RadioButton getArea() {
        return area;
    }

    public void updateSliders(ParticleConfiguration c) {
        color.setValue(Color.web(c.getValue("color")));
        size.setValue(Double.valueOf(c.getValue("size")));
        sizeEnd.setValue(Double.valueOf(c.getValue("sizeEnd")));
        sizeStep.setValue(Double.valueOf(c.getValue("sizeStep")));
        spread.setValue(Double.valueOf(c.getValue("spread")));
        direction.setValue(Double.valueOf(c.getValue("direction")));
        gravity.setValue(Double.valueOf(c.getValue("gravity")));
        mode.setSelected(c.getValue("mode").equals("MODE_SINGLE"));
        life.setValue(Double.valueOf(c.getValue("life")));
        velocity.setValue(Double.valueOf(c.getValue("velocity")));
        velocityStep.setValue(Double.valueOf(c.getValue("velocityStep")));
        rate.setValue(Double.valueOf(c.getValue("rate")));
    }

    private void updatePreview() {
        ParticleConfiguration c = ((ParticleEditor)editor).getConfig();
        c.setValue("color", String.format("#%X", color.getValue().hashCode()));
        c.setValue("size", String.valueOf(size.getValue()));
        c.setValue("sizeEnd", String.valueOf(sizeEnd.getValue()));
        c.setValue("sizeStep", String.valueOf(sizeStep.getValue()));
        c.setValue("spread", String.valueOf(spread.getValue()));
        c.setValue("direction", String.valueOf(direction.getValue()));
        c.setValue("mode", mode.isSelected() ? "MODE_SINGLE" : "MODE_CONTINUOUS");
        c.setValue("gravity", String.valueOf(gravity.getValue()));
        c.setValue("velocity", String.valueOf(velocity.getValue()));
        c.setValue("velocityStep", String.valueOf(velocityStep.getValue()));
        c.setValue("life", String.valueOf(life.getValue()));
        c.setValue("rate", String.valueOf(rate.getValue()));
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
        sizeEnd.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        sizeStep.valueProperty().addListener((observable, newValue, oldValue) -> {
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
        life.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        velocity.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        velocityStep.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        rate.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        point.setOnAction((ActionEvent event) -> {
            if (point.isSelected()) {
                ((ParticleEditor) editor).emitter.setShape(new Rectangle(1, 1));
            }
        });
        line.setOnAction((ActionEvent event) -> {
            if (line.isSelected()) {
                ((ParticleEditor) editor).emitter.setShape(new Rectangle(-250, 0, 500, 1));
            }
        });
        area.setOnAction((ActionEvent event) -> {
            if (area.isSelected()) {
                ((ParticleEditor) editor).emitter.setShape(new Rectangle(-250, -250, 500, 500));
            }
        });
    }
}
