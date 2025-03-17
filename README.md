The Honor Pledge: I affirm that I will uphold the highest principles of honesty and integrity in all my endeavors at Gettysburg College and foster an atmosphere of mutual respect within and beyond the classroom.

Group Name: Heidi Ho, Ellie Mandelberg

Name: Huynh(Heidi) Ho
Studen IDs: 614 0064

Name: Ellie Mandelberg
Student IDs: 614 0617


---------------------
1. Pig Solitaire (2.4)


2. Pig (3.5)
(a) State space: 
    The state is defined by: (i, j, k) 
    for which: 
        0 <= i <= 100 is the total score of the player up to the turn 
        0 <= j <= 100 is the total score of the opponent up to the turn
        0 <= k < 100 - i is the turn total of the current player 

    The goal is to reach 100 points, in other word
    	If the player has i + k >= 100, they win. (j + k >= for the opponent)

    Actions:
        Roll: Rolls a die, if they got a 1, k resets to 0, and other player takes over, otherwise, the points are added to k and the player may roll again or hold. 

        Hold: The turn total k is added to the player’s score, and it becomes the opponent’s turn.

(b) 
    Let P(i,j,k) is the probability of winning from this state

    Base case: 
        If i + k >= 100, P(i, j, k) = 1
        If j >= 100, P(i, j, k) = 0 (Player2 won)

    Winning probability if hold:
    P_hold(i, j, k) = 1 - P(j, i + k,  0)

    Winning probability if roll:
    P_roll(i, j, k) = 1/6 * ((1 - P(j, i, 0) + P(i, j, k + 2) + P(i, j, k + 3) + P(i, j, k + 4) + P(i, j, k + 5) + P(i, j, k + 6))) 

    Winning probability: 
    P(i, j, k) = max(P_hold(i, j, k), P_roll(i, j, k))

    Optimal play:
    roll[i][j][k] = P_roll > P_hold.
    If P_roll > P_hold then roll, otherwise hold. 

(c) 
    The implementation uses value iteration to compute P(i,j,k) iteratively until the maximum difference in probabilities between iterations is less than epsilon.
    
    During iterations, when estimate the probability of winning for each state, compare p_Hold and p_Roll then update the roll - 3D boolean array to store whether the player should roll or hold for each sate. The player should roll if p_Roll > p_Hold. 
    
    The probability that the first player will win if both players play optimally is given by p[0][0][0] which is 0.5305927250129694

(d) 
3. Two-Dice Pig