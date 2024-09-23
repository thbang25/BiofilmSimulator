/**
Team 167
Thabang, Kenneth, Omolemo
Biofilm simulator
red bacteria class
*/

import java.awt.*;
import java.util.List;
import java.util.Random;

class RedBacteria extends Bacteria {
    private static final int[] HorizontalPlane = {-1, 0, 1, 0}; /** Possible horizontal motions */
    private static final int[] VerticalPlane = {0, 1, 0, -1}; /** Possible vertical motions */

    private final int startingVerticalPosition; /* Initial x position */
    private final int startingHorizontalPosition; /* Initial y position */

    /** constructor for the red variant */
    public RedBacteria(int x, int y) {
        super(x, y, Color.RED); /* Set color of the variant to red */
        this.startingVerticalPosition = x; /* Record initial position */
        this.startingHorizontalPosition = y; /* Record initial position */
    }

    /** get parameters to allow red bacteria to move */
    @Override
    public void update(int[][] Grid, List<Bacteria> newBacteria, double redGrowthRate, double blueGrowthRate, Random random) {
        /* Check if bacteria should spread */
        if (random.nextDouble() < redGrowthRate) {
            trySpread(Grid, newBacteria, random); /* Spread variant around randomly in the environment */
        }

        /* Movement for red bacteria */
        if (random.nextDouble() < 0.1) { /* Increased likelihood of movement */
            spreadOutward(Grid, random); /* Move with balanced outward growth */
        }
    }

    /** Move bacteria with balanced outward growth */
    private void spreadOutward(int[][] Grid, Random random) {
        int[] spreadThisSide = null;
        double spreadDirection = -1;

        /* Evaluate all possible moves */
        for (int i = 0; i < 4; i++) {
            int updateHorizontal = x + HorizontalPlane[i];
            int updateVertical = y + VerticalPlane[i];

            if (isValidPosition(Grid, updateHorizontal, updateVertical)) {
                /* Calculate distance from initial position */
                double distance = Math.sqrt(Math.pow(updateHorizontal - startingVerticalPosition, 2) + Math.pow(updateVertical - startingHorizontalPosition, 2));
                /* Combine distance with random factor for balanced movement */
                double randomFactor = random.nextDouble(); /* Random factor between 0 and 1 */
                double spreadDirCounter = distance * 0.8 + randomFactor * 0.2; /* Weight distance more, but include randomness */

                /* Choose the move with the best spreadDirCounter */
                if (spreadDirCounter > spreadDirection) {
                    spreadDirection = spreadDirCounter;
                    spreadThisSide = new int[]{updateHorizontal, updateVertical};
                }
            }
        }

        /* Move to the best direction */
        if (spreadThisSide != null) {
            x = spreadThisSide[0];
            y = spreadThisSide[1];
            addTrail(); /* Add position to the trail after moving */
        }
    }

    /** Check if the new position is within grid boundaries and not occupied */
    private boolean isValidPosition(int[][] Grid, int updateHorizontal, int updateVertical) {
        return updateHorizontal >= 0 && updateHorizontal < Grid.length &&
               updateVertical >= 0 && updateVertical < Grid[0].length &&
               Grid[updateHorizontal][updateVertical] == 0; /* Ensure the cell is empty */
    }

    /** bacteria multiplies by producing offspring */
    @Override
    protected Bacteria AsexualReproduction(int x, int y) {
        /* Make sure that bacteria multiplies */
        return new RedBacteria(x, y);
    }
}
