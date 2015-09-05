package ga.engine.physics;

public class Vector3D {
    
    public double dX = 0, dY = 0, dZ = 0;

    public Vector3D(double dX, double dY, double dZ) {
        this.dX = dX;
        this.dY = dY;
        this.dZ = dZ;
    }
    
    public Vector3D() {}
    
    public double length() {
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }
    
    public Vector3D add(Vector3D vector) {
        return new Vector3D(dX + vector.dX, dY + vector.dY, dZ + vector.dZ);
    }
    
    public Vector3D sub(Vector3D vector) {
        return new Vector3D(dX - vector.dX, dY - vector.dY, dZ - vector.dZ);
    }
    
    public Vector3D scale(double scale) {
        return new Vector3D(dX * scale, dY * scale, dZ * scale);
    }
}
