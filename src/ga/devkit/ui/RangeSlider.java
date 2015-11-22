package ga.devkit.ui;

import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;

public class RangeSlider extends StackPane {

    private Slider minSlider;
    private Slider maxSlider;
    
    public RangeSlider(double min, double max, double minValue, double maxValue) {
        minSlider = new Slider(min, max, minValue);
        minSlider.getStyleClass().add("slider-min");
        maxSlider = new Slider(min, max, maxValue);
        maxSlider.getStyleClass().add("slider-max");
        getChildren().addAll(minSlider, maxSlider);
    }
    
    public RangeSlider() {
        this(0.0, 1.0, 0.0, 1.0);
    }
}
