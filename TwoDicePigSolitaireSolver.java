public class TwoDicePigSolver {
    //computes optimal play for Two Dice Pig using value iteration.
    //(1) Two standard dice are rolled. If neither shows a 1, their sum is added to the turn total. 
    //(2) If a single 1 is rolled, the player’s turn ends with the loss of the turn total. 
    //(3) If two 1s are rolled, the player’s turn ends with the loss of the turn total and score. 
    //Value iteration computes the win probabilities and optimal play decisions for each non-terminal state assuming that both players play optimally, 
    //that is, play so as to maximize the probability of winning.
    /*Two standard dice are rolled.
If neither shows a 1, their sum is added to the turn total.
If a single 1 is rolled, the player’s turn ends with the loss of the turn total.
If two 1s are rolled, the player’s turn ends with the loss of the turn total and score. */
   
    //Instance variables
    private int goal;
    private double theta; //a double value - convergence constant for value iteration. If the largest state value change is less than theta, terminate value iteration

    public TwoDicePigSolver(int goal, double theta){
        this.goal = goal;
        this.theta = theta;
    }


    public double pWin(int myScore, int otherScore, int turnScore) {
        //If the largest state value change is less than theta, terminate value iteration
        if (Math.abs(turnScore) < theta) {
            return 0.5;
        }
        if (myScore + turnScore >= goal) {
            return 1;
        }
        if (otherScore >= goal) {
            return 0;
        }

        double pRoll = 0;
        double pHold = 0;

        // 1/36 of the time, the player will roll two 1s and lose their turn total and score
        pRoll += 1.0/36 * (1 - pWin(otherScore, myScore, 0));

        // 1/6 of the time, the player will roll a 1 and lose their turn total
        pRoll += 1.0/6 * (1 - pWin(otherScore, myScore, 0));

        // 25/36 of the time, the player will roll a number between 2 and 12
        for (int l = 2; l <= 12; l++) {
            pRoll += 1.0 / 36 * pWin(myScore, otherScore, turnScore + l);
        }

        pHold = pWin(myScore + turnScore, otherScore, 0);
        return Math.max(pRoll, pHold);
       
    }
}
