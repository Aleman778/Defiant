package ga.devkit.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class AssetPreview extends Interface implements Initializable {
    
    public GraphicsContext g = null;
    @FXML
    public Canvas canvas;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = canvas.getGraphicsContext2D();
    }
    
    public boolean isReady() {
        return (g != null);
    }
}
