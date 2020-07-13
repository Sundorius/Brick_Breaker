package controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import model.Brick;
import view.GraphicUI;


/**
 * This class is the brain of the Brick Breaker game.
 * @author Sundorius
 */
public class Controller 
{
    private GraphicUI gui;
    private ButtonListener bLis;
    private ArrayList<ArrayList<Brick>> bricks;
    private int level = 1;
    private int maxRows = 14 + (level-1)/2;
    private int maxCols = 12 + level/2;
    private int points = 0;
    private int pointThreshold = 80+(level*20);

    
    /**
     * <br>Constructor
     * <br>Precondition: Nothing.
     * <br>Postcondition: A new controller is made, and listeners are attached to each brick.
     */
    public Controller()
    {
        this.gui = new GraphicUI(maxRows, maxCols, level);
        this.bricks = this.gui.getBricks();
        this.bLis = new ButtonListener();
        this.AssignListeners();
    }
    
    
    /**
     * <br>Transformer: Assigns a listener to each brick.
     * <br>Postcondition: Each brick has a listener.
     */
    private void AssignListeners()
    {
        for(int i=0; i<maxRows; i++)
        {
            for(int j=0; j<maxCols; j++)
            {
                this.bricks.get(i).get(j).addMouseListener(bLis);
            }
        }
    }
    
    
    /**
     * <br>Observer: Checks if the brick with coordinates x,y has been checked previously.
     * <br>Precondition: Arguments x,y have to be of type int and x>0 and y>0 and holder has to be of type ArrayList<CoordinateHolder> and holder!=null.
     * <br>Postcondition: Returns true if brick has been checked before, else returns false.
     * @param holder arrayList of the coordinates of the neighbouring bricks that are the same color as the selected brick.
     * @param x x coordinate.
     * @param y y coordinate.
     * @return  true if brick with coordinate x,y  has been checked previously, else false.
     */
    private boolean ifChecked(ArrayList<CoordinateHolder> holder, int x, int y )
    {
        for(int i=0; i<holder.size(); i++)
        {
            if(x == holder.get(i).getX() && y == holder.get(i).getY())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * <br>Observer: Find bricks that are going to explode by the bomb brick.
     * <br>Precondition: Argument btn has to be of type Brick and btn!=null.
     * <br>Postcondition: Selection checked.
     * @param holder arrayList of the coordinates of the bricks that are going
     * to explode because of the bomb brick.
     * @param btn a button of type Brick.
     *
     * @return an arrayList with the coordinates of the neighbouring bricks that
     * are going to explode.
     */
    private ArrayList<CoordinateHolder> FindBricksThatWillBeEXploded(Brick btn, ArrayList<CoordinateHolder> holder)
    {

        int xPos = btn.getXCoo();
        int yPos = btn.getYCoo();

        /* Checks if the bricks that are on xPos-1, xPos+1, yPos-1 and yPos+1
              of the bomb brick are inside the Bricks grid. If they are, their
              coordinates are placed in a holder for further modification.
              Else it returns only the coordinates of the bomb brick. */
        if (xPos - 1 >= 0)
        {
            holder.add(new CoordinateHolder(xPos - 1, yPos));
            if (yPos - 1 >= 0 && yPos + 1 <= maxCols-1)
            {
                holder.add(new CoordinateHolder(xPos - 1, yPos - 1));
                holder.add(new CoordinateHolder(xPos - 1, yPos + 1));
            }
            else
            {
                if (yPos - 1 >= 0)
                {
                    holder.add(new CoordinateHolder(xPos - 1, yPos - 1));
                }
                if (yPos + 1 <= maxCols-1)
                {
                    holder.add(new CoordinateHolder(xPos - 1, yPos + 1));
                }
            }
        }
        if (xPos + 1 <= maxRows-1)
        {
            holder.add(new CoordinateHolder(xPos + 1, yPos));
            if (yPos - 1 >= 0 && yPos + 1 <= maxCols-1)
            {
                holder.add(new CoordinateHolder(xPos + 1, yPos - 1));
                holder.add(new CoordinateHolder(xPos + 1, yPos + 1));
            }
            else
            {
                if (yPos - 1 >= 0)
                {
                    holder.add(new CoordinateHolder(xPos + 1, yPos - 1));
                }
                if (yPos + 1 <= maxCols-1)
                {
                    holder.add(new CoordinateHolder(xPos + 1, yPos + 1));
                }
            }
        }
        else
        {
            if (yPos - 1 >= 0)
            {
                holder.add(new CoordinateHolder(xPos, yPos - 1));
            }
            if (yPos + 1 <= maxCols-1)
            {
                holder.add(new CoordinateHolder(xPos, yPos + 1));
            }
        }
        /* The position of the bomb brick is already added in the holder by the mouseClicked method. */
        return holder;
    }

     /**
      * <br>Observer: Checks if player's selection is correct.
      * <br>Precondition: Arguments btn have to be of type Brick and btn!=null and holder has to be of type ArrayList<CoordinateHolder> and holder!=null.
      * <br>Postcondition: Selection checked.
      * @param btn a button of type Brick.
      * @param holder arrayList of the coordinates of the neighbouring bricks that are the same color as the selected brick.
      * @return  an arrayList with the coordinates of the neighbouring bricks that are of the same color as btn.
      */
    private ArrayList<CoordinateHolder> CheckSelection(Brick btn, ArrayList<CoordinateHolder> holder)
    {
        /* If the brick does not exist, return just the coordinates arrayList. */
        /* It is an extra check, for safety. */
        if(btn == null)
        {
            return holder;
        }
        
        int X = btn.getXCoo();
        int Y = btn.getYCoo();

        for(int row=btn.getXCoo()+1; row<maxRows; row++)  /* Check all the bricks UNDER the selected brick. */
        {
            if(this.bricks.get(row).get(Y)==null || this.bricks.get(row).get(Y).getBackground() != btn.getBackground() )
            {
                break; 
            }
            /* If that brick is checked in a previous check, or it is the parameter btn, then stop here, and proceed in another direction. */
            else if(ifChecked(holder,this.bricks.get(row).get(Y).getXCoo(),  this.bricks.get(row).get(Y).getYCoo())
                    || ( this.bricks.get(row).get(Y).getXCoo() == btn.getXCoo() && this.bricks.get(row).get(Y).getYCoo() == btn.getYCoo()))
            {
                break;
            }
            else if(btn.getBackground() == this.bricks.get(row).get(btn.getYCoo()).getBackground())
            {
                /* adds that brick's coordinates in the arrayList, since it has the same color as the parameter btn. */
                holder.add(new CoordinateHolder(this.bricks.get(row).get(btn.getYCoo()).getXCoo(), this.bricks.get(row).get(btn.getYCoo()).getYCoo()));
                /* now check for other bricks. */
                holder = CheckSelection(this.bricks.get(row).get(btn.getYCoo()), holder);
            }
        }
        for(int col=btn.getYCoo()+1; col<maxCols; col++) /* Check all the bricks RIGHT the selected brick. */
        {
            if(this.bricks.get(X).get(col)==null || this.bricks.get(X).get(col).getBackground() != btn.getBackground())
            {
                break;
            }
            /* If that brick is checked in a previous check, or it is the parameter btn, then stop here, and proceed in another direction. */
            else if(ifChecked(holder,this.bricks.get(X).get(col).getXCoo(),  this.bricks.get(X).get(col).getYCoo())
                    || ( this.bricks.get(X).get(col).getXCoo() == btn.getXCoo() && this.bricks.get(X).get(col).getYCoo() == btn.getYCoo()))
            {
                break;
            }
            else if(btn.getBackground() == this.bricks.get(X).get(col).getBackground())
            {
                /* adds that brick's coordinates in the arrayList, since it has the same color as the parameter btn. */
                holder.add(new CoordinateHolder(this.bricks.get(X).get(col).getXCoo(), this.bricks.get(X).get(col).getYCoo()));
                /* now check for other bricks. */
                holder = CheckSelection(this.bricks.get(X).get(col), holder);
            }
        }
        for(int col=btn.getYCoo()-1; col>=0; col--) /* Check all the bricks LEFT the selected brick. */
        {
            if(this.bricks.get(X).get(col)==null || this.bricks.get(X).get(col).getBackground() != btn.getBackground())
            {
                break;
            }
            /* If that brick is checked in a previous check, or it is the parameter btn, then stop here, and proceed in another direction. */
            else if(ifChecked(holder,this.bricks.get(X).get(col).getXCoo(),  this.bricks.get(X).get(col).getYCoo())
                    || ( this.bricks.get(X).get(col).getXCoo() == btn.getXCoo() && this.bricks.get(X).get(col).getYCoo() == btn.getYCoo()))
            {
                break;
            }
            else if(btn.getBackground() == this.bricks.get(X).get(col).getBackground())
            {
                /* adds that brick's coordinates in the arrayList, since it has the same color as the parameter btn. */
                 holder.add(new CoordinateHolder(this.bricks.get(X).get(col).getXCoo(), this.bricks.get(X).get(col).getYCoo()));
                /* now check for other bricks. */
                holder = CheckSelection(this.bricks.get(X).get(col), holder);
            }
        }
        for(int row=btn.getXCoo()-1; row>=0; row--)  /* Check all the bricks ABOVE the selected brick. */
        {
            if(this.bricks.get(row).get(Y)==null || this.bricks.get(row).get(Y).getBackground() != btn.getBackground())
            {
                break;
            }
            /* If that brick is checked in a previous check, or it is the parameter btn, then stop here, and proceed in another direction. */
            else if(ifChecked(holder,this.bricks.get(row).get(Y).getXCoo(),  this.bricks.get(row).get(Y).getYCoo())
                    || ( this.bricks.get(row).get(Y).getXCoo() == btn.getXCoo() && this.bricks.get(row).get(Y).getYCoo() == btn.getYCoo()))
            {
                break;
            }
            else if(btn.getBackground() == this.bricks.get(row).get(Y).getBackground())
            {
                /* adds that brick's coordinates in the arrayList, since it has the same color as the parameter btn. */
                 holder.add(new CoordinateHolder(this.bricks.get(row).get(Y).getXCoo(), this.bricks.get(row).get(Y).getYCoo()));
                /* now check for other bricks. */
                holder = CheckSelection(this.bricks.get(row).get(Y), holder);
            }
        }
        return holder;         
    }

    
    /**
     * <br>Transformer: Swaps isEnabled attribute and background of all the bricks that are upwards of the brick, the bricks that have the same Y coordinate as the brick, which coordinate is stored in the current.
     * <br>Precondition: current has to be of type CoordinateHolder and current!=null.
     * <br>Postcondition: Attribute background and isEnabled are swapped.
     * @param current object of type CoordinateHolder, that holds the coordinates of the brick.
     */
    private void swapBrickInfoVertical(CoordinateHolder current)
    {
        int y = current.getY();
        for( int row=current.getX(); row>0; row--)/* Since we "play" with row+1 we do not want to hit an out of bounds exception, so we stop at the 1st brick.
                                          /* The 0 brick will also be checked by the command ....[row-1][y]. That way we never go out of bounds. */
        {
            boolean enabled = bricks.get(row).get(y).isEnabled();
            Color color = bricks.get(row).get(y).getBackground();
            String name = bricks.get(row).get(y).getBname();

            bricks.get(row).get(y).setEnabled(bricks.get(row-1).get(y).isEnabled());
            bricks.get(row).get(y).setBackground(bricks.get(row-1).get(y).getBackground());
            bricks.get(row).get(y).setBname(bricks.get(row-1).get(y).getBname());

            bricks.get(row-1).get(y).setEnabled(enabled);
            bricks.get(row-1).get(y).setBackground(color);
            bricks.get(row-1).get(y).setBname(name);
            this.gui.revalidate();
            this.gui.repaint();
        }      
    }
    
    
    /**
     * <br>Transformer: Swaps isEnabled attribute and background of all the bricks that are in the columns right of the column with the Y coordinate that is stored in current.
     * <br>Precondition: current has to be of type CoordinateHolder and current!=null.
     * <br>Postcondition: Attribute background and isEnabled are swapped.
     * @param current object of type CoordinateHolder, that holds the y coordinate of the brick.
     */
    private void swapBrickInfoHorizontal(CoordinateHolder current)
    {
        int y = current.getY();
        for( int i=0; i<maxRows; i++)
        {
            for(int col=y; col<maxCols-1; col++) /* Since we "play" with col+1 we do not want to hit an out of bounds exception, so we stop at the maxCols-1 brick. */
                                          /* The last brick will also be checked by the command ....[i][col+1]. That way we never go out of bounds. */
            {
                boolean enabled = bricks.get(i).get(col).isEnabled();
                Color color = bricks.get(i).get(col).getBackground();
                String name =  bricks.get(i).get(col).getBname();

                bricks.get(i).get(col).setEnabled(bricks.get(i).get(col+1).isEnabled());
                bricks.get(i).get(col).setBackground(bricks.get(i).get(col+1).getBackground());
                bricks.get(i).get(col).setBname(bricks.get(i).get(col+1).getBname());

                bricks.get(i).get(col+1).setEnabled(enabled);
                bricks.get(i).get(col+1).setBackground(color);
                bricks.get(i).get(col+1).setBname(name);
                this.gui.revalidate();
                this.gui.repaint();
            }
        }     
    }
    
    /**
     * This class holds a X and a Y coordinate.
     */
    private class CoordinateHolder implements Comparable<CoordinateHolder>
    {
        private int x;
        private int y;
        
        /**
         * <br>Constructor
         * <br>Precondition: Arguments x,y values have to be greater than 0.
         * <br>Postcondition: A new custom CoordinateHolder is made.
         * @param x x coordinate.
         * @param y y coordinate.
         */
        private CoordinateHolder(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        
        
        /**
         * <br>Transformer: Sets the x coordinate.
         * <br>Precondition: Argument has to be of type int.
         * <br>Postcondition: x coordinate has been set.
         * @param y x coordinate.
         */
        private void setX(int x)
        {
            this.x = x;
        }
        
        /**
         * <br>Accessor: Returns the x coordinate.
         * <br>Postcondition: y coordinate has been returned.
         * @return x coordinate.
         */
        private int getX()
        {
            return this.x;
        }
        
        
        /**
         * <br>Transformer: Sets the y coordinate.
         * <br>Precondition: Argument has to be of type int.
         * <br>Postcondition: y coordinate has been set.
         * @param y y coordinate.
        */
        private void setY(int y)
        {
            this.y = y;
        }
        
        /**
         * <br>Accessor: Returns the y coordinate.
         * <br>Postcondition: x coordinate has been returned.
         * @return y coordinate.
         */
        private int getY()
        {
            return this.y;
        }
        
        
        /* Compares the X,Y coordinates. */
        /* Sorts X coordinates in ascending order and Y coordinates in descending order ( if they have the same X coordinate!!). */
        @Override
        public int compareTo(CoordinateHolder holder)
        {
            int Xcoo=((CoordinateHolder)holder).getX();
            int X_result =  this.x - Xcoo;  /* Compares in ascending order. */
            if(X_result == 0) /* If they have the same X coordinate. */
            {
                int Ycoo = ((CoordinateHolder)holder).getY();
                return Ycoo - this.y; /* Compares in descending order. */
            }
            return X_result; /* If they have different X coordinate. */
        }
    }
    
    
    /**
     * <br>Transformer: Makes the brick with the same coordinates as stored in current, blank and unpressable.
     * <br>Precondition: current has to be of type CoordinateHolder and current!=null.
     * <br>Postcondition: Brick with the same coordinates as stored in current is blank and unpressable.
     * @param current object of type CoordinateHolder.
     */
    private void makeBlank(CoordinateHolder current)
    {
        int x = current.getX();
        int y = current.getY();
        /* If brick is not enabled, do nothing, because it is already made blank. */
        if( !this.bricks.get(x).get(y).isEnabled() )
        {
            return;
        }
        /* Else make the brick[x][y] blank and unpressable. */
        else
        {
            this.bricks.get(x).get(y).setEnabled(false);
            this.bricks.get(x).get(y).setBackground(Color.WHITE); /* By blank I mean, white. */
            this.bricks.get(x).get(y).setBname("Dead");
            this.gui.revalidate();
            this.gui.repaint();
        }
    }
    
    
    /**
     * <br>Transformer: Find which columns are blank by checking the last row and save only the Y coordinate of each blank column.
     * <br>Precondition: holder must be of type ArrayList<CoordinateHolder> and holder!=null.
     * <br>Postcondition: The Y coordinates of all the blank columns will be stored at holder.
     * @param holder object of type ArrayList<CoordinateHolder>.
     */
    private ArrayList<CoordinateHolder> findBlankColumns(ArrayList<CoordinateHolder> holder)
    {
        for(int col=0; col<maxCols; col++)
        {
            /* If the brick at the bottom of the column is not enabled, that means that the column is full of blank bricks. */
            if(!this.bricks.get(maxRows-1).get(col).isEnabled())
            {
                /* We only need the Y coordinate, so we initialize the X as zero(x=0). */
                holder.add(new CoordinateHolder(0, col));
            }
        }
        return holder;
    }

    /**
     * <br>Transformer: Makes all the preparations for the level up.
     * <br>Precondition: nothing
     * <br>Postcondition: All the necessary attributes have been changed to fit
     * the new level.
     */
    private void levelUp()
    {
        this.points = 0;
        this.level++;
        this.maxRows = 12 + level / 2;
        this.maxCols = 14 + (level - 1) / 2;
        this.pointThreshold = 80 + (level * 20);
        this.gui.dispose();
        this.gui = new GraphicUI(maxRows,maxCols,level);
        this.bricks = this.gui.getBricks();
        this.bLis = new ButtonListener();
        this.AssignListeners();
    }

    /**
     * <br>Observer: Checks if all bricks can not be destroyed.
     */
    private boolean findIfBlocked()
    {
        int blocked = 0;
        int alive = 0;

        for (int i = 0;i < this.bricks.size();i++)
        {
            for (int j = 0;j < this.bricks.get(i).size();j++)
            {
                /* We want canDestroy, at the end, to have value >0. */
                int canDestroy = 0;
                Brick choosen = this.bricks.get(i).get(j);
                if (choosen.getBname().equals("Bomb"))
                {
                    alive++;
                    if (choosen.getXCoo() - 1 >= 0)
                    {

                        if (!this.bricks.get(choosen.getXCoo() - 1).get(j).getBname().equals("Dead"))
                        {
                            canDestroy++;
                        }
                        if (choosen.getYCoo() - 1 >= 0)
                        {
                            if (!this.bricks.get(choosen.getXCoo() - 1)
                                .get(choosen.getYCoo() - 1).getBname().equals("Dead"))
                            {
                                canDestroy++;
                            }
                        }
                        if (choosen.getYCoo() + 1 <= maxCols - 1)
                        {
                            if (!this.bricks.get(choosen.getXCoo() - 1)
                                .get(choosen.getYCoo() + 1).getBname().equals("Dead"))
                            {
                                canDestroy++;
                            }
                        }
                    }
                    if (choosen.getXCoo() + 1 <= maxRows - 1)
                    {
                        if (!this.bricks.get(choosen.getXCoo() + 1).get(j).getBname().equals("Dead"))
                        {
                            canDestroy++;
                        }
                        if (choosen.getYCoo() - 1 >= 0)
                        {
                            if (!this.bricks.get(choosen.getXCoo() + 1)
                                .get(choosen.getYCoo() - 1).getBname().equals("Dead"))
                            {
                                canDestroy++;
                            }
                        }
                        if (choosen.getYCoo() + 1 <= maxCols - 1)
                        {
                            if (!this.bricks.get(choosen.getXCoo() + 1)
                                .get(choosen.getYCoo() + 1).getBname().equals("Dead"))
                            {
                                canDestroy++;
                            }
                        }
                    }
                    if (choosen.getYCoo() - 1 >= 0)
                    {
                        if (!this.bricks.get(choosen.getXCoo())
                            .get(choosen.getYCoo() - 1).getBname().equals("Dead"))
                        {
                            canDestroy++;
                        }
                    }
                    if (choosen.getYCoo() + 1 <= maxCols - 1)
                    {
                        if (!this.bricks.get(choosen.getXCoo())
                            .get(choosen.getYCoo() + 1).getBname().equals("Dead"))
                        {
                            canDestroy++;
                        }
                    }
                    /* If brick can not destroy even one other, then increment blocked bricks. */
                    if (canDestroy <= 0)
                    {
                        blocked++;
                    }
                }
                else if (choosen.getBname().equals("Simple"))
                {
                    Color choosenColor = choosen.getBackground();
                    alive++;
                    if (choosen.getXCoo() - 1 >= 0)
                    {
                        if (this.bricks.get(choosen.getXCoo() - 1).get(j).getBackground().equals(choosenColor))
                        {
                            canDestroy++;
                        }
                    }
                    if (choosen.getXCoo() + 1 <= maxRows - 1)
                    {
                        if (this.bricks.get(choosen.getXCoo() + 1).get(j).getBackground().equals(choosenColor))
                        {
                            canDestroy++;
                        }
                    }
                    if (choosen.getYCoo() - 1 >= 0)
                    {
                        if (this.bricks.get(i).get(choosen.getYCoo() - 1).getBackground().equals(choosenColor))
                        {
                            canDestroy++;
                        }
                    }
                    if (choosen.getYCoo() + 1 <= maxCols - 1)
                    {
                        if (this.bricks.get(i).get(choosen.getYCoo() + 1).getBackground().equals(choosenColor))
                        {
                            canDestroy++;
                        }
                    }
                    /* If brick can not destroy even one other, then increment blocked bricks. */
                    if (canDestroy <= 0)
                    {
                        blocked++;
                    }
                }
            }
        }
        System.out.println("BLOCKED: " + blocked);
        System.out.println("ALIVE: " + alive);
        return alive == blocked;
    }
    
    
    /**
     * This class checks if the brick selection is valid,if it is valid it does the necessary modifications.
     */
    public class ButtonListener implements MouseListener
    {
        /**
         * <br>Transformer: Checks if the selection of the brick is valid,if it is valid it does the necessary modifications.
         * <br>Postcondition: If selection is valid, necessary modifications are made.
         * @param me mouse event
         **/
        @Override
        public void mouseClicked(MouseEvent me)
        {
            Brick btn = ((Brick) me.getSource());  /* The brick the user selected. */
            ArrayList<CoordinateHolder> holder = new ArrayList<CoordinateHolder>();
            holder.add(new CoordinateHolder(btn.getXCoo(), btn.getYCoo()));

            /* If brick is a Bomb Brick. */
            if (btn.getBname().equals("Bomb"))
            {
                if(btn.getYCoo()-1>=0)
                {
                    holder.add(new CoordinateHolder(btn.getXCoo(), btn.getYCoo() - 1));
                }
                if(btn.getYCoo()+1<=maxCols-1)
                {
                    holder.add(new CoordinateHolder(btn.getXCoo(), btn.getYCoo() + 1));
                }
                holder = FindBricksThatWillBeEXploded(btn, holder);
            }
            /* If brick is Simple Brick. */
            else
            {
                holder = CheckSelection(btn, holder);
            }
            /* If holder is of size <=1 and brick is simple brick, it means that no other 
                  bricks near the selected one have the same color, if brick is bomb brick it is valid.
                  Because size <=1 means, that the arrayList contains only the selected brick. */
            if (holder.size() <= 1 && btn.getBname().equals("Simple"))
            {
                return;
            }
            else  /* If selection of user is valid, then here starts the real game! */
            {
            	/* Sorts the X coordinates in ascending order, and the Y coordinates in descending order(if they have the same X coordinate!!)! */
                Collections.sort(holder);
                /*  | FOR TESTING PURPOSES |
                for(CoordinateHolder arr: holder)  System.out.println("X: "+arr.getX()+"  Y: "+arr.getY());
                System.out.println(); 
                */
                
                /* Makes all the neighbouring bricks blank and unpressable. */
                for(int i=0; i<holder.size(); i++)
                {
                	/* If a brick that is destroyed is not dead by previous round, then increment points. */
                    if (!bricks.get(holder.get(i).getX()).get(holder.get(i).getY()).getBname().equals("Dead"))
                    {
                        points++;
                    }
                    makeBlank(holder.get(i));
                }
                
                /* FOR DEBUGGING
                System.out.println("pointThreshold: "+pointThreshold);
                System.out.println("POINTS: " + points);
                System.out.println("BLOCKED OR NOT: " + findIfBlocked()+"\n");
                
                
                /* If all bricks are blocked and user does not have the required points for 
                    	this level, then game ends. */
                if(findIfBlocked() && points <= pointThreshold)
                {
                    gui.dispose();
                    gui = new GraphicUI(-1,maxCols,level);
                }

                
                /* If user meets the point threshold, then level up. */
                if (points >= pointThreshold)
                {
                    System.out.println("NEXT LEVEL");
                    levelUp();
                    return;
                }
                
                /* Moves all the blank bricks at the top. */
                for(int i=0; i<holder.size(); i++)
                {
                    swapBrickInfoVertical(holder.get(i));
                }
                
                /* Remove all the coordinates of the holder, and replace them with the new of the columns that are blank, if any. */
                holder.clear();
                
                /* Find which columns are blank. */
                holder = findBlankColumns(holder);
                
                /* If there are blank columns, move them to the right! */
                if(holder.size()>0)
                {
                    /* Because all the X coordinates are equal to 0, the objects will be sorted by the Y coordinate! */
                    Collections.sort(holder);
                    /* | FOR TESTING PURPOSES |
                    for(int i=0; i<holder.size(); i++)
                    {
                        System.out.println("X: "+holder.get(i).getX()+"  Y: "+holder.get(i).getY());
                    }
                    System.out.println();
                    */

                    /* Moves all the blank bricks at the right. */
                    for(int i=0; i<holder.size(); i++)
                    {
                        swapBrickInfoHorizontal(holder.get(i));
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent me){}
        @Override
        public void mouseReleased(MouseEvent me){}
        @Override
        public void mouseEntered(MouseEvent me){}
        @Override
        public void mouseExited(MouseEvent me){}
    } 
}