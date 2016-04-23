package ga.devkit.ui;

import com.sun.javafx.geom.Dimension2D;
import com.sun.javafx.geom.Rectangle;
import ga.engine.core.Application;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class Core extends Interface implements Initializable {

    @FXML
    public TabPane centerContent;
    public SplitPane rightContent;
    public SplitPane bottomContent;
    
    private static ProjectView project;
    private HashMap<String, Editor> editors;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editors = new HashMap<>();
        
        //Project View
        project = new ProjectView();
        project.load();
        bottomContent.getItems().add(project.root);
        SplitPane.setResizableWithParent(project.root, false);
        bottomContent.getItems().add(project.folder.root);
        SplitPane.setResizableWithParent(project.folder.root, false);
        bottomContent.getItems().add(project.preview.root);
        bottomContent.setDividerPositions(0.2, 0.7);
        SplitPane.setResizableWithParent(project.root, false);
        
        //Events
        centerContent.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
            rightContent.getItems().clear();
            try {
                editors.get(newValue.getText()).rightContent(rightContent);
            } catch (Exception e) {
            }
        });
        centerContent.getTabs().addListener((ListChangeListener.Change<? extends Tab> c) -> {
            c.next();
            for (Tab tab: c.getRemoved()) {
                removeEditor(tab.getText());
            }
        });
        centerContent.setOnDragDropped((DragEvent event) -> {
            if (event.getDragboard().hasContent(DataFormat.FILES)) {
                File file = event.getDragboard().getFiles().get(0);
                if (file.exists()) {
                    openFile(file);
                }
            }
        });
        centerContent.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasContent(DataFormat.FILES)) {
                File file = event.getDragboard().getFiles().get(0);
                if (file.exists()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            }
        });
    }
    
    public void openFile(File file) {
        Interface editor;
        switch (getExtension(file.getName())) {
            case "scene":
                editor = new SceneEditor(file);
                editor.load();
                addEditor(editor, file.getName());
                break;
            case "psystem":
                editor = new ParticleEditor(new Dimension2D((float) centerContent.getWidth(), (float) centerContent.getHeight() - 32), file);
                editor.load();
                addEditor(editor, file.getName());
                break;
            default:
                break;
        }
    }
    
    public void addEditor(Interface editor, String name) {
        if (!(editor instanceof Editor) || editors.containsKey(name))
            return;
        
        editors.put(name, (Editor) editor);
        Tab tab = new Tab(name);
        tab.setContent(editor.root);
        centerContent.getTabs().add(tab);
    }
    
    public void removeEditor(String name) {
        editors.remove(name);
    }
    
    @FXML
    public void menuitemNew() {
    }
    
    @FXML
    public void menuitemOpen() {
    }
    
    @FXML
    public void menuitemSave() {
        editors.get(centerContent.getSelectionModel().getSelectedItem().getText()).save();
    }
    
    @FXML
    public void menuitemSaveAll() {
        for (Editor e : editors.values()) {
            e.save();
        }
    }
    
    @FXML
    public void menuitemExit() {
        Application.setDevmode(false);
    }
    
    public static String getExtension(String filename) {
        int index = filename.lastIndexOf(".");
        if (index < 0)
            return "";
        return filename.substring(index + 1);
    }
    
    public static String getFilename(String filename) {
        int index = filename.lastIndexOf(".");
        if (index < 0)
            return filename;
        return filename.substring(0, index);
    }
    
    public static boolean rectIntersection(Rectangle A, Rectangle B) {
        return !(A.x + A.width - 1 < B.x || 
                 A.y + A.height - 1 < B.y || 
                 A.x > B.x + B.width - 1 || 
                 A.y > B.y + B.height - 1);
    }
    
    public static ProjectView getProject() {
        return project;
    }
}
