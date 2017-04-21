
import java.util.*;

/**
 * Updated by ChesterAiGo on 4/20/17.
 *
 * Denotation: 1 --> 'r', 2 --> 'y', 0 --> '.'
 */
public class ConnectFour {

    public static void main(String[] args) {


        /**
         * IF YOU WISH TO PLAY HERE, call play("red") for playing as red or play("yellow") for playing as yellow
         */

        play(args[0]);
        

///////////////////////////////////////////////////////
//                   FOR TESTING                     //
///////////////////////////////////////////////////////

//        for(int i = 0 ; i < 10000 ; i ++) {
//            String raw_tc = generateRandomTc();
//
//            if(raw_tc != null) {
//
//                long startTime = System.nanoTime();
//
//                String[] tc = raw_tc.split(" ");
//                int player = -1, oppoent = -1;
//                if(tc[1].equals("red")) {
//                    player = 1;
//                    oppoent = 2;
//                }
//                else {
//                    player = 2;
//                    oppoent = 1;
//                }
//
//                String state = tc[0];
//                int[][] currentBoard = convertToBoard(state);
//
//                if(cal(oppoent, currentBoard)[4] > 0)
//                    alphaPruning(convertToState(currentBoard), 2, 2);
//                else
//                    alphaPruning(convertToState(currentBoard), 2, 7);
//
//                long endTime = System.nanoTime();
//                System.out.println((endTime - startTime) / 1000000);  //divide by 1000000 to get milliseconds.)
//                if((endTime - startTime) / 1000000 > 1000) {
//                    System.out.println(raw_tc);
//                    break;
//                }
//            }
//        }



    }

/////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                         //
//                                      PLAYING                                            //
//                                                                                         //
/////////////////////////////////////////////////////////////////////////////////////////////

