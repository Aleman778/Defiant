package ga.devkit.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class AssetPreview extends Interface implements Initializable {
    
    public GraphicsContext g = null;
    public String type;
    @FXML
    public Label title;
    public Canvas canvas;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = canvas.getGraphicsContext2D();
        title.setText("");
    }
    
    public void refreshFile(File file) {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        type = "File";
        switch (Core.getExtension(file.getName())) {
            case "png": case "jpg": case "gif":
                Image image = new Image("file:" + file.getPath());
                canvas.setWidth(image.getWidth());
                canvas.setHeight(image.getHeight());
                g.drawImage(image, 0, 0);
                type = "Image";
                break;
            case "tileset":
//                Image image = new Image();
//                canvas.setWidth(image.getWidth());
//                canvas.setHeight(image.getHeight());
//                g.drawImage(image, 0, 0);
                type = "Tileset";
                break;
            case "object":
                type = "GameObject";
                break;
            case "mp3": case "m4a": case "waw": case "midi":
                type = "Audio";
                break;
        }
        
        title.setText(type + ": " + file.getName());
    }
    
    public boolean isReady() {
        return (g != null);
    }
}
