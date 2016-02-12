package ga.devkit.editor;

import ga.engine.resource.ResourceManager;
import ga.engine.xml.XMLReader;
import java.io.File;
import javafx.scene.image.Image;
import org.xml.sax.Attributes;

public class Tileset {
    
    public Image tilesheet;
    public String imagepath;
    public int offsetX, offsetY;
    public int width, height;
    
    public Tileset(File filepath) {
        XMLReader reader = new XMLReader() {

            @Override
            public void documentStart() {
            }

            @Override
            public void documentEnd() {
            }

            @Override
            public void nodeStart(String element, Attributes attri) {
            }

            @Override
            public void nodeEnd(String element, Attributes attri, String value) {
                switch (element) {
                    case "image":
                        tilesheet = ResourceManager.getImage(value);
                        imagepath = value;
                        break;
                    case "offsetX":
                        offsetX = Integer.parseInt(value);
                        break;
                    case "offsetY":
                        offsetY = Integer.parseInt(value);
                        break;
                    case "width":
                        width = Integer.parseInt(value);
                        break;
                    case "height":
                        height = Integer.parseInt(value);
                        break;
                }
            }
        };
        reader.parse(filepath);
    }

    public Image getTilesheet() {
        return tilesheet;
    }

    public void setTilesheet(Image tilesheet) {
        this.tilesheet = tilesheet;
    }
    
    public String getPath() {
        return imagepath;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
