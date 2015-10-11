package ga.devkit.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FolderView extends Interface implements Initializable {

    @FXML
    public Label title;
    public FlowPane content;
    
    public File[] refreshFiles(File folder) {
        if (!folder.isDirectory())
            return null;
        
        File[] result = folder.listFiles();
        if (result == null)
            return null;
        title.setText("Folder: " + folder.getPath());
        content.getChildren().clear();
        for (File file: result) {
            addFile(file);
        }
        
        return result;
    }
    
    private void addFile(File file) {
        ImageView image = new ImageView(getClass().getResource("/textures/player/RED_Player.png").toExternalForm());
        Label name = new Label(getFilename(file.getName()));
        name.setMaxWidth(86);
        name.setAlignment(Pos.CENTER);
        VBox box = new VBox(image, name);
        box.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(2), new Insets(4))));
        box.setPrefWidth(96);
        box.setPrefHeight(96);
        box.setAlignment(Pos.CENTER);
        content.getChildren().add(box);
    }
    
    private String getExtension(String filename) {
        int index = filename.lastIndexOf(".");
        if (index < 0)
            return "";
        return filename.substring(index + 1);
    }
    
    private String getFilename(String filename) {
        int index = filename.lastIndexOf(".");
        if (index < 0)
            return filename;
        return filename.substring(0, index);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshFiles(new File("res"));
    }
}
