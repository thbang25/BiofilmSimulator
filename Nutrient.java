/**
Team 167
Thabang, Kenneth, Omolemo
Biofilm simulator
Nutrient class
*/

/** nutrient class */
public class Nutrient {
    private final int x; //vertical plane
    private final int y; //horizontal plane

    public Nutrient(int x, int y) {
        this.x = x;
        this.y = y;
    }
/** return vertical plane co */
    public int getX() {
        return x;
    }
/** return horizontal plane co */
    public int getY() {
        return y;
    }
}
