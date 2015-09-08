package ga.engine.physics;

public class Vector3D {
    
    public double x = 0, y = 0, z = 0;

    public Vector3D(double dX, double dY, double dZ) {
        this.x = dX;
        this.y = dY;
        this.z = dZ;
    }
    
    public Vector3D() {}
    
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    public Vector3D add(Vector3D vector) {
        return new Vector3D(x + vector.x, y + vector.y, z + vector.z);
    }
    
    public Vector3D sub(Vector3D vector) {
        return new Vector3D(x - vector.x, y - vector.y, z - vector.z);
    }
    
    public Vector3D scale(double scale) {
        return new Vector3D(x * scale, y * scale, z * scale);
    }

    @Override
    public String toString() {
        return "[x = " + x + ", y = " + y + ", z = " + z + "]";
    }
}
