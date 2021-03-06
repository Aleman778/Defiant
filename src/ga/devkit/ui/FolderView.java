package ga.devkit.ui;

import ga.devkit.editor.Tileset;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
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
    public AssetPreview preview;
    public List<VBox> boxes;

    public FolderView(AssetPreview preview) {
        this.preview = preview;
        this.boxes = new ArrayList<>();
    }
    
    public File[] refreshFiles(File folder) {
        previewAsset(folder);
        if (!folder.isDirectory())
            return null;
        
        File[] result = folder.listFiles();
        if (result == null)
            return null;
        
        boxes.clear();
        title.setText("Folder: " + folder.getPath());
        this.folder = folder.getPath();
        content.getChildren().clear();
        for (File file: result) {
            if (!file.isDirectory())
                addFile(file);
        }
        
        return result;
    }
    
    private void addFile(File file) {
        Node image = new Rectangle(0, 0);
        switch (Core.getExtension(file.getName())) {
            case "png": case "jpg": case "gif":
                image = new ImageView("file:" + file.getPath());
                ((ImageView) image).setViewport(new Rectangle2D(0, 0, 64, 64));
                break;
            case "tileset":
                Tileset tileset = new Tileset(file);
                image = new ImageView(tileset.getTilesheet());
                image.setLayoutX(32 - Math.min(tileset.width, 64) / 2);
                image.setLayoutY(32 - Math.min(tileset.height, 64) / 2);
                ((ImageView) image).setViewport(new Rectangle2D(0, 0,
                        Math.min(tileset.width, 64), Math.min(tileset.height, 64)));
                break;
        }
        Label name = new Label(Core.getFilename(file.getName()));
        name.setMaxWidth(86);
        name.setAlignment(Pos.CENTER);
        VBox box = new VBox(image, name);
        box.setBackground(new Background(new BackgroundFill(new Color(0.22, 0.22, 0.22, 1.0), CornerRadii.EMPTY, new Insets(4))));
        box.setPrefWidth(96);
        box.setPrefHeight(96);
        box.setAlignment(Pos.CENTER);
        content.getChildren().add(box);
        box.setOnMousePressed((MouseEvent event) -> {
            for (VBox b: boxes) {
                b.setBackground(new Background(new BackgroundFill(new Color(0.22, 0.22, 0.22, 1.0), CornerRadii.EMPTY, new Insets(4))));
                b.setBorder(Border.EMPTY);
            }
            
            box.setBackground(new Background(new BackgroundFill(new Color(0.28, 0.28, 0.28, 1.0), CornerRadii.EMPTY, new Insets(4))));
            box.setBorder(new Border(new BorderStroke(new Color(0.1, 0.6, 0.9, 1.0), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.5), new Insets(2))));
            previewAsset(file);
        });
        box.setOnDragDetected((MouseEvent event) -> {
            final Dragboard db = box.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            List<File> files = new ArrayList<>();
            files.add(file);
            clipboardContent.put(DataFormat.FILES, files);
            db.setContent(clipboardContent);
            event.consume();
        });
        boxes.add(box);
    }
    
    public void previewAsset(File file) {
        if (!preview.isReady())
            return;
        
        preview.refreshFile(file, file.getPath().replace("\\", "/").substring(4));
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshFiles(new File("res"));
    }
}
