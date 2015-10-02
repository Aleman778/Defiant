package ga.devkit.ui;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Interface {
    
    protected String fxml = "";
    public Parent root = null;
    public final String name;
    
    public Interface(String name) {
        this.name = name;
    }
    
    public final void load() {
        String classname = getClass().getCanonicalName();
        String location = "fxml/" + classname.substring(classname.lastIndexOf(".") + 1).toLowerCase() + ".fxml";
        System.out.println(location);
        FXMLLoader loader = new FXMLLoader(Interface.class.getResource(location));
        try {
            loader.setController(this);
            loader.load();
            root = (Parent) loader.getRoot();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
