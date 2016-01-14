package ga.devkit.ui;

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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ParticleSettings extends Interface implements Initializable {

    private Editor editor;

    @FXML
    public Slider direction, spread, size, sizeEnd, sizeStep, gravity, velocity, velocityMin, velocityMax, velocityStep, life, rate, colorPoint, random;
    public ColorPicker color, colorMid, colorEnd;
    public CheckBox mode, colorLock, interpolate;
    public RadioButton point, area, circle, square, sprite;
    public TextField areaBox;

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

    public Slider getVelocityMin() {
        return velocityMin;
    }

    public Slider getVelocityMax() {
        return velocityMax;
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

    public RadioButton getArea() {
        return area;
    }

    public RadioButton getCircle() {
        return circle;
    }

    public RadioButton getSquare() {
        return square;
    }

    public TextField getAreaBox() {
        return areaBox;
    }

    public Slider getColorPoint() {
        return colorPoint;
    }

    public ColorPicker getColorMid() {
        return colorMid;
    }

    public ColorPicker getColorEnd() {
        return colorEnd;
    }

    public CheckBox getColorLock() {
        return colorLock;
    }

    public CheckBox getInterpolate() {
        return interpolate;
    }

    public Slider getRandom() {
        return random;
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
        velocityMin.setValue(Double.valueOf(c.getValue("velocityMin")));
        velocityMax.setValue(Double.valueOf(c.getValue("velocityMax")));
        velocityStep.setValue(Double.valueOf(c.getValue("velocityStep")));
        rate.setValue(Double.valueOf(c.getValue("rate")));
        circle.setSelected(c.getValue("particleShape").equals("circle"));
        square.setSelected(c.getValue("particleShape").equals("square"));
        areaBox.setText(c.getValue("shape"));
        if (c.getValue("colorEnd").equals("")) {
            colorMid.setValue(color.getValue());
            colorEnd.setValue(color.getValue());
        } else {
            colorMid.setValue(Color.web(c.getValue("colorMid")));
            colorEnd.setValue(Color.web(c.getValue("colorEnd")));
        }
        colorPoint.setValue(Double.valueOf(c.getValue("colorPoint")));
        if (Integer.parseInt(areaBox.getText().split(",")[0].trim()) > 1 && Integer.parseInt(areaBox.getText().split(",")[1].trim()) > 1) {
            area.setSelected(true);
            areaBox.setDisable(false);
        } else {
            areaBox.setDisable(true);
        }
        random.setValue(Double.valueOf(c.getValue("random")));
    }

    public void updatePreview() {
        ParticleConfiguration c = ((ParticleEditor) editor).getConfig();
        if (colorLock.isSelected()) {
            colorEnd.setValue(color.getValue());
            colorMid.setValue(color.getValue());
        }
        c.setValue("color", String.format("#%X", color.getValue().hashCode()));
        c.setValue("size", String.valueOf(size.getValue()));
        c.setValue("sizeEnd", String.valueOf(sizeEnd.getValue()));
        c.setValue("sizeStep", String.valueOf(sizeStep.getValue()));
        c.setValue("spread", String.valueOf(spread.getValue()));
        c.setValue("direction", String.valueOf(direction.getValue()));
        c.setValue("mode", mode.isSelected() ? "MODE_SINGLE" : "MODE_CONTINUOUS");
        c.setValue("gravity", String.valueOf(gravity.getValue()));
        c.setValue("velocity", String.valueOf(velocity.getValue()));
        c.setValue("velocityMin", String.valueOf(velocityMin.getValue()));
        c.setValue("velocityMax", String.valueOf(velocityMax.getValue()));
        c.setValue("velocityStep", String.valueOf(velocityStep.getValue()));
        c.setValue("life", String.valueOf(life.getValue()));
        c.setValue("rate", String.valueOf(rate.getValue()));
        c.setValue("particleShape", square.isSelected() ? "square" : "circle");
        c.setValue("shape", String.valueOf(areaBox.getText()));
        c.setValue("colorMid", String.format("#%X", colorMid.getValue().hashCode()));
        c.setValue("colorEnd", String.format("#%X", colorEnd.getValue().hashCode()));
        c.setValue("colorPoint", String.valueOf(colorPoint.getValue()));
        c.setValue("random", String.valueOf(random.getValue()));
        ((ParticleEditor) editor).updateConfig(c);
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
        velocityMin.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        velocityMax.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        velocityStep.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        rate.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        point.setOnAction((ActionEvent event) -> {
            areaBox.setText("1, 1");
            areaBox.setDisable(true);
            updatePreview();
        });
        area.setOnAction((ActionEvent event) -> {
            areaBox.setText("50, 50");
            areaBox.setDisable(false);
            updatePreview();
        });
        areaBox.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
        circle.setOnAction((ActionEvent event) -> {
            ((ParticleEditor) editor).emitter.setSprite(null);
            updatePreview();
        });
        square.setOnAction((ActionEvent event) -> {
            ((ParticleEditor) editor).emitter.setSprite(null);
            updatePreview();
        });
        sprite.setOnAction((ActionEvent event) -> {
            ((ParticleEditor) editor).emitter.setSprite(new Image(ParticleSettings.class.getResource("/ga/devkit/editor/nonrenderable.png").toExternalForm()));
        });
        colorPoint.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
        colorMid.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
        colorEnd.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
        colorLock.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
        mode.setOnAction((ActionEvent event) -> {
            updatePreview();
        });
        random.valueProperty().addListener((observable, newValue, oldValue) -> {
            updatePreview();
        });
    }
}
