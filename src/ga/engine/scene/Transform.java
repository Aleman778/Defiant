package ga.engine.scene;

import ga.engine.physics.Vector2D;
import ga.engine.physics.Vector3D;

public class Transform {
    
    public GameObject gameobject;
    public Vector3D position;
    public Vector3D rotation;
    public Vector3D scale;
    
    public Transform(GameObject object, Vector3D position, Vector3D rotation, Vector3D scale) {
        this.gameobject = object;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }
    
    public Transform(GameObject object, double x, double y, double z) {
        this(object, new Vector3D(x, y, z), new Vector3D(), new Vector3D(1, 1, 1));
    }
    
    public Transform(GameObject object, Transform transform) {
        this(object, transform.position, transform.rotation, transform.scale);
    }
    
    public Transform(GameObject object) {
        this(object, new Vector3D(), new Vector3D(), new Vector3D(1, 1, 1));
    }
    
    public void translate(Vector3D vector3) {
        position = position.add(vector3);
    }
    
    public void translate(double x, double y, double z) {
        translate(new Vector3D(x, y, z));
    }
    
    public void rotate(Vector3D vector3) {
        rotation = rotation.add(vector3);
    }
    
    public void rotate(double x, double y, double z) {
        rotate(new Vector3D(x, y, z));
    }
    
    public void scale(Vector3D vector3) {
        scale = scale.add(vector3);
    }
    
    public void scale(double x, double y, double z) {
        scale(new Vector3D(x, y, z));
    }
    
    public Vector3D position2D() {
        return new Vector3D(position.x, position.y, 0.0);
    }
    
    public Vector2D toVector2D() {
        return new Vector2D(position.x, position.y);
    }
    
    public Vector3D localPosition() {
        GameObject parent = gameobject.getParent();
        if (parent != null)
            return parent.getTransform().localPosition().add(position);
        return position;
    }
    
    public Vector3D localRotation() {
        GameObject parent = gameobject.getParent();
        if (parent != null)
            return parent.getTransform().localRotation().add(rotation);
        return rotation;
    }
}
