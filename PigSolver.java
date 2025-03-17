import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class PigSolver {
    // Instance variables
    private double[][][] p;
    private boolean[][][] roll;
    private int goal;

    public PigSolver(int goal, double epsilon) {
        this.goal = goal;
        p = new double[goal][goal][goal];
        roll = new boolean[goal][goal][goal];

        int iterations = 0;
        double theta;
        do {
            theta = valueIterate();
            iterations++;
            if (iterations % 1000 == 0) {
                System.out.println("Iteration: " + iterations + " | Theta: " + theta);
            }
        } while (theta >= epsilon);
    }

    private double valueIterate() {
        double theta = 0.0;

        for (int i = 0; i < goal; i++) {
            for (int j = 0; j < goal; j++) {
                for (int k = 0; k < goal - i; k++) {
                    double oldProb = p[i][j][k];
                    p[i][j][k] = computeNewProb(i, j, k);

                    theta = Math.max(theta, Math.abs(oldProb - p[i][j][k]));
                }
            }
        }
        return theta;
    }

    private double computeNewProb(int i, int j, int k) {
        // Probability of holding: the opponent's turn starts with the player's score increased by k
        double pHold = (1 - pWin(j, i + k, 0));
        
        // Probability of rolling: average over the outcomes of rolling 2, 3, 4, 5, 6, and 1
        double pRoll = (1.0 / 6) * ((1 - pWin(j, i, 0)) + pWin(i, j, k + 2) + pWin(i, j, k + 3)
                + pWin(i, j, k + 4) + pWin(i, j, k + 5) + pWin(i, j, k + 6));

        // Determine whether to roll or hold based on which action has a higher probability of winning
        roll[i][j][k] = pRoll > pHold;
        return Math.max(pHold, pRoll);
    }

    public double pWin(int i, int j, int k) {
        if (i + k >= goal) return 1.0;
        else if (j >= goal) return 0.0;
        else return p[i][j][k];
    }

    public boolean shouldRoll(int i, int j, int k) {
        return roll[i][j][k];
    }

    

    public void savePolicyToFile(String filename, int fixedTurnTotal) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Player Score,Opponent Score,Turn Total,Decision\n"); // Add column headers
            for (int i = 0; i < goal; i++) {
                for (int j = 0; j < goal; j++) {
                    for (int k = 0; k < goal - i; k++) {
                        writer.write(i + "," + j + "," + k + "," + (roll[i][j][k] ? "Roll" : "Hold") + "\n");
                    }
                }
            }
            System.out.println("Policy saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        // try {
        //     if (args.length > 2)
        //         throw new IllegalArgumentException("Too many arguments");
        //     if (args.length > 0) goal = Integer.parseInt(args[0]);
        //     if (args.length > 1) epsilon = Double.parseDouble(args[1]);
        // } catch (Exception e) {
        //     System.out.println(e);
        //     System.out.println("Usage: java PigSolver [int goal [double epsilon]]");
        //     System.exit(1);
        // }

        PigSolver solver = new PigSolver(goal, epsilon);
        // solver.outputPolicy();
        // System.out.println("Probability that the first player will win if both play optimally: " + solver.pWin(0, 0, 0));
        // solver.advise();
        solver.savePolicyToFile("policy.csv", 10);
    }
}