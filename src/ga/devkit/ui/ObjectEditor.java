package ga.devkit.ui;

import ga.devkit.editor.EditorObject;
import ga.engine.scene.GameComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class ObjectEditor extends Interface implements Initializable {

    @FXML
    public Label title;
    public Label componentTitle;
    public AnchorPane componentPane;
    public SplitPane content;
    public Button makeBlueprint;
    public TreeView<String> components;
    public TreeItem<String> componentRoot;
    
    private EditorObject object;
    
    public ObjectEditor() {
        
    }
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        componentRoot = new TreeItem<>("Root");
        components.setRoot(componentRoot);
        components.showRootProperty().setValue(false);
        SplitPane.setResizableWithParent(componentPane, false);
        setObject(null);
    }
    
    public void setObject(EditorObject selection) {
        object = selection;
        clearComponents();
        if (object == null) {
            content.setDisable(true);
            makeBlueprint.setDisable(true);
            title.setText("Object Editor - No object selected");
        } else {
            content.setDisable(false);
            makeBlueprint.setDisable(false);
            title.setText("Object Editor - " + selection.getName());
            setComponents(object);
        }
    }
    
    private void clearComponents() {
        componentRoot.getChildren().clear();
    }
    
    private void setComponents(EditorObject object) {
        componentRoot.getChildren().add(new TreeItem<>("Transform2D"));
        for (GameComponent component: object.getAllComponents()) {
            String classname = component.getClass().getCanonicalName();
            componentRoot.getChildren().add(new TreeItem<>(classname.substring(classname.lastIndexOf(".") + 1)));
        }
    }
}
