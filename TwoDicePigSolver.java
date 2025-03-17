import java.util.Scanner;

public class TwoDicePigSolver {
    // Instance variables
    private double[][][] p;       //Storing win probability for each valid stage
    private boolean[][][] roll;   //Storing value to show if the player should roll in the stage or not
    private int goal;             //The point goal for Pig game

    public TwoDicePigSolver(int goal, double epsilon) {
        this.goal = goal;
        p = new double[goal][goal][goal];
        roll = new boolean[goal][goal][goal];

        while (valueIterate() >= epsilon);
    }

    private double valueIterate() {
        double theta = 0.0;

        // For each state
        for (int i = 0; i < goal; i++) {
            for (int j = 0; j < goal; j++) {
                for (int k = 0; k < goal - i; k++) {
                    // Get the old probability
                    double oldProb = p[i][j][k];

                    //Set the new probability in p 
                    p[i][j][k] = computeNewProb(i, j, k);

                    // Update theta, the maximum of current theta and the difference of old and new winning probability
                    theta = Math.max(theta, Math.abs(oldProb - p[i][j][k]));
                }
            }
        }
        return theta;
    }

    private double computeNewProb(int i, int j, int k) {
        // Probability of holding: the opponent's turn starts with the player's score increased by k
        double pHold = (1 - pWin(j, i + k, 0));
        
        // Probability of rolling, not that if roll double 1, the player lose all the total score with total turn score.
        double pRoll = 10.0/36 * (1 - pWin(j, i, 0)) + 1.0/36 * (1 - pWin(j, 0, 0))   
                        + 1.0/36 * pWin(i, j, k + 4) + 2.0/36 * pWin(i, j, k + 5) + 3.0/36 * pWin(i, j, k + 6) + 4.0/36 * pWin(i, j, k + 7) + 5.0/36 * pWin(i, j, k + 8)
                        + 4.0/36 * pWin(i, j, k + 9) + 3.0/36 * pWin(i, j, k + 10) + 2.0/36 * pWin(i, j, k + 11) + 1.0/36 * pWin(i, j, k + 12);

        // Determine whether to roll or hold based on which action has a higher probability of winning
        roll[i][j][k] = pRoll > pHold;
        return Math.max(pHold, pRoll);
    }

    public double pWin(int i, int j, int k) {
        // Return 1.0 if we reach the goal, 0.0 if the opponent do, and the winning probability of the state otherwise
        if (i + k >= goal) return 1.0;
        else if (j >= goal) return 0.0;
        else return p[i][j][k];
    }

    public boolean shouldRoll(int i, int j, int k) {
        return roll[i][j][k];
    }

    public void outputPolicy() {
        for (int i = 0; i < goal; i++) {
            for (int j = 0; j < goal; j++) {
                System.out.print("Player score: " + i + ", Opponent score: " + j + " -> ");
                for (int k = 0; k < goal - i; k++) {
                    if (roll[i][j][k]) {
                        System.out.print("Roll at turn value " + k + "  ");
                    } else {
                        System.out.print("Hold at turn value " + k + "  ");
                    }
                }
                System.out.println(); // Move to the next line for the next score pair
            }
        }
    }

    public void advise() {
        Scanner in = new Scanner(System.in);
        System.out.println("Starting Pig advice mode. Enter nothing to terminate.");
        while (true) {
            try {
                System.out.print("Please enter your score, your opponent's score, and the turn total, separated by spaces: ");
                String line = in.nextLine().trim();
                if (line.equals(""))
                    break;
                Scanner lineIn = new Scanner(line);
                int i = lineIn.nextInt();
                int j = lineIn.nextInt();
                int k = lineIn.nextInt();
                if (i >= 0 && j >= 0 && k >= 0 && i < goal && j < goal && k < goal - i) {
                    System.out.printf("You should %s.\n", shouldRoll(i, j, k) ? "roll" : "hold");
                } else {
                    System.out.println("Invalid input. Scores and turn total must be non-negative and within the goal range.");
                }
                lineIn.close();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter three integers separated by spaces.");
            }
        }
        System.out.println("Exiting Pig advice mode.");
        in.close();
    }

    
    public static void main(String[] args) {
        int goal = 100;
        double epsilon = 1e-9;

        TwoDicePigSolver solver = new TwoDicePigSolver(goal, epsilon);
        solver.outputPolicy();
        System.out.println("Probability that the first player will win if both play optimally: " + solver.pWin(0, 0, 0));
        solver.advise();
    }
}