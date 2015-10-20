package ga.devkit.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FolderView extends Interface implements Initializable {

    @FXML
    public Label title;
    public String folder;
    public FlowPane content;
    
    public File[] refreshFiles(File folder) {
        if (!folder.isDirectory())
            return null;
        
        File[] result = folder.listFiles();
        if (result == null)
            return null;
        title.setText("Folder: " + folder.getPath());
        content.getChildren().clear();
        this.folder = folder.getPath();
        for (File file: result) {
            if (!file.isDirectory())
                addFile(file);
        }
        
        return result;
    }
    
    private void addFile(File file) {
        Node image = new Rectangle(64, 64, Color.GRAY);
        switch (Core.getExtension(file.getName())) {
            case "png": case "jpg": case "gif":
                image = new ImageView("file:" + file.getPath());
                ((ImageView) image).setViewport(new Rectangle2D(0, 0, 64, 64));
                break;
        }
        Label name = new Label(Core.getFilename(file.getName()));
        name.setMaxWidth(86);
        name.setAlignment(Pos.CENTER);
        VBox box = new VBox(image, name);
        box.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(2), new Insets(4))));
        box.setPrefWidth(96);
        box.setPrefHeight(96);
        box.setAlignment(Pos.CENTER);
        content.getChildren().add(box);
        box.setOnDragDetected((MouseEvent event) -> {
            final Dragboard db = box.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            List<File> files = new ArrayList<>();
            files.add(file);
            content.put(DataFormat.FILES, files);
            db.setContent(content);
            event.consume();
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshFiles(new File("res"));
    }
}
