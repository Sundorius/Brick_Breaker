package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import model.Brick;

/**
 * This class represents the main screen of the game.
 * @author Sundorius
 */
public class GraphicUI extends JFrame
{
    private final HashMap<Integer, levelsColors> colors = new HashMap<>();
    private ArrayList<ArrayList<Brick>>  bricks;
    private JPanel panel;
    
    /**
     * <br>Constructor
     * <br>Precondition: Nothing.
     * <br>Postcondition: A new frame is opened in the center of the screen with bricks that have random colors.
     * @param rows the number of rows of bricks
     * @param cols the number of columns of bricks 
     * @param level the level of the fame
     */
    public GraphicUI(int rows, int cols, int level)
    {
        /* Game ends if rows equals -1. */
        if(rows==-1)
        {
            JLabel label = new JLabel();
            Border border = BorderFactory.createLineBorder(Color.BLACK);
            label.setBorder(border);
            label.setPreferredSize(new Dimension(150, 100));
            label.setText("You reached level "+level);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            
            
            this.setTitle("End of Game");
            this.setSize(150,100); 
            this.setLayout(new FlowLayout());
            this.add(label);
        }
        else
        {
            this.setTitle("Brick Breaker level "+level);
            this.bricks =  new ArrayList<ArrayList<Brick>>();
            this.panel = new JPanel(new GridLayout(rows,cols));
            this.setSize( (50*rows) , (29*cols) ); 
            this.Init(rows, cols, level);
            this.add(panel);
        }
        this.setResizable(false);
        this.setLocationRelativeTo(null); /* Open panel in center of the window.  */
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    
    /**
     * <br>Transformer: Initializes the colors array, and gives to each brick its coordinates and random color as well, it adds each brick to the panel.
     * <br>Postcondition: Each brick has its coordinates, color and is added to the panel.
     * @param row the number of rows of bricks
     * @param col the number of columns of bricks 
     * @param level the level of the fame
     */
    private void Init(int row, int col , int level)
    {
        int maxColors = 4 + (level-1)/2;
        int maxBricks = (row*col)/maxColors;
                
        this.colors.put(0, new levelsColors(maxBricks,Color.blue));
        this.colors.put(1, new levelsColors(maxBricks,Color.pink));
        this.colors.put(2, new levelsColors(maxBricks,Color.cyan));
        this.colors.put(3, new levelsColors(maxBricks,Color.yellow));
        this.colors.put(4, new levelsColors(maxBricks,Color.green));
        this.colors.put(5, new levelsColors(maxBricks,Color.magenta));
        this.colors.put(6, new levelsColors(maxBricks,Color.orange));
        this.colors.put(7, new levelsColors(maxBricks,Color.red));
        /*this.colors.put(8, new levelsColors(level,Color.black)); Black is only for the bomb brick so we keep track of it separately.  */
        
        int special_bricks_counter = (col*row)*5/100;
        int simple_bricks_counter = (row*col) - special_bricks_counter;
        int randomNum = 0;
        Random random = new Random();
        for(int i=0; i<row; i++)
        {
            ArrayList<Brick> yArray = new ArrayList<>();
            String name = new String();
            for(int j=0; j<col; j++)
            {
                boolean done = false;
                do
                {
                    /* If we are in the 2/3 of col and rows then make some bombs!  */
                    if( (j>(col*(2/3)) && i>(row*(2/3))) )
                    {
                        randomNum = random.nextInt(colors.size() - 0 + 1) + 0; /* Range is [0,10].  */
                        /* These numbers are for Simple bricks, as we need more simple bricks than bomb ones
                        /*     thats why we choose more numbers to represent a simple brick.  */
                        if(randomNum == 0 || randomNum == 2 || randomNum == 4 || 
                                randomNum == 5 || randomNum == 6 || randomNum == 7)
                        {
                            if(simple_bricks_counter>0)
                            {
                                name = "Simple";
                                simple_bricks_counter--;
                                done = true;
                            }

                        }
                        /* If special brick is choosen.  */
                        else
                        {
                            if(special_bricks_counter>0)
                            {
                                name = "Bomb";
                                special_bricks_counter--;
                                done = true;
                            }
                        }
                    }
                    else
                    {
                        randomNum = random.nextInt(colors.size() - 0 + 1) + 0; /* Range is [0,10].  */
                        /* These numbers are for Simple bricks, as we need more simple bricks than bomb ones
                        /*     thats why we choose more numbers to represent a simple brick.  */
                        if(randomNum == 0 || randomNum == 2 || randomNum == 4 || 
                                randomNum == 5 || randomNum == 6 || randomNum == 7)
                        {
                            if(simple_bricks_counter>0)
                            {
                                name = "Simple";
                                simple_bricks_counter--;
                                done = true;
                            }

                        }
                    }
                          
                    
                }while(!done);
                
                Brick newBrick = new Brick(i,j,name);
                
                if(name.equals("Bomb"))
                {
                    newBrick.setBackground(Color.black);
                }
                else
                {
                    this.randomColor(newBrick, maxColors); /* Set color to the brick.  */    
                }
                /*  | FOR TESTING PURPOSES |
                if(i%2==0)
                {
                    this.bricks[i][j].setBackground(Color.BLACK);
                }
                else
                {
                    this.bricks[i][j].setBackground(Color.RED);
                }*/
                
                
                this.panel.add(newBrick);  
                yArray.add(newBrick);
            }
            this.bricks.add(yArray);
        }
    }

    
    /**
     * <br>Transformer: Changes the color of the brick.
     * <br>Postcondition: Color of brick has been set.
     * @param brick the brick that will get be 'painted' 
     * @param maxColors the maximum number of colors to choose from.
     */
    /* This happens only one time, at the beginning of the game. */
    private void randomColor(Brick brick, int maxColors)
    {
        boolean done = false;

        /* If a user is so good that exceeds level 7, then stop at 7 colors that are available. */
        if(maxColors >= colors.size())
        {
            maxColors = colors.size()-1;
        }
        
        /* We use do-while loop, because if a color is selected, we might have used it as many times as we can , 
            so we need to pick another one. */
        do
        {
            Random rand = new Random();
            int randNum = rand.nextInt(maxColors - 0 + 1) + 0;
            if(colors.get(randNum).getMaxBricks()>0)
            {
                colors.get(randNum).decrementMaxBricks();
                brick.setBackground(colors.get(randNum).getColor()); /* The background color of the brick. */
                done = true;
            }  
        }while(!done);
    }
    
    
    /**
     * <br>Accessor: Returns the bricks 2D array.
     * <br>Postcondition: bricks array has been returned.
     * @return array of all the bricks
     */
    public ArrayList<ArrayList<Brick>> getBricks()
    {
        return this.bricks;
    }
    
    /**
     * Keeps track of how many bricks can still be made with each color.
     */
    public class levelsColors
    {
        int maxBricks;
        Color color;
        
        /**
         * <br>Constructor
         * <br>Precondition: Argument level has to be greater than 0 and argument color has to be of type Color and non null.
         * <br>Postcondition: A new custom levelsColors instance is made.
         * @param maxBricks maximum number of bricks with color: color.
         * @param color the color of the brick.
         */
        public levelsColors(int maxBricks, Color color)
        {
            this.maxBricks = maxBricks;
            this.color = color;
        }
        
        /**
         * <br>Transformer: Decrements by 1 the maxBricks number.
         * <br>Precondition: Argument has to be of type int.
         * <br>Postcondition: maxBricks has been decremented by 1.
        */
        public void decrementMaxBricks()
        {
            this.maxBricks--;
        }
        
        /**
         * <br>Accessor: Returns the maxBricks.
         * <br>Postcondition: maxBricks  has been returned.
         * @return maxBricks maximum number of bricks with color: color.
         */
        public int getMaxBricks()
        {
            return this.maxBricks;            
        }
        
        /**
         * <br>Accessor: Returns the color.
         * <br>Postcondition: color has been returned.
         * @return color the color.
         */
        public Color getColor()
        {
            return this.color;
        }
    }
}
