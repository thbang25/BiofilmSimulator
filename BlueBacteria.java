/**
Team 167
Thabang, Kenneth, Omolemo
Biofilm simulator 
blue bacteria class
*/

import java.awt.*;
import java.util.List;
import java.util.Random;

/** blue bacteria that is immobile */
class BlueBacteria extends Bacteria {
	/** constructor for the blue variate */
    public BlueBacteria(int x, int y) {
        super(x, y, Color.BLUE); /* set the colour */
    }

   @Override
   public void update(int[][] grid, List<Bacteria> newBacteria, double blueGrowthRate, double redGrowthRate, Random random) {
      if (random.nextDouble() < blueGrowthRate) { /* Check bacterial growth rate */
         if (trySpread(grid, newBacteria, random)) {
            addTrail(); /* Add position to the trail after spreading */
         }
      }
   }
/** bacteria multiplies by producing offspring */

    @Override
    protected Bacteria AsexualReproduction(int x, int y) {
		/* make sure that bacteria multiplies */
        return new BlueBacteria(x, y);}
} 
