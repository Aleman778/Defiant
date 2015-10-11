package ga.devkit.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;

public class ProjectView extends Interface implements Initializable {
    
    @FXML
    public TreeView<String> tree = new TreeView<>();
    
    public FlowPane content;
    public AssetPreview preview;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        content = new FlowPane();
        preview = new AssetPreview();
        refreshTree();
    }

    @Override
    public void load() {
        super.load();
        preview.load();
    }
    
    public void refreshTree() {
        List<File> folders = findFolders(new ArrayList<>(), new File("res"));
        TreeItem<String> root = new TreeItem<>("res");
        for (File file: folders) {
            root.getChildren().add(new TreeItem<>(file.getName()));
        }
        tree.setRoot(root);
    }
    
    private List<File> findFiles(File file) {
        List<File> result = new ArrayList<>();
        File[] files = file.listFiles();
        if (files == null)
            return result;
        
        for (File f: files) {
            if (!f.isDirectory()) {
                result.add(f);
            }
        }
        return result;
    }
    
    private List<File> findFolders(List<File> result, File file) {
        File[] files = file.listFiles();
        if (files == null)
            return result;
        
        for (File f: files) {
            if (f.isDirectory()) {
                findFolders(result, f);
                result.add(f);
            }
        }
        return result;
    }
    
    private String getFilename(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }
}
