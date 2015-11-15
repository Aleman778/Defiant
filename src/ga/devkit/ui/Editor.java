package ga.devkit.ui;

import java.io.File;
import javafx.scene.control.SplitPane;

public interface Editor {
    
    public File getFile();
    public void save();
    public void rightContent(SplitPane sidebar);
}
