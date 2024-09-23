/**
Team 167
Thabang, Kenneth, Omolemo
Biofilm simulator 
bacteria class to allow the variants to extend
*/
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** abstract class to make the functions and behavior of bacteria */
abstract class Bacteria {
   protected int x;
   protected int y;
   protected Color color;
   /** List to store the trail */
   protected List<Point> pslTrail;
   
/** create constructor */
   public Bacteria(int x, int y, Color color) {
      this.x = x;
      this.y = y;
      this.color = color;
      /* Initialize the trail list */
      this.pslTrail = new ArrayList<>();
      // Add initial position to the trail
      addTrail();
   }
	  
/** created method to be used by the children or extensions of the variants */
   public abstract void update(int[][] Grid, List<Bacteria> newBacteria, double redGrowthRate, double blueGrowthRate, Random random);
   
/** return colour */
   public Color getColor() {
      return color;
   }

/** return horizontal coordinate */
   public int getX() {
      return x;}
	  
/** return vertical coordinate */
   public int getY() {
      return y;}
	  
/** spread the growth of the bacteria around the grid */
   protected boolean trySpread(int[][] Grid, List<Bacteria> newBacteria, Random random) {
      int[] HorizontalPlane = {-1, 0, 1, 0}; /* the possible motions in the horizontal axis */
      int[] VerticalPlane = {0, 1, 0, -1}; /* the possible motions in the Vertical axis */
       // a list of the directions we can move into
      List<Integer> availableDirections = new ArrayList<>();
   
   /* possible directions */
      for (int i = 0; i < 4; i++) {
         int updateHorizontal = x + HorizontalPlane[i];
         int updateVertical = y + VerticalPlane[i];
      
      /* Check bounds */
         if (updateHorizontal >= 0 && updateHorizontal < Grid.length &&
            updateVertical >= 0 && updateVertical < Grid[0].length &&
            Grid[updateHorizontal][updateVertical] == 0) {
         /* add to available directions where free space is found */
            availableDirections.add(i);
         }
      }
   
   /* if the space is not occupied move into it */
      if (!availableDirections.isEmpty()) {
         int direction = availableDirections.get(random.nextInt(availableDirections.size()));
         int updateHorizontal = x + HorizontalPlane[direction];
         int updateVertical = y + VerticalPlane[direction];
        
      /* Ensure the new position is within grid bounds */
         if (updateHorizontal >= 0 && updateHorizontal < Grid.length &&
            updateVertical >= 0 && updateVertical < Grid[0].length) {
            /* create new bacteria through reproduction */
            newBacteria.add(AsexualReproduction(updateHorizontal, updateVertical));
            return true;}
      }
      return false;
   }
 /** bacterial trail as it secretes pheromones */
   protected void addTrail() {
      pslTrail.add(new Point(x, y));
   }
   
/** make sure that bacteria multiplies */
   protected abstract Bacteria AsexualReproduction(int x, int y);

/** secret trail */
   public List<Point> getPslTrail() {
      return pslTrail;
   }


}