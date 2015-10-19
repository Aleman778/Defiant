package ga.devkit.ui;

import ga.devkit.editor.EditorObject;
import ga.engine.scene.GameObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class LevelEditor extends Interface implements Initializable {

    @FXML
    public Canvas grid;
    public Group content;
    public AnchorPane canvas;
    
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

    public void updateCanvas() {
        grid.setWidth(width);
        grid.setHeight(height);
        if (showGrid) {
            g.setFill(new Color(0.0, 0.0, 0.0, 0.2));
            for (int i = 1; i < width / tileSize; i++) {
                g.fillRect(i * tileSize, 0, 1, height);
            }
            for (int j = 1; j < height / tileSize; j++) {
                g.fillRect(0, j * tileSize, width, 1);
            }
        }
    }
    
    public void render() {
        content.getChildren().clear();
        for (GameObject object: rootObject.getChildren()) {
            //((EditorObject) object).render(content);
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
        updateCanvas();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height / tileSize * tileSize;
        updateCanvas();
    }
    
    public void setSize(int width, int height) {
        this.width = width / tileSize * tileSize;
        this.height = height / tileSize * tileSize;
        updateCanvas();
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
        width = width / this.tileSize * this.tileSize;
        height = height / this.tileSize * this.tileSize;
        updateCanvas();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = grid.getGraphicsContext2D();
        setSize(2000, 2000);
        
        canvas.setOnMousePressed((MouseEvent event) -> {
            selectedObject = new EditorObject(event.getX(), event.getY(), 0);
            Node node = selectedObject.getNode();
            node.setTranslateX(selectedObject.transform.position.x);
            node.setTranslateY(selectedObject.transform.position.y);
            canvas.getChildren().add(selectedObject.getNode());
        });
        canvas.setOnMouseDragged((MouseEvent event) -> {
            Node node = selectedObject.getNode();
            node.setTranslateX(selectedObject.transform.position.x + event.getX() - selectedObject.transform.position.x);
            node.setTranslateY(selectedObject.transform.position.y + event.getY() - selectedObject.transform.position.y);
        });
        canvas.setOnMouseReleased((MouseEvent event) -> {
            rootObject.addChild(new EditorObject((int) event.getX() / tileSize * tileSize, (int) event.getY() / tileSize * tileSize, 0));
            canvas.getChildren().remove(selectedObject.getNode());
            selectedObject = null;
            render();
        });
    }
}