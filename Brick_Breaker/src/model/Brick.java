package model;

import javax.swing.JButton;

/**
 * This class represents a brick.
 * @author Sundorius
 */
public class Brick extends JButton
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1417304537087736135L;
	private int xCoo, yCoo; /* Coordinates of a Brick. */
    private String bName = new String();
    
    
    /**
     * <br>Constructor
     * <br>Precondition: Arguments x,y values have to be greater than 0.
     * <br>Postcondition: A new custom Brick of type button is made.
     * @param x x coordinate of brick
     * @param y y coordinate of brick  
     * @param bName name of the brick
     */
    public Brick(int x, int y, String bName)
    {
        this.xCoo = x;
        this.yCoo = y;
        this.bName = bName;
    }

    
    /**
     * <br>Transformer: Sets the x coordinate of the brick.
     * <br>Precondition: Argument has to be of type int.
     * <br>Postcondition: The x coordinate of the brick has been set.
     * @param x x coordinate of the brick
     */
    public void setXCoo(int x)
    {
        this.xCoo = x;
    }
    
    
    /**
     * <br>Accessor: Returns the x coordinate of the brick.
     * <br>Postcondition: The x coordinate of the brick has been returned.
     * @return x coordinate of the brick
     */
    public int getXCoo()
    {
        return this.xCoo;
    }
    
    
   /**
     * <br>Transformer: Sets the y coordinate of the brick.
     * <br>Precondition: Argument has to be of type int.
     * <br>Postcondition: The y coordinate of the brick has been set.
     * @param y y coordinate of the brick
     */
    public void setYCoo(int y)
    {
        this.yCoo = y;
    }
    
    /**
     * <br>Accessor: Returns the y coordinate of the brick.
     * <br>Postcondition: The y coordinate of the brick has been returned.
     * @return y coordinate of the brick
     */
    public int getYCoo()
    {
        return this.yCoo;
    }

    /**
     * <br>Transformer: Sets the name of the brick.
     * <br>Precondition: Argument has to be of type String.
     * <br>Postcondition: The name of the brick has been set.
     * @param name name of the brick
     */
    public void setBname(String name)
    {
        this.bName = name;
    }

    /**
     * <br>Accessor: Returns the bricks's name.
     * <br>Postcondition: The name of the brick has been returned.
     * @return name of the brick
     */
    public String getBname()
    {
        return this.bName;
    }
}