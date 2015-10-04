package ga.devkit.ui;

import ga.engine.core.Application;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Core extends Interface implements Initializable {

    @FXML
    public TabPane content;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LevelEditor editor = new LevelEditor();
        editor.load();
        Tab tab = new Tab("Level Editor", editor.root);
        content.getTabs().add(tab);
    }
    
    @FXML
    public void exitDevkit() {
        Application.setDevmode(false);
    }
}
