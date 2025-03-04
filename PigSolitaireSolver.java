// This class is a solver for pig solitaire
//Consider the solitaire (single player) game of Pig where a player is challenged to reach a given goal score g within n turns.
public class PigSolitaireSolver {

//Instance variables
private int goal;
private int turns;

//Constructor that takes in an int for the goal and int for the amount of turns
public PigSolitaireSolver(int goal, int turns){
    this.goal = goal;
    this.turns = turns;
}

//Method that Return the probability of winning with optimal play given the current score, number of turns completed, and current turn total.
public double pWin(int i, int j, int k) {
    if (i >= goal) {
        return 1;
    }
    if (j >= turns) {
        return 0;
    }
    
    double pRoll = 0;
    double pHold = 0;

    // 1/6 of the time, the player will roll a 1 and lose their turn total
    pRoll += 1.0/6 * (1 - pWin(i, j + 1, 0));

    // 5/6 of the time, the player will roll a number between 2 and 6
    for (int l = 2; l <= 6; l++) {
        pRoll += 1.0 / 6 * pWin(i, j + 1, k + l);
    }

    pHold = pWin(i + k, j + 1, 0);
    return Math.max(pRoll, pHold);
}

//Method that determins whether or not an optimal player should roll given the current score, number of turns completed, and current turn total
public boolean shouldRoll(int i, int j, int k) {
    if (i >= goal) {
        return false;
    }
    if (j >= turns) {
        return false;
    }

    double pRoll = 0;
    double pHold = 0;
   
    // 1/6 of the time, the player will roll a 1 and lose their turn total
    pRoll += 1.0/6 * (1 - pWin(i, j + 1, 0));

    // 5/6 of the time, the player will roll a number between 2 and 6
    for (int l = 2; l <= 6; l++) {
        pRoll += 1.0 / 6 * pWin(i, j + 1, k + l);
    }

    pHold = pWin(i + k, j + 1, 0);
    return pRoll > pHold;

}
}