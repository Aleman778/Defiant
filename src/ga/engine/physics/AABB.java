package ga.engine.physics;

/**
 * <b>Axis Aligned Bounding Box</b><br>
 * <i>added in version 1.0</i><br><br>
 * Use the AABB for simple and fast collision detection.<br><br>
 * <b><u>Performance tip:</u></b><br>
 * Check for colliding AABB (simplified collider) first then do another collision
 * check for the colliding objects with a more expensive and accurate
 * collision check.
 */
public class AABB {
 
    private double minX, minY, maxX, maxY;
    
    /**
     * <b>Constructor</b><br>
     * <i>added in version 1.0</i><br><br>
     * @param x The x component
     * @param y The y component
     * @param width The width of the box
     * @param height The height of the box
     */
    public AABB(double x, double y, double width, double height) {
        this.minX = x;
        this.minY = y;
        this.maxX = x + width;
        this.maxY = y + height;
    }
    
    /**
     * <b>Constructor</b><br>
     * <i>added in version 1.0</i>
     * @param x The x component
     * @param y The y component
     */
    public AABB(double x, double y) {
        this(x, y, 0, 0);
    }
    
    /**
     * <b>Constructor</b><br>
     * <i>added in version 1.0</i>
     */
    public AABB() {
        this(0, 0, 0, 0);
    }
    
    /**
     * <b>Contains</b><br>
     * <i>added in version 1.0</i><br><br>
     * Tests whether another AABB is completely contained by this one.
     * @param other the other bounding box
     * @return True if they are contained, false otherwise
     */
    public boolean contains(AABB other) {
        return getMinX()       <= other.getMaxX() &&
               other.getMaxX() <= getMaxX() &&
               getMinY()       <= other.getMaxY() &&
               other.getMaxY() <= getMaxY();
    }
    
    /**
     * <b>Intersect</b><br>
     * <i>added in version 1.0</i><br><br>
     * Tests whether another AABB is instersecting this one.
     * @param other the other bounding box
     * @return True if they are intersecting, false otherwise
     */
    public boolean intersect(AABB other) {
        return !(getMaxX() < other.getMinX() || 
                 getMaxY() < other.getMinY() || 
                 getMinX() > other.getMaxX() || 
                 getMinY() > other.getMaxY());
    }

    /**
     * <b>Move</b><br>
     * <i>added in version 1.0</i><br><br>
     * Moves the bounding box, adds the value to both min and max positions
     * @param x move in the x component
     * @param y move in the y component
     */
    public void move(double x, double y) {
        minX += x; maxX += x;
        minY += y; maxY += y;
    }
    
    /**
     * <b>Set Position</b><br>
     * <i>added in version 1.0</i><br><br>
     * Sets the position of the bounds
     * @param x The x component to move to
     * @param y The y component to move to
     */
    public void setPosition(double x, double y) {
        double w = getWidth();
        double h = getHeight();
        
        minX = x;
        minY = y;
        maxX = x + w;
        maxY = y + h;
    }
    
    /**
     * <b>Get Minumum X</b><br>
     * <i>added in version 1.0</i><br><br>
     * @return The Minumum x value
     */
    public double getMinX() {
        return minX;
    }

    /**
     * <b>Set Min X</b><br>
     * <i>added in version 1.0</i><br><br>
     * @param minX The Minumum x value
     */
    public void setMinX(double minX) {
        this.minX = minX;
    }
    
    /**
     * <b>Get Minumum Y</b><br>
     * <i>added in version 1.0</i><br><br>
     * @return The Minumum y value
     */
    public double getMinY() {
        return minY;
    }

    /**
     * <b>Set Min Y</b><br>
     * <i>added in version 1.0</i><br><br>
     * @param minY The Minumum y value
     */
    public void setMinY(double minY) {
        this.minY = minY;
    }
    
    /**
     * <b>Get Maximum X</b><br>
     * <i>added in version 1.0</i><br><br>
     * @return The Maximum x value
     */
    public double getMaxX() {
        return maxX;
    }
    
    /**
     * <b>Set Max X</b><br>
     * <i>added in version 1.0</i><br><br>
     * @param maxX The Maximum x value
     */
    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    /**
     * <b>Get Maximum Y</b><br>
     * <i>added in version 1.0</i><br><br>
     * @return The Maximum y value
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * <b>Set Max Y</b><br>
     * <i>added in version 1.0</i><br><br>
     * @param maxY The Maximum y value
     */
    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }
    
    /**
     * <b>Get Width</b><br>
     * <i>added in version 1.0</i><br><br>
     * @return The Width
     */
    public double getWidth() {
        return maxX - minX;
    }
    
    /**
     * <b>Get Height</b><br>
     * <i>added in version 1.0</i><br><br>
     * @return The Height
     */
    public double getHeight() {
        return maxY - minY;
    }
}
