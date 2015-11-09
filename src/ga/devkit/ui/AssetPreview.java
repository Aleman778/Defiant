package ga.devkit.ui;

import com.sun.javafx.geom.Rectangle;
import ga.devkit.editor.Tileset;
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
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

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
    
    public void refreshFile(File file) {
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        type = "File";
        switch (Core.getExtension(file.getName())) {
            case "png": case "jpg": case "gif":
                Image image = new Image("file:" + file.getPath());
                canvas.setWidth(image.getWidth());
                canvas.setHeight(image.getHeight());
                drawCheckerboard();
                g.drawImage(image, 0, 0);
                type = "Image";
                break;
            case "tileset":
                Tileset tileset = new Tileset(file);
                drawTileset(tileset, 0, 0, 1, 1);
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
    
    public void drawTileset(Tileset tileset, int tileX, int tileY, int tileW, int tileH) {
        canvas.setWidth(tileset.getTilesheet().getWidth());
        canvas.setHeight(tileset.getTilesheet().getHeight());
        drawCheckerboard();
        g.drawImage(tileset.getTilesheet(), 0, 0);
        g.setFill(new Color(1.0, 1.0, 1.0, 0.5));
        for (int i = 1; i < tileset.getTilesheet().getWidth() / tileset.getWidth(); i++) {
            g.fillRect(i * tileset.getWidth(), 0, 1, tileset.getTilesheet().getHeight());
        }
        for (int j = 1; j < tileset.getTilesheet().getHeight() / tileset.getHeight(); j++) {
            g.fillRect(0, j * tileset.getHeight(), tileset.getTilesheet().getWidth(), 1);
        }
        renderSelection(new Rectangle(tileX * tileset.getWidth() + 1, tileY * tileset.getHeight() + 1,
                tileW * tileset.getWidth() - 2, tileH * tileset.getHeight() - 2));
    }
    
    public boolean isReady() {
        return (g != null);
    }
    
    private void renderSelection(Rectangle selection) {
        g.setLineJoin(StrokeLineJoin.MITER);
        g.setLineCap(StrokeLineCap.SQUARE);
        g.setLineDashes(6.0);
        
        g.setFill(Color.rgb(67, 141, 215, 0.5));
        g.fillRect(selection.x, selection.y, selection.width, selection.height);
        
        g.setStroke(Color.rgb(0, 0, 0, 1));
        g.setLineDashOffset(6.0);
        g.strokeLine(selection.x, selection.y, selection.x, selection.y + selection.height);
        g.strokeLine(selection.x, selection.y, selection.x + selection.width, selection.y);
        g.strokeLine(selection.x + selection.width, selection.y, selection.x + selection.width, selection.y + selection.height);
        g.strokeLine(selection.x, selection.y + selection.height, selection.x + selection.width, selection.y + selection.height);
        
        g.setStroke(Color.rgb(67, 141, 215, 1));
        g.setLineDashOffset(0.0);
        g.strokeLine(selection.x, selection.y, selection.x, selection.y + selection.height);
        g.strokeLine(selection.x, selection.y, selection.x + selection.width, selection.y);
        g.strokeLine(selection.x + selection.width, selection.y, selection.x + selection.width, selection.y + selection.height);
        g.strokeLine(selection.x, selection.y + selection.height, selection.x + selection.width, selection.y + selection.height);
    }
    
    private void drawCheckerboard() {
        for (int i = 0; i < canvas.getWidth() / 16; i++) {
            for (int j = 0; j < canvas.getHeight() / 16; j++) {
                if (i % 2 + j % 2 == 1) {
                    g.setFill(Color.DARKGRAY);
                } else {
                    g.setFill(Color.GRAY);
                }
                g.fillRect(i * 16, j * 16, 16, 16);
            }
        }
    }
}
