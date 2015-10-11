package ga.devkit.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class AssetPreview extends Interface implements Initializable {
    
    @FXML
    public Canvas canvas;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas.setWidth(64);
        canvas.setHeight(64);
        canvas.getGraphicsContext2D().setFill(Color.RED);
        canvas.getGraphicsContext2D().fillRect(0, 0, 64, 64);
    }
    
}
