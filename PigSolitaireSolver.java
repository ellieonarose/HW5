import java.util.HashMap;
import java.util.Map;

public class PigSolitaireSolver {
    private int goal;
    private int turns;
    private Map<String, Double> memo;

    //Constructor
    public PigSolitaireSolver(int goal, int turns) {
        this.goal = goal;
        this.turns = turns;
        this.memo = new HashMap<>();
    }

    //Method to compute the minimum hold value for each i and j at each score
    public int minHold(int i, int j) {
        int minHold = -1;

        for (int k = 1; k <= Math.min(goal - i, 100); k++) {
            double pRoll = (1.0 / 6) * pWin(i, j - 1, 0);
            for (int roll = 2; roll <= 6; roll++) {
                pRoll += (1.0 / 6) * pWin(i, j - 1, Math.min(k + roll, goal - i));
            }
            double pHold = pWin(i + k, j - 1, 0);

            if (pHold >= pRoll) {
                minHold = k;
                break;
            }
        }

        return minHold;
    }

    //Method to compute the probability of winning with optimal play
    // i: current score
    // j: current turn number
    // k: current turn score
    public double pWin(int i, int j, int k) {
        // Base cases
        // Player wins
        if (i + k >= goal) 
            return 1.0; 
        // No turns left, player loses 
        if (j >= turns) 
            return 0.0;     

        String state = i + "," + j + "," + k;
        if (memo.containsKey(state)) return memo.get(state);

        // Rolling a 1 resets turn total and advances turn count
        double pRoll = (1.0 / 6) * pWin(i, j + 1, 0);

        // Rolling 2-6 does not consume a turn
        for (int roll = 2; roll <= 6; roll++) {
            pRoll += (1.0 / 6) * pWin(i, j, Math.min(k + roll, goal - i));
        }

        // Holding secures points but advances turn count
        double pHold = pWin(i + k, j + 1, 0);

        // Compute final probability and store it in memoization cache
        double result = Math.max(pRoll, pHold);
        memo.put(state, result);
        return result;
    }

    //Method to determine if player should roll or hold
    // i: current score
    // j: current turn number
    // k: current turn score
    public boolean shouldRoll(int i, int j, int k) {
       // No turns left
       if (j >= turns || i >= goal) 
           return false;

       double pRoll = (1.0 / 6) * pWin(i, j + 1, 0);
       for (int roll = 2; roll <= 6; roll++) {
           pRoll += (1.0 / 6) * pWin(i, j, Math.min(k + roll, goal - i));
       }
       double pHold = pWin(i + k, j + 1, 0);

       return pRoll > pHold;
    }

     //Main method to test the solver
    public static void main(String[] args) {
        PigSolitaireSolver solver = new PigSolitaireSolver(95, 11);
        //print probability of winning
        System.out.println("Probability of winning: " + solver.pWin(0, 0, 0));
        System.out.println("Should Roll: " + solver.shouldRoll(0, 0, 0));
        Die die = new Die();


 }
}
