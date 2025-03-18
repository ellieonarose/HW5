
import java.util.HashMap;
import java.util.Map;

// This class is a solver for pig solitaire
public class PigSolitaireSolver {

    // Instance variables
    private int goal;
    private int turns;
    private Map<String, Double> memo; // Memoization cache

    // Constructor
    public PigSolitaireSolver(int goal, int turns) {
        this.goal = goal;
        this.turns = turns;
        this.memo = new HashMap<>();
    }
//for each i and j there is some min k to hold
//print out comma separated grid of min hold values, import into spreadsheet
//surface plot over values

    //method to compute the minimum hold value for each i and j at each score
    public int minHold(int i, int j) {
        int minHold = Integer.MAX_VALUE;
        for (int k = 1; k <= 100 - i; k++) { // Ensure we don't exceed goal
        double pRoll = (1.0 / 6) * pWin(i, j - 1, 0);
        for (int roll = 2; roll <= 6; roll++) {
            pRoll += (1.0 / 6) * pWin(i, j, k + roll);
        }
        double pHold = pWin(i + k, j - 1, 0);

        if (pHold >= pRoll) {
            minHold = k;
            break;  // Stop early once we find the minimum hold value
        }
    }

        return minHold;
    }

    // Method to compute the probability of winning with optimal play
    public double pWin(int i, int j, int k) {
        // Base cases
        // If reaching or exceeding goal, win
        if (i + k >= goal) 
            return 1.0;  

        // No turns left, lose
        if (j == 0) 
            return 0.0;  

        // Memoization lookup
        String state = i + "," + j + "," + k;
        if (memo.containsKey(state)) 
            return memo.get(state);

        // Probability calculations
        // Rolling a 1 resets turn score
        double pRoll = (1.0 / 6) * pWin(i, j - 1, 0); 
        // Rolling 2-6 keeps turn count
        for (int roll = 2; roll <= 6; roll++) {
            pRoll += (1.0 / 6) * pWin(i, j, k + roll); 
        }

        // Holding secures points
        double pHold = pWin(i + k, j - 1, 0) ; 

        double result = Math.max(pRoll, pHold);
        memo.put(state, result); // Store result
        return result;
    }

    // Method to determine if rolling is the best option
    public boolean shouldRoll(int i, int j, int k) {
       
        // If no roll has been taken, roll
        if(k == 0){
            return true;
        }

        // No turns left or already won
        if (j == 0 || i + k >= goal) 
            return false; 

        // if k is great enough to give a good chacne of winning, hold
        // if(k > 1/5 * goal)  
        //     return false;

        double pRoll = (1.0 / 6) * pWin(i, j - 1, 0);
        for (int roll = 2; roll <= 6; roll++) {
            pRoll += (1.0 / 6) * pWin(i, j, k + roll);
        }
        double pHold = pWin(i + k, j - 1, 0) ;

        // Roll if it has a higher probability of winning
        return pRoll > pHold; 
    }

    // Main method to test the solver
    public static void main(String[] args) {
        PigSolitaireSolver solver = new PigSolitaireSolver(100, 11);
        //print probability of winning
        System.out.println("Probability of winning: " + solver.pWin(0, 11, 0));
        Die die = new Die();

        //print out the minimum hold values
        for (int i = 0; i < 100; i++) {
            for (int j = 10; j >=1; j--) {
                System.out.print(solver.minHold(i, j) + ",");
            }
            System.out.println();
        }

        // int myScore = 0;
        // int turnScore = 0;
        // int remainingTurns = 10;

        // while (myScore < 100 && remainingTurns > 0) {
        //     System.out.println("\nCurrent Score: " + myScore + " | Turns Left: " + remainingTurns);
        //     boolean rolling = solver.shouldRoll(myScore, remainingTurns, turnScore);

        //     if (rolling) {
        //         int roll = die.die();
        //         System.out.println("Player rolled: " + roll);
        //         if (roll == 1) {
        //             // Rolling a 1 resets turnScore and ends turn
        //             turnScore = 0;
        //             remainingTurns--;
        //             System.out.println("Rolled a 1! Lost turn points.");
        //         } else {
        //             turnScore += roll;
        //         }
        //     } else {
        //         // Player chooses to hold
        //         myScore += turnScore;
        //         turnScore = 0;
        //         remainingTurns--;
        //         System.out.println("Player held. New Score: " + myScore);
        //     }
        // }

        // // Determine if player wins or loses
        // if (myScore >= 100) {
        //     System.out.println("\nPlayer wins!");
        // } else {
        //     System.out.println("\nPlayer loses!");
        // }
    }
}