    public static void play(String input) {
        Scanner in = new Scanner(System.in);
        String state = ".......,.......,.......,.......,.......,.......";
        int[][] currentBoard = convertToBoard(state);
        displayBoard(currentBoard);

        if(input.equalsIgnoreCase("red")) {

            do {
                //MY MOVEMENT
                int myMove = in.nextInt();
                put(currentBoard, myMove, 1);
                //DISPLAY
                displayBoard(currentBoard);
                if(yellowWins(currentBoard) || redWins(currentBoard)) break;
                //COMPUTER
                int computerMove = 3;
                if(cal(1, currentBoard)[4] > 0 || cal(2, currentBoard)[4] > 0 )
                    computerMove = alphaPruning(convertToState(currentBoard), 2, 2);
                else
                    computerMove = alphaPruning(convertToState(currentBoard), 2, 9);

                put(currentBoard, computerMove, 2);

                //DISPLAY
                displayBoard(currentBoard);
                state = convertToState(currentBoard);
            } while(!yellowWins(currentBoard) && !redWins(currentBoard));

        } else if(input.equalsIgnoreCase("yellow")) {
            while(!yellowWins(currentBoard) && !redWins(currentBoard)) {
                //Computer
                int computerMove = 3;
                if(cal(2, currentBoard)[4] > 0 || cal(1, currentBoard)[4] > 0 )
                    computerMove = alphaPruning(convertToState(currentBoard), 1, 2);
                else
                    computerMove = alphaPruning(convertToState(currentBoard), 1, 9);

                put(currentBoard, computerMove, 2);

                displayBoard(currentBoard);
                if(yellowWins(currentBoard) || redWins(currentBoard)) break;
                //DISPLAY

                //MY MOVEMENT
                int myMove = in.nextInt();
                put(currentBoard, myMove, 1);
                //DISPLAY
                displayBoard(currentBoard);
                state = convertToState(currentBoard);
            }

        } else if(input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
            System.out.println("\n\nThank you for playing and see you soon !\n\n");
            System.exit(0);
        }
        else {
            System.out.println("\n\nInvalid input, game ends.\n\n");
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                         //
//                                  ALPHA - PRUNING                                        //
//                                                                                         //
/////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * ALPHA-PRUNING ALGORITHM
     * @param state
     * @param player
     * @param maxDepth
     */
    private static int alphaPruning(String state, int player, int maxDepth) {
        int[][] currentBoard = convertToBoard(state); // NOW BOARD INITIALIZED

        int bestMove = -1;

        int alpha = Integer.MIN_VALUE;
        int beta  = Integer.MAX_VALUE;
        //RED SEEKS FOR HIGHEST
        if(player == 1) {
            int highestScore = Integer.MIN_VALUE;
            //GET ALL VALID MOVES
            for(int i = 0 ; i < 7 ; i ++) {
                if (!columnIsFull(currentBoard, i)) {
                    int tempScore = 0;
                    int[][] newBoard = nextBoard(currentBoard, i, player);
                    tempScore = minForAP(newBoard, maxDepth, alpha, beta);
                    if (tempScore > highestScore) {
                        highestScore = tempScore;
                        alpha = tempScore;
                        bestMove = i;
                    }
                }
            }

        }
        // YELLOW SEEKS FOR LOWEST
        else if(player == 2) {

            int lowestScore = Integer.MAX_VALUE;
            //GET ALL VALID MOVES
            for(int i = 0 ; i < 7 ; i ++) {
                if (!columnIsFull(currentBoard, i)) {
                    int tempScore = 0;
                    int[][] newBoard = nextBoard(currentBoard, i, player);
                    tempScore = maxForAP(newBoard, maxDepth, alpha, beta);
                    if (tempScore < lowestScore) {
                        lowestScore = tempScore;
                        beta = tempScore;
                        bestMove = i;
                    }
                }
            }

        }
        //UNACCEPTABLE
        else {
            System.out.println("????");
            System.exit(10);
        }

//        System.out.println(bestMove);
        return bestMove;
        //Reset
    }
    private static int minForAP(int[][] board, int maxDepth, int alpha, int beta) {
        maxDepth -- ;
        //TERMINAL TEST
        if(maxDepth <= 0 ) {
            return utility(board, -1);
        }
        if(yellowWins(board))
            return utility(board, 2);

        if(redWins(board))
            return utility(board, 1);
        int lowestScore = Integer.MAX_VALUE;
        //GET ALL VALID MOVES
        for(int i = 0 ; i < 7 ; i ++)
            if(!columnIsFull(board, i)) {
                int tempScore = 0;
                int[][] newBoard = nextBoard(board, i, 2);
                tempScore = maxForAP(newBoard, maxDepth, alpha, beta);
                if(tempScore < lowestScore) {
                    lowestScore = tempScore;
                }
                if(lowestScore <= alpha) {
                    return lowestScore;
                }
                if(lowestScore < beta) {
                    beta = lowestScore;
                }
            }
        return lowestScore;
    }
    private static int maxForAP(int[][] board, int maxDepth, int alpha, int beta) {
        maxDepth -- ;
        if(maxDepth <= 0 ) {
            return utility(board, -1);
        }
        if(yellowWins(board))
            return utility(board, 2);

        if(redWins(board))
            return utility(board, 1);

        int highestScore = Integer.MIN_VALUE;
        //GET ALL VALID MOVES
        for(int i = 0 ; i < 7 ; i ++)
            if(!columnIsFull(board, i)) {
                int tempScore = 0;
                int[][] newBoard = nextBoard(board, i, 1);
                tempScore = minForAP(newBoard, maxDepth, alpha, beta);
                if(tempScore > highestScore) {
                    highestScore = tempScore;
                }
                if(highestScore >= beta) {
                    return highestScore;
                }
                if(alpha < highestScore) {
                    alpha = highestScore;
                }
            }

        return highestScore;

    }


/////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                         //
//                                  UTILITY FUNCTIONS                                      //
//                                                                                         //
/////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean redWins(int[][] board) {
        return cal(1, board)[3] > 0 ;
    }
    private static boolean yellowWins(int[][] board) {
        return cal(2, board)[3] > 0 ;
    }

    /**
     * UPDATE BOARD (RETURN A NEW ONE)
     * @param board
     * @param col
     * @param player
     * @return
     */
    private static int[][] nextBoard(int[][] board, int col, int player) {
        int[][] newBoard = copyArray(board);
        int i = 5;
        while(i >= 0 && newBoard[i][col] != 0) {
            i --;
        }
        try {
            newBoard[i][col] = player;
        } catch(Exception e) {
            displayBoard(board);
            System.out.println("In nextBoard...");
            System.exit(3);
        }
        return newBoard;
    }

    /**
     * Deeply copy an array
     * @param board
     * @return
     */
    private static int[][] copyArray(int[][] board) {
        if (board == null)
            return null;
        int[][] result = new int[board.length][];
        for (int r = 0; r < board.length; r++) {
            result[r] = board[r].clone();
        }
        return result;
    }

    /**
     * CHECK IF THIS COLUMN IS FULL --> INVALID MOVE!
     * @param board
     * @param col
     * @return
     */
    private static boolean columnIsFull(int[][] board, int col) {
        for(int i = 0 ; i < 6 ; i ++) {
            if(board[i][col] == 0) return false;
        }
        return true;
    }

    /**
     * Irrevelant to other functions, more like terminal test
     * convertBoard to be removed
     * @param state
     * @return
     */
    private static int utility(int[][] board, int winner) {

        if(winner == 1) {
            return Integer.MAX_VALUE - 100;
        }

        if(winner == 2) {
            return Integer.MIN_VALUE + 100;
        }

        return evaluation(board);
    }

    /**
     * Get score(red) - score(yellow)
     * convertBoard to be removed
     * @param state
     * @return
     */
    private static int evaluation(int[][] board) {
        return score(1, board) - score(2, board);
    }

    /**
     * Get score for any player, given the state is initialized in @evaluation
     * 1 --> 'r', 2 --> 'y', 0 --> '.'
     * @param player
     * @return
     */
    private static int score(int player, int[][] board) {
        int[] result = cal(player, board);
        return result[0] + 50 * result[1] + 1000 * result[2] + 100000 * result[3] + 10000 * result[4];
    }

    /**
     * Convert string state to int[][] board
     * @param state
     */
    private static int[][] convertToBoard(String state) {
        int[][] board = new int[6][7];
        state = state.replace(",", "");
        for(int i = 0 ; i < 6 ; i ++) {
            for(int j = 0 ; j < 7 ; j ++) {
                int index = i * 7 + j;
                if(state.charAt(index) == '.')
                    board[5 - i][j] = 0;
                else if(state.charAt(index) == 'r')
                    board[5 - i][j] = 1;
                else if(state.charAt(index) == 'y')
                    board[5 - i][j] = 2;
                else
                    board[5 - i][j] = -1; // ERROR
            }
        }
        return board;
    }

    /**
     *
     * For displaying the current board
     */
    private static void displayBoard(int[][] board) {
        System.out.println("--------------");
        for(int i = 0 ; i < 6 ; i ++) {
            for(int j = 0 ; j < 7 ; j ++) {
                char temp = '?';
                if(board[i][j] == 2) temp = 'X';
                if(board[i][j] == 1) temp = 'O';
                if(board[i][j] == 0) temp = '_';
                System.out.print(temp + " ");
            }
            System.out.println("");
        }
        System.out.println("--------------");
    }

    /**
     * Testing function, Convert a board to string state
     */
    private static String convertToState(int[][] board) {
        String state = "";
        for(int i = 5 ; i >= 0 ; i --) {
            for(int j = 0 ; j < 7 ; j ++) {
                if(board[i][j] == 0) state += ".";
                if(board[i][j] == 1) state += "r";
                if(board[i][j] == 2) state += "y";
            }
            if(i != 0) state += ",";
        }
        return state;
    }

    /**
     * For testing tournment version
     * @param board
     * @param col
     */
    private static void put(int[][] board, int col, int player) {
        int i = 5;
        for( ; i >= 0 ; i --) {
            if(board[i][col] == 0) break;
        }
        if(i == -1) System.out.println("Invalid move!");
        else board[i][col] = player;
    }
    /**
     * Get how many twos, threes, fours. If there's a streak >= 4, regard it as a four
     */
    private static int[] cal(int p, int[][] board) {
        int ones = Ones(p, board);

        int[] h = Horizonally(p, board);
        int[] v = Vertcially(p, board);
        int[] db = DiagonallyBelowHalf(p, board);
        int[] da = DiagonallyAboveHalf(p, board);
        int[] vda = ViceDiagonallyAboveHalf(p, board);
        int[] vdb = ViceDiagonallyBelowHalf(p, board);

        int twos = v[0] + h[0] + db[0] + da[0] + vda[0] + vdb[0];
        int threes = v[1] + h[1] + db[1] + da[1] + vda[1] + vdb[1];
        int fours = v[2] + h[2] + db[2] + da[2] + vda[2] + vdb[2];
        int worstThrees = h[3] + v[3] + HorizonallySpecial(p, board);
        return new int[] {ones, twos, threes, fours, worstThrees};
    }
    private static int   Ones(int p, int[][] board) {
        int ones = 0;
        for(int i = 0 ; i < 6 ; i ++)
            for(int j = 0 ; j < 7 ; j ++)
                if(board[i][j] == p)
                    ones ++;

        return ones;
    }
    private static int[] Horizonally(int p, int[][] board) {
        int twos = 0, threes = 0, fours = 0, WorstThrees = 0;
        for(int i = 0 ; i < 6 ; i ++) {
            for(int j = 0 ; j < 7 ; j ++) {
                if(board[i][j] == p) {
                    int tempStreak = 1;
                    while(j + 1 < 7 && board[i][j + 1] == p) {
                        tempStreak ++;
                        j ++;
                    }
                    if(tempStreak == 2)
                        twos ++;
                    if(tempStreak == 3) {
                        if(j + 1 < 7 && board[i][j + 1] == 0 && j - 3 >= 0 && board[i][j - 3] == 0) {
                            threes += 6;
                            WorstThrees += 3;
                        }
                        else if(j + 1 < 7 && board[i][j + 1] == 0) {
                            WorstThrees ++;
                            threes += 4;
                        }
                        else if(j - 3 >= 0 && board[i][j - 3] == 0) {
                            threes += 4;
                            WorstThrees ++;
                        }
                        else threes ++;
                    }
                    if(tempStreak >= 4)
                        fours ++;
                }
            }
        }
        return new int[] {twos, threes, fours, WorstThrees};
    }
    private static int[] Vertcially(int p, int[][] board) {
        int twos = 0, threes = 0, fours = 0, WorstThrees = 0;
        for(int i = 0 ; i < 7 ; i ++) {
            for(int j = 0 ; j < 6 ; j ++) {
                if(board[j][i] == p) {
                    int tempStreak = 1;
                    while(j + 1 < 6 && board[j + 1][i] == p) {
                        tempStreak ++;
                        j ++;
                    }
                    if(tempStreak == 2)
                        twos ++;
                    if(tempStreak == 3) {
                        if(j - 3 >= 0 && board[j - 3][i] == 0) {
                            threes += 4;
                            WorstThrees ++;
                        }
                        threes ++;
                    }
                    if(tempStreak >= 4)
                        fours ++;
                }
            }
        }
        return new int[] {twos, threes, fours, WorstThrees};
    }
    private static int[] DiagonallyAboveHalf(int p, int[][] board) {
        int twos = 0, threes = 0, fours = 0;
        for(int col = 1 ; col < 6 ; col ++) {
            for(int x = 0, y = col ; x < 7 - col ; x ++, y ++) {
                if(board[x][y] == p) {
                    int tempStreak = 0;
                    while(x < 6 && y < 7 && board[x][y] == p) {
                        x ++;
                        y ++;
                        tempStreak ++;
                    }

                    if(tempStreak == 2)
                        twos ++;
                    if(tempStreak == 3) {
                        if(y + 1 < 7 && x + 1 < 6 && board[x + 1][y + 1] == 0 && x - 3 >= 0 && y - 3 >= 0 && board[x - 3][y - 3] == 0) threes += 10;
                        else if(y + 1 < 7 && x + 1 < 6 && board[x + 1][y + 1] == 0 && y + 1 == 5) threes += 7;
                        else if(y + 1 < 7 && x + 1 < 6 && board[x + 1][y + 1] == 0) threes += 3;
                        else if(x - 3 >= 0 && y - 3 >= 0 && board[x - 3][y - 3] == 0) threes += 3;
                        else threes ++;
                    }
                    if(tempStreak >= 4)
                        fours ++;
                }
            }
        }
        return new int[] {twos, threes, fours};
    }
    private static int[] DiagonallyBelowHalf(int p, int[][] board) {
        int twos = 0, threes = 0, fours = 0;
        for(int row = 0 ; row < 5 ; row ++) {
            for(int x = 0, y = row ; x < 6 - row ; y ++, x ++) {
                if(board[y][x] == p) {
                    int tempStreak = 0;
                    while(x < 7 && y < 6 && board[y][x] == p) {
                        x ++;
                        y ++;
                        tempStreak ++;
                    }

                    if(tempStreak == 2)
                        twos ++;
                    if(tempStreak == 3) {
                        if(x + 1 < 7 && y + 1 < 6 && board[y + 1][x + 1] == 0 && y - 3 >= 0 && x - 3 >= 0 && board[y - 3][x - 3] == 0) threes += 10;
                        else if(x + 1 < 7 && y + 1 < 6 && board[y + 1][x + 1] == 0 && y + 1 == 5) threes += 7;
                        else if(x + 1 < 7 && y + 1 < 6 && board[y + 1][x + 1] == 0) threes += 3;
                        else if(y - 3 >= 0 && x - 3 >= 0 && board[y - 3][x - 3] == 0) threes += 3;
                        else threes ++;
                    }

                    if(tempStreak >= 4)
                        fours ++;
                }
            }
        }
        return new int[] {twos, threes, fours};
    }
    private static int[] ViceDiagonallyAboveHalf(int p, int[][] board) {
        int twos = 0, threes = 0, fours = 0;
        for(int row = 1 ; row < 6 ; row ++) {
            for(int x = 0, y = row; y >= 0 && x < row + 1 ; y --, x ++) {
                if(board[y][x] == p) {
                    int tempStreak = 0;
                    while(y >= 0 && x < 7 && board[y][x] == p) {
                        y --;
                        x ++;
                        tempStreak ++;
                    }
                    if(tempStreak == 2)
                        twos ++;
                    if(tempStreak == 3) {
                        if(y + 3 < 6 && x - 3 >= 0 && board[y+3][x-3] == 0 && y - 1 >= 0 && x + 1 < 7 && board[y-1][x+1] == 0) threes += 10;
                        else if(y + 3 < 6 && x - 3 >= 0 && board[y+3][x-3] == 0 && y + 3 == 5) threes += 7;
                        else if(y + 3 < 6 && x - 3 >= 0 && board[y+3][x-3] == 0) threes += 3;
                        else if(y - 1 >= 0 && x + 1 < 7 && board[y-1][x+1] == 0) threes += 3;
                        else threes++;
                    }
                    if(tempStreak >= 4)
                        fours ++;
                }
            }
        }
        return new int[] {twos, threes, fours};
    }
    private static int[] ViceDiagonallyBelowHalf(int p, int[][] board) {
        int twos = 0, threes = 0, fours = 0;
        for(int col = 1 ; col < 7 ; col ++) {
            for(int y = 5, x = col; y >= 0 && x < 7; y --, x ++) {
                if(board[y][x] == p) {
                    int tempStreak = 0;
                    while(y >= 0 && x < 7 && board[y][x] == p) {
                        y --;
                        x ++;
                        tempStreak ++;
                    }
                    if(tempStreak == 2)
                        twos ++;
                    if(tempStreak == 3) {
                        if(y + 3 < 6 && x - 3 >= 0 && board[y+3][x-3] == 0 && y - 1 >= 0 && x + 1 < 7 && board[y-1][x+1] == 0) threes += 10;
                        else if(y + 3 < 6 && x - 3 >= 0 && board[y+3][x-3] == 0 && y + 3 == 5) threes += 7;
                        else if(y + 3 < 6 && x - 3 >= 0 && board[y+3][x-3] == 0) threes += 3;
                        else if(y - 1 >= 0 && x + 1 < 7 && board[y-1][x+1] == 0) threes += 3;
                        else threes++;
                    }
                    if(tempStreak >= 4)
                        fours ++;
                }
            }
        }
        return new int[] {twos, threes, fours};
    }

    private static int HorizonallySpecial(int p, int[][] board) {
        int worstThrees = 0;
        for(int i = 0 ; i < 6 ; i ++) {
            for (int j = 0; j < 7; j++) {
                if(board[i][j] == p && j + 3 < 7 && board[i][j + 2] == p && board[i][j + 3] == p && board[i][j + 1] == 0 && (i == 0 || board[i-1][j+1]!=0)) worstThrees++;
                else if(board[i][j] == p && j + 3 < 7 && board[i][j + 1] == p && board[i][j + 3] == p && board[i][j + 2] == 0 && (i == 0 || board[i-1][j+2]!=0)) worstThrees ++;
            }
        }
        return worstThrees;
    }


    /**
     * For generating random testcases
     * @return
     */
    private static String generateRandomTc() {

        String faketc = "";
        Random r = new Random();

        String state = "";
        for(int i = 0 ; i < 6 ; i ++) {
            for(int j = 0 ; j < 7 ; j ++) {
                int yOrr = r.nextInt(2);
                if(yOrr == 1) {
                    faketc += "y";
                } else {
                    faketc += "r";
                }
            }
            if(i != 5) faketc += ",";
        }


        int[][] Board = convertToBoard(faketc);

        for(int i = 0 ; i < 7 ; i ++) {
            int depth = r.nextInt(6);

            for(int j = 0 ; j <= depth ; j ++) {
                Board[j][i] = 0;
            }
        }

        if(yellowWins(Board) || redWins(Board)) {
            return null;
        }
//        else {
//            displayBoard(Board);
//        }

        String tc = "";

        for(int i = 5 ; i >= 0 ; i --) {
            for(int j = 0 ; j < 7 ; j ++) {
                if(Board[i][j] == 0)
                    tc += ".";
                if(Board[i][j] == 1)
                    tc += "r";
                if(Board[i][j] == 2)
                    tc += "y";
            }
            if(i != 0) tc += ",";
        }


        int rorY = r.nextInt(2);
        if(rorY == 1) {
            tc += " red";
        } else {
            tc += " yellow";
        }
//
//        int AorM = r.nextInt(2);
//        if(AorM == 1) {
//            tc += " A";
//        } else {
//            tc += " M";
//        }
//
//        tc += " " + (r.nextInt(6) + 1);

//        System.out.println(tc);
        return tc;
    }

}