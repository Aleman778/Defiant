package ga.devkit.ui;

import com.sun.javafx.geom.Rectangle;
import ga.devkit.editor.EditorComponent;
import ga.devkit.editor.EditorObject;
import ga.devkit.editor.EditorTile;
import ga.devkit.editor.SelectionType;
import ga.devkit.editor.Tileset;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.resource.ResourceManager;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class AssetPreview extends Interface implements Initializable {
    
    public GraphicsContext g = null;
    public String type;
    @FXML
    public Label title;
    public Canvas canvas;
    
    private int x, y;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = canvas.getGraphicsContext2D();
        title.setText("");
    }
    
    public void refreshFile(File file, String resfilepath) {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setOnMousePressed(null);
        canvas.setOnMouseReleased(null);
        canvas.setOnMouseDragged(null);
        SceneEditor.PLACEMENT.clear();
        type = "File";
        
        if (file.isDirectory()) {
            type = "Directory";
        } else {
            switch (Core.getExtension(file.getName())) {
                case "png": case "jpg": case "gif":
                    Image image = ResourceManager.getImage(resfilepath);
                    canvas.setWidth(image.getWidth());
                    canvas.setHeight(image.getHeight());
                    UIRenderUtils.renderCheckerboard(g);
                    g.drawImage(image, 0, 0);

                    EditorObject object = new EditorObject(Core.getFilename(file.getName()));
                    ImageRenderer renderer = new ImageRenderer(image);
                    object.addComponent(new EditorComponent(renderer));
                    object.setAABB(0, 0, (int) image.getWidth(), (int) image.getHeight());
                    SceneEditor.PLACEMENT.setSelectionType(SelectionType.PLACEMENT);
                    SceneEditor.PLACEMENT.addObject(object);
                    System.out.println(SceneEditor.PLACEMENT.getObjects().size());
                    type = "Image";
                    break;
                case "tileset":
                    Tileset tileset = new Tileset(file);
                    drawTileset(tileset, 0, 0, 1, 1);

                    EditorTile baseTile = new EditorTile(tileset.getPath(), 0, 0, 0, tileset.width, tileset.height, new Vector2D());
                    SceneEditor.PLACEMENT.setSelectionType(SelectionType.PLACEMENT);
                    SceneEditor.PLACEMENT.addTile(baseTile);
                    canvas.setOnMousePressed((MouseEvent event) -> {
                        x = (int) (event.getX() / tileset.getWidth());
                        y = (int) (event.getY() / tileset.getHeight());
                        drawTileset(tileset, (int) (event.getX() / tileset.getWidth()), (int) (event.getY() / tileset.getHeight()), 1, 1);
                    });
                    canvas.setOnMouseDragged((MouseEvent event) -> {
                        int minX = Math.min(x, (int) (event.getX() / tileset.getWidth()));
                        int minY = Math.min(y, (int) (event.getY() / tileset.getHeight()));
                        int maxX = Math.max(x, (int) (event.getX() / tileset.getWidth())) + 1;
                        int maxY = Math.max(y, (int) (event.getY() / tileset.getHeight())) + 1;
                        drawTileset(tileset, minX, minY, maxX - minX, maxY - minY);
                    });
                    canvas.setOnMouseReleased((MouseEvent event) -> {
                        int minX = Math.min(x, (int) (event.getX() / tileset.getWidth()));
                        int minY = Math.min(y, (int) (event.getY() / tileset.getHeight()));
                        int maxX = Math.max(x, (int) (event.getX() / tileset.getWidth())) + 1;
                        int maxY = Math.max(y, (int) (event.getY() / tileset.getHeight())) + 1;
                        EditorTile tile = new EditorTile(tileset.getPath(), 0, minX * tileset.width, minY * tileset.height,
                                (maxX - minX) * tileset.width, (maxY - minY) * tileset.height, new Vector2D());
                        SceneEditor.PLACEMENT.clear();
                        SceneEditor.PLACEMENT.setSelectionType(SelectionType.PLACEMENT);
                        SceneEditor.PLACEMENT.addTile(tile);
                    });
                    type = "Tileset";
                    break;
                case "blueprint":
                    type = "Blueprint";
                    break;
                case "mp3": case "m4a": case "waw": case "midi":
                    type = "Audio";
                    break;
            }
        }
        
        title.setText(type + ": " + file.getName());
    }
    
    public void drawTileset(Tileset tileset, int tileX, int tileY, int tileW, int tileH) {
        canvas.setWidth(tileset.getTilesheet().getWidth());
        canvas.setHeight(tileset.getTilesheet().getHeight());
        UIRenderUtils.renderCheckerboard(g);
        g.drawImage(tileset.getTilesheet(), 0, 0);
        g.setFill(new Color(1.0, 1.0, 1.0, 0.5));
        for (int i = 1; i < tileset.getTilesheet().getWidth() / tileset.getWidth(); i++) {
            g.fillRect(i * tileset.getWidth(), 0, 1, tileset.getTilesheet().getHeight());
        }
        for (int j = 1; j < tileset.getTilesheet().getHeight() / tileset.getHeight(); j++) {
            g.fillRect(0, j * tileset.getHeight(), tileset.getTilesheet().getWidth(), 1);
        }
        UIRenderUtils.renderSelection(g, new Rectangle(tileX * tileset.getWidth() + 1, tileY * tileset.getHeight() + 1,
                tileW * tileset.getWidth() - 2, tileH * tileset.getHeight() - 2));
    }
    
    public boolean isReady() {
        return (g != null);
    }
}
