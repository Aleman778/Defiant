package ga.devkit.ui;

import ga.engine.core.Application;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class Core extends Interface implements Initializable {

    @FXML
    public TabPane centerContent;
    public SplitPane rightContent;
    public SplitPane bottomContent;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Level Editor
        LevelEditor editor = new LevelEditor();
        editor.load();
        Tab tab = new Tab("Level Editor", editor.root);
        centerContent.getTabs().add(tab);
        
        //Project View
        ProjectView project = new ProjectView();
        project.load();
        bottomContent.getItems().add(project.root);
        bottomContent.getItems().add(project.content);
        bottomContent.getItems().add(project.preview.root);
        bottomContent.setDividerPositions(0.2, 0.8);
        SplitPane.setResizableWithParent(project.root, false);
    }
    
    @FXML
    public void exitDevkit() {
        Application.setDevmode(false);
    }
}