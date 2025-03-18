The Honor Pledge: I affirm that I will uphold the highest principles of honesty and integrity in all my endeavors at Gettysburg College and foster an atmosphere of mutual respect within and beyond the classroom.

Group Name: Heidi Ho, Ellie Mandelberg

Name: Huynh(Heidi) Ho
Studen IDs: 614 0064

Name: Ellie Mandelberg
Student IDs: 614 0617


---------------------
1. Pig Solitaire (2.4)
(a)
State space.

		The goal is the number the player must try to reach within their allowed turns
		n is the number of rolls allowed to reach the goal 
		0 <= i <= goal is the total score of the player
   			
		0 <= j <= n is the number of turns the player has remaining
		
		0 <= k < 1, 1 < k <= goal is the number of points the player has accumulated in the current turn but not yet added to i.
		
		Actions:
		If a player decides to roll, one die is rolled. If the die does not land on 1, the landed number is added to the turn total (k). If a 1 is rolled, the player's turn total becomes 0, and the next turn begins.
		
		If a player decides to hold, the turn total (k) is added to the current score (i) and the next turn begins. 


(b) Write the equations that describe optimal play.
	
	 Equation for a roll:
		1/6 * pWin(i, j-1, 0) + 1.0/6 * pWin(i, j-1, k+2) + 1.0/6 * pWin(i, j-1, k+3) + 1.0/6 * pWin(i, j-1, k+4) + 1.0/6 * pWin(i, j-1, k+5) + 1.0/6 * pWin(i, j-1, k+6). 
		Where the first pWin is the probability of winning in the 1/6th chance that the player rolls a 1 and loses their score. The rest of the equation is each 
		of the 1/6th chances of rolling another score and the percentage of winning when having that number added to the round total. 
	
	Equation for a hold:  
	pWin(i+k, j-1, 0) where this is the percentage of winning when the player adds their round total to their current 
	score and has one fewer turn to reach the goal.
	
	Optimal play: 
	Maximum of the result hold and roll.


(c) Prove that the state space is acyclic, i.e. that states cannot repeat.


	Every turn, one of two things will happen. Either the current player score will increase by their turn total or it will remain the same with the roll of a 1. While k, the turn total, may repeat based on roll, the total score of the player will never return to 0 (or any previous score). Since the scores can only move in the positive direction, there is no way for any state to repeat itself if a score is registered. If no score is registered (when the player rolls a 1), every component of the state space will remain the same. In both cases, no cycle is formed because the state can never change to something it was in the past. Also, there are only a certain number of moves allowed. Every move that is taken is one move taken away from the number of rolls remaining, meaning this state is always changing in the negative direction. This will never cycle back. 

(d) Compute the optimal policy for g = 100 and n = 10. Again, assume that an optimal player with a winning turn total will hold and win.

	I have created a spreadsheet detailing what the player should do at each state. Here is the link to that: 
	https://docs.google.com/spreadsheets/d/108DOtctWtUNZLHozID3WU1gPEfjiZwQxmoTCyNjNxG0/edit?usp=sharing 

(e) Summarize or visualize the policy, and describe it qualitatively in your own words.

	The earlier in the game, the fewer risks there are that need to be taken. As the game progresses, though, if the player is far away from the goal, more risks need to be taken to ensure a win. The lower the score toward the end, the more the player needs to roll to reach the goal. As shown, if the player is on turn one (ten turns left) with zero points, they can hold at 22. If they are on turn ten, though, they must play until they get 100 points. Otherwise, there is no chance they can win.


(f) For g = 100, what is the smallest n for which the optimal player’s initial win probability is ≥ .50?

	n = 11

2. Pig (3.5)
   
(a)

    State space:
    The state is defined by: (i, j, k), for which: 
    0 <= i <= 100 is the total score of the player up to the turn. 
    0 <= j <= 100 is the total score of the opponent up to the turn.
    0 <= k < 100 - i is the turn total of the current player. 

    The goal is to reach 100 points, in other word
    If the player has i + k >= 100, they win. (j + k >= for the opponent).

    Actions: 
    Roll: Two standard dice are rolled.
    If neither shows a 1, their sum is added to the turn total.
    If a single 1 is rolled, the player's turn ends with the loss of the turn total.
    If two 1's are rolled, the player's turn ends with the loss of the turn total and score.

    Hold: The turn total k is added to the player’s score, and it becomes the opponent’s turn.
(b) 
    
    Let P(i,j,k) is the probability of winning from this state.

    Base case: 
        If i + k >= 100, P(i, j, k) = 1.    (Win cond.)
        If j >= 100, P(i, j, k) = 0.        (Lose cond.)

    Winning probability if hold:
    P_hold(i, j, k) = 1 - P(j, i + k,  0).

    Winning probability if roll:
    P_roll(i, j, k) = 1/6 * ((1 - P(j, i, 0)) + P(i, j, k + 2) + P(i, j, k + 3) + P(i, j, k + 4) + P(i, j, k + 5) + P(i, j, k + 6)). 

    Winning probability: 
    P(i, j, k) = max(P_hold(i, j, k), P_roll(i, j, k)).

    Optimal play:
    If P_roll > P_hold then roll, otherwise hold. 

(c) 
    
    The implementation uses value iteration to compute P(i,j,k) iteratively until the maximum difference in probabilities between iterations is less than epsilon.
    
    During iterations, when estimate the probability of winning for each state, compare p_Hold and p_Roll then update the roll - 3D boolean array to store whether the player should roll or hold for each sate. The player should roll if p_Roll > p_Hold. 
    
    The probability that the first player will win if both players play optimally is given by p[0][0][0] which is 0.5305927250129694.

(d)      
    
    The optimal policy is based on balancing risk (rolling a 1 and losing all turn points) and reward (maximizing the probability of reaching 100 points before the opponent). The key elements are:

	Rolling Strategy: A player should continue rolling if their turn total k is below a certain threshold (at least 21 if not yet reached the goal). This threshold is not constant but depends on the player’s score i, the opponent’s score j, and the remaining distance to 100.
	
    Holding Strategy: Once the accumulated turn total k reaches a sufficiently high value (depending on i, j), the player should hold to secure the points.
	Risk vs. Reward Consideration: Rolling comes with a 1/6 risk of rolling a 1 and losing the turn total k. The policy carefully evaluates when the expected gain from rolling outweighs this risk.
	
    Players should take more risks when behind and play more conservatively when ahead.

    Visualization Insight: The 3D policy landscape(given in class) shows multiple local minima, meaning that depending on the game state (i, j, k), the best threshold for stopping may vary. The player should stop rolling before falling into a disadvantageous local minimum.


3. Two-Dice Pig

(a)
    
    State space: 
    The state is defined by: (i, j, k), for which: 
    0 <= i <= 100 is the total score of the player up to the turn. 
    0 <= j <= 100 is the total score of the opponent up to the turn.
    0 <= k < 100 - i is the turn total of the current player. 

    The goal is to reach 100 points, in other word
    If the player has i + k >= 100, they win. (j + k >= for the opponent).

    Actions:
        
    Roll: One standard die is rolled.
    If it does not show a 1, their sum is added to the turn total.
    If a 1 is rolled, the player's turn ends with the loss of the turn total.

    Hold: The turn total k is added to the player’s score, and it becomes the opponent’s turn.
(b)
    
    Let P(i,j,k) is the probability of winning from this state

    Base case: 
        If i + k >= 100, P(i, j, k) = 1.    (Win cond.)
        If j >= 100, P(i, j, k) = 0.        (Lose cond.)

    Winning probability if hold:
    P_hold(i, j, k) = 1 - P(j, i + k,  0)

    Winning probability if roll:
    P_roll(i, j, k) =   10/36 * (1 - P(j, i, 0)) + 1/36 * (1 - P(j, 0, 0))   
                        + 1/36 * P(i, j, k + 4) + 2/36 * P(i, j, k + 5) + 3/36 * P(i, j, k + 6) + 4/36 * P(i, j, k + 7) + 5/36 * P(i, j, k + 8)
                        + 4/36 * P(i, j, k + 9) + 3/36 * P(i, j, k + 10) + 2/36 * P(i, j, k + 11) + 1/36 * P(i, j, k + 12). 

    Winning probability: 
    P(i, j, k) = max(P_hold(i, j, k), P_roll(i, j, k))

    Optimal play:
    If P_roll > P_hold then roll, otherwise hold. 

(c)
    
    The implementation uses value iteration to compute P(i,j,k) iteratively until the maximum difference in probabilities between iterations is less than epsilon.
    
    During iterations, when estimate the probability of winning for each state, compare p_Hold and p_Roll then update the roll - 3D boolean array to store whether the player should roll or hold for each sate. The player should roll if p_Roll > p_Hold. 
    
    Given that the probability of winning decreases due to the additional penalty of rolling two ones, the first player’s probability of winning (0.5195588939251339) is slightly lower than in the standard single-die Pig game.

(d)
    
    The optimal policy is based on balancing risk (rolling a 1 and losing all turn points or rolling double 1s or losing all scores) and reward (maximizing the probability of reaching 100 points before the opponent). 
    
    The key elements are:

	Rolling Strategy: A player should continue rolling if their turn total k is below a certain threshold (at least 21 if not yet reached the goal). This threshold is not constant but depends on the player’s score i, the opponent’s score j, and the remaining distance to 100.
	
    Holding Strategy: Once the accumulated turn total k reaches a sufficiently high value (depending on i, j), the player should hold to secure the points.
	
    Players should take more risks when behind and play more conservatively when ahead.

    Visualization Insight: The 3D policy landscape(given in class) shows multiple local minima, meaning that depending on the game state (i, j, k), the best threshold for stopping may vary. The player should stop rolling before falling into a disadvantageous local minimum.
