package ga.devkit.editor;

import ga.engine.physics.Vector3D;
import ga.engine.rendering.Renderable;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

public class EditorObject extends GameObject {
    
    private static Image NONE_RENDERABLE_IMAGE = new Image("ga/game/grass_tile.png");
    private Node node = null;

    public EditorObject(Vector3D position, Vector3D rotation, Vector3D scale) {
        super(position, rotation, scale);
        setDefaultNode();
    }

    public EditorObject(double x, double y, double z) {
        this(new Vector3D(x, y, z), new Vector3D(), new Vector3D(1, 1, 1));
    }

    public EditorObject() {
        this(new Vector3D(), new Vector3D(), new Vector3D(1, 1, 1));
    }
    
    @Override
    public GameObject addComponent(GameComponent component) {
        super.addComponent(component);
        if (component instanceof Renderable) {
            setNode(((Renderable) component).getNode());
        }
        
        return this;
    }
    
    private void setNode(Node node) {
        this.node = node;
        this.node.setOnMousePressed((MouseEvent event) -> {
            System.out.println("Pressed on node!");
        });
    }
    
    private void setDefaultNode() {
        setNode(new ImageView(NONE_RENDERABLE_IMAGE));
    }
    
    public Node getNode() {
        return node;
    }

    @Override
    public void render(Group group) {
        group.getChildren().add(node);
        node.getTransforms().clear();
        Vector3D rotation = transform.localRotation();
        Vector3D position = transform.localPosition();
        Rotate rx = new Rotate(rotation.x, 0, 0, 0, Rotate.X_AXIS);
        Rotate ry = new Rotate(rotation.y, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rz = new Rotate(rotation.z, 0, 0, 0, Rotate.Z_AXIS);
        node.getTransforms().addAll(rx, ry, rz);
        node.setTranslateX((int) position.x);
        node.setTranslateY((int) position.y);
        node.setTranslateZ((int) position.z);
        node.setScaleX(transform.scale.x);
        node.setScaleY(transform.scale.y);
        node.setScaleZ(transform.scale.z);
        
        for (GameObject child : getChildren()) {
            ((EditorObject) child).render(group);
        }
    }
}
