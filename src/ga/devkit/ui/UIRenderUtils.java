package ga.devkit.ui;

import com.sun.javafx.geom.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class UIRenderUtils {
    
    public static void renderGrid(GraphicsContext g, int width, int height, int tileSize) {
        g.setFill(new Color(1.0, 1.0, 1.0, 0.4));
        for (int i = 1; i < width / tileSize; i++) {
            g.fillRect(i * tileSize, 0, 1, height);
        }
        for (int j = 1; j < height / tileSize; j++) {
            g.fillRect(0, j * tileSize, width, 1);
        }
    }
    
    public static void renderSelection(GraphicsContext g, Rectangle selection) {
        g.setLineJoin(StrokeLineJoin.MITER);
        g.setLineCap(StrokeLineCap.SQUARE);
        g.setLineDashes(6.0);
        
        g.setFill(Color.rgb(67, 141, 215, 0.1));
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
    
    public static void renderResizeSelection(GraphicsContext g, Rectangle selection) {
        renderDot(g, selection.x, selection.y);
        renderDot(g, selection.x + selection.width / 2, selection.y);
        renderDot(g, selection.x + selection.width, selection.y);
        renderDot(g, selection.x, selection.y + selection.height / 2);
        renderDot(g, selection.x, selection.y + selection.height);
        renderDot(g, selection.x + selection.width / 2, selection.y + selection.height);
        renderDot(g, selection.x + selection.width, selection.y + selection.height / 2);
        renderDot(g, selection.x + selection.width, selection.y + selection.height);
    }
    
    public static void renderDot(GraphicsContext g, int x, int y) {
        g.setFill(Color.rgb(67, 141, 215, 1));
        g.fillRect(x - 3, y - 3, 6, 6);
        g.setFill(Color.WHITE);
        g.fillRect(x - 2, y - 2, 4, 4);
    }
    
    public static void renderCheckerboard(GraphicsContext g) {
        for (int i = 0; i < g.getCanvas().getWidth() / 16; i++) {
            for (int j = 0; j < g.getCanvas().getHeight() / 16; j++) {
                if (i % 2 + j % 2 == 1) {
                    g.setFill(new Color(0.25, 0.25, 0.25, 1.0));
                } else {
                    g.setFill(new Color(0.32, 0.32, 0.32, 1.0));
                }
                g.fillRect(i * 16, j * 16, 16, 16);
            }
        }
    }
}
