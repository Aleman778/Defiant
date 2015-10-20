package ga.devkit.ui;

import ga.devkit.editor.EditorObject;
import ga.engine.scene.GameObject;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class LevelEditor extends Interface implements Initializable {

    @FXML
    public Canvas canvas;
    public AnchorPane pane;
    
    private GraphicsContext g;
    private int width, height;
    private int tileSize;
    private boolean showGrid;
    private EditorObject rootObject;
    private EditorObject selectedObject;
    
    public LevelEditor() {
        this.width = 0;
        this.height = 0;
        this.tileSize = 32;
        this.showGrid = true;
        this.rootObject = new EditorObject();
        this.selectedObject = null;
    }
    
    public void render() {
        canvas.setWidth(width);
        canvas.setHeight(height);
        g.clearRect(0, 0, width, height);
        rootObject.render(g);
        
        if (showGrid)
            drawGrid();
    }
    
    private void drawGrid() {
        g.setFill(new Color(0.0, 0.0, 0.0, 0.1));
        for (int i = 1; i < width / tileSize; i++) {
            g.fillRect(i * tileSize, 0, 1, height);
        }
        for (int j = 1; j < height / tileSize; j++) {
            g.fillRect(0, j * tileSize, width, 1);
        }
    }
    
    public void addObject(EditorObject object) {
        object.addChild(object);
    }
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width / tileSize * tileSize;
        render();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height / tileSize * tileSize;
        render();
    }
    
    public void setSize(int width, int height) {
        this.width = width / tileSize * tileSize;
        this.height = height / tileSize * tileSize;
        render();
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
        width = width / this.tileSize * this.tileSize;
        height = height / this.tileSize * this.tileSize;
        render();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = canvas.getGraphicsContext2D();
        setSize(2000, 2000);
        render();
        
        pane.setOnMousePressed((MouseEvent event) -> {
            
        });
        pane.setOnMouseDragged((MouseEvent event) -> {
            
        });
        pane.setOnMouseReleased((MouseEvent event) -> {
            
        });
        pane.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasContent(DataFormat.FILES)) {
                File file = event.getDragboard().getFiles().get(0);
                if (file.exists()) {
                    String ext = Core.getExtension(file.getName());
                    event.acceptTransferModes(TransferMode.COPY);
                    if (ext.equals("object")) {
                        System.out.println("Object!?!?!??");
                    }
                }
            } else if (event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)) {
                
            }
        });
        pane.setOnDragDropped((DragEvent event) -> {
            if (event.isAccepted()) {
                
            }
        });
    }
    
    private String getExtension(String filename) {
        int index = filename.lastIndexOf(".");
        if (index < 0)
            return "";
        return filename.substring(index + 1);
    }
}