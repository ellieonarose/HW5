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
    public double pWin(int i, int j, int k) {
        // Base cases
    if (i + k >= goal) return 1.0;  // Player wins
    if (j == 0) return 0.0;         // No turns left, player loses

    String state = i + "," + j + "," + k;
    if (memo.containsKey(state)) return memo.get(state);

    // Rolling a 1 resets turn total and ends turn
    double pRoll = (1.0 / 6) * pWin(i, j - 1, 0);

    // Rolling 2-6 does NOT consume a turn
    for (int roll = 2; roll <= 6; roll++) {
        pRoll += (1.0 / 6) * pWin(i, j, Math.min(k + roll, goal - i));
    }

    // Holding secures points but ends turn
    double pHold = pWin(i + k, j - 1, 0);

    // Compute final probability and store it in memoization cache
    double result = Math.max(pRoll, pHold);
    memo.put(state, result);
    return result;
    }

    public boolean shouldRoll(int i, int j, int k) {
        // No turns left
        if (j == 0 || i >= goal) return false;

        double pRoll = (1.0 / 6) * pWin(i, j - 1, 0);
        for (int roll = 2; roll <= 6; roll++) {
            pRoll += (1.0 / 6) * pWin(i, j - 1, Math.min(k + roll, goal - i));
        }
        double pHold = pWin(i + k, j - 1, 0);

        return pRoll > pHold;
    }

     //Main method to test the solver
    public static void main(String[] args) {
        PigSolitaireSolver solver = new PigSolitaireSolver(90, 8);
        //print probability of winning
        System.out.println("Probability of winning: " + solver.shouldRoll(0, 0, 24));
        Die die = new Die();

//         //print out the minimum hold values
//         for (int i = 0; i < 100; i++) {
//             for (int j = 10; j >=1; j--) {
//                 System.out.print(solver.minHold(i, j) + ",");
//             }
//             System.out.println();
//         }

//         // int myScore = 0;
//         // int turnScore = 0;
//         // int remainingTurns = 10;

//         // while (myScore < 100 && remainingTurns > 0) {
//         //     System.out.println("\nCurrent Score: " + myScore + " | Turns Left: " + remainingTurns);
//         //     boolean rolling = solver.shouldRoll(myScore, remainingTurns, turnScore);

//         //     if (rolling) {
//         //         int roll = die.die();
//         //         System.out.println("Player rolled: " + roll);
//         //         if (roll == 1) {
//         //             // Rolling a 1 resets turnScore and ends turn
//         //             turnScore = 0;
//         //             remainingTurns--;
//         //             System.out.println("Rolled a 1! Lost turn points.");
//         //         } else {
//         //             turnScore += roll;
//         //         }
//         //     } else {
//         //         // Player chooses to hold
//         //         myScore += turnScore;
//         //         turnScore = 0;
//         //         remainingTurns--;
//         //         System.out.println("Player held. New Score: " + myScore);
//         //     }
//         // }

//         // // Determine if player wins or loses
//         // if (myScore >= 100) {
//         //     System.out.println("\nPlayer wins!");
//         // } else {
//         //     System.out.println("\nPlayer loses!");
//         // }
//     }
 }
}
