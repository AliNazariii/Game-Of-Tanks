package game.elements;

import game.Utils.Utility;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * each object in the game like tank or bullets and ... has a location(x,y)
 * and certain velocity at X,Y Dimensions.
 */
public class GameObject {
    //fields
    protected ObjectId id;
    protected double x;
    protected double y;
    protected int velX, velY;
    protected BufferedImage texture;


    //constructor
    public GameObject(){

    }
    public GameObject(double x, double y, ObjectId id, String path)
    {
        this.x = x;
        this.y = y;
        velX = 0;
        velY = 0;
        this.id = id;
        this.texture = Utility.loadBufferedImage(path);
    }

    //methods

    /**
     * set an id for game object
     */
    public void setId(ObjectId id)
    {
        this.id = id;
    }

    /**
     * set a X location for object
     *
     * @param x
     */
    public void setX(double x)
    {
        this.x = x;
    }

    /**
     * set a Y location for object
     *
     * @param y
     */
    public void setY(double y)
    {
        this.y = y;
    }

    /**
     * set horizontal velocity for object
     *
     * @param velX
     */
    public void setVelX(int velX)
    {
        this.velX = velX;
    }

    /**
     * set vertical velocity for object
     *
     * @param velY
     */
    public void setVelY(int velY)
    {
        this.velY = velY;
    }

    /**
     * return the id of the object
     *
     * @return Object' id
     */
    public ObjectId getId()
    {
        return id;
    }

    /**
     * returns x
     *
     * @return X location of object
     */
    public double getX()
    {
        return x;
    }

    /**
     * returns y
     *
     * @return Y location of object
     */
    public double getY()
    {
        return y;
    }

    /**
     * velocity at x direction
     *
     * @return horizontal velocity
     */
    public int getVelX()
    {
        return velX;
    }

    /**
     * velocity at y direction
     *
     * @return vertical velocity
     */
    public int getVelY()
    {
        return velY;
    }

    public BufferedImage getTexture()
    {
        return texture;
    }

    public void setTexture(BufferedImage texture)
    {
        this.texture = texture;
    }

    public Rectangle getBounds()
    {
        return new Rectangle((int) x, (int) y, 100, 100);
    }
}
