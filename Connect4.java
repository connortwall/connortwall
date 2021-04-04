import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;

/** Connect 4.
 *  Connor Wall
 *  cwall6
 *  March 26th, Spring 2021
 */
public class Connect4 {
   
   public static void main(String[] args) throws IOException {
      Random rnd = new Random(0);
      Scanner kb = new Scanner(System.in);
      Scanner inFS = null;
      int rows;
      int cols;
      char[][] board = {{'R', 'Y', 'R'}, {'Y', 'R', 'Y'}, {'R', 'Y', 'R'}};
      boolean redTurn = false;
     // saveGame(board, "gametest.txt", false);
   
      while (true) {
         printMenu();
         String input = kb.nextLine();
         if (input.length() != 1) {
            System.out.println("Invalid choice!");
         }
         else {
            switch (input.charAt(0)) {
               case 'N':
               case 'n':
               
                  board = choiceNew(kb);
                  redTurn = rnd.nextInt(2) == 0;
                  break;
            
               case 'L':
               case 'l':
                  System.out.print("Enter filename: ");
                  String infile = kb.nextLine();
                  FileInputStream fileByteStream = new FileInputStream(infile);
                  inFS = new Scanner(fileByteStream);
                  rows = inFS.nextInt();
                  cols = inFS.nextInt();
                  inFS.nextLine();
                  board = createBoard(rows, cols);
                  redTurn = loadGame(board, inFS);
                  
                  break;
            
               case 'Q':
               case 'q':
                  kb.close();
                  return;
               default:
                  System.out.println("Invalid choice!");
                  continue;
            }
            int result = play(board, kb, redTurn);
            switch (result) {
               case 0:
                  System.out.println("Congrats, red wins!");
                  break;
               case 1:
                  System.out.println("Congrats, yellow wins!");
                  break;
               case 2:
                  System.out.println("Game is drawn!");
                  break;
               case -1:
                  System.out.print("Enter filename: ");
                  String fileName = kb.nextLine();
                  saveGame(board, fileName, redTurn);
               default:
                  continue;
            
            }
         }
      }
   }
   /** This method creates a new board from menu input in main.
    * @param kb scanner from main
    * @return initialized board with dimensions row and col
    */
    
   public static char[][] choiceNew(Scanner kb) {
      char[][] board;
      int cols;
      int rows;
      System.out.print("Enter number of columns: ");
      while (true) {
         if (kb.hasNextInt()) {
            cols = kb.nextInt();
            if (cols >= 4 && cols <= 11) {
               break;
            }
         } else {
            kb.nextLine();
         }
         System.out.print("Error, enter number of columns: ");
      }
   
      System.out.print("Enter number of rows: ");
      while (true) {
         if (kb.hasNextInt()) {
            rows = kb.nextInt();
            if (rows >= 4 && rows <= 11) {
               break;
            }
         } else {
            kb.nextLine();
         }
         System.out.print("Error, enter number of rows: ");
      }
      board = createBoard(rows, cols);
      initBoard(board);
      return board;
   }

   /** This method creates a board.
   * @param rows decided in main
   * @param cols decided in main
   * @return board with dimensions row and col
   */
   public static char[][] createBoard(int rows, int cols) {
      char[][] board = new char[rows][cols];
      return board;
   }
   /** This method controls the entire game flow. As long as the game is being 
    * player by the players and "save" (i.e., -1) choice is not selected, the 
    * game continues. The flow stops when either the game ends 
    * (i.e., a player wins) or because -1 is entered to save the game.
    * @param board a 2D array which is the game's board representing 
    * the current state of the game
    * @param kb the scanner to be used to collect user inputs from 
    * standard input
    * @param redTurn a boolean indicating if it is red's player to move or not
    * @return -1 if save is selected, 0 if red wins, 1 if yellow wins,
    *          2 if the game is drawn.
    * @throws IOException
    */
    
   public static int play(char[][] board, Scanner kb, 
                     boolean redTurn) throws IOException {
      
      while (true) {
         printBoard(board);
         switch (isGameOver(board)) {
            case 0:
               printBoard(board);
               return 0;
            case 1:
               printBoard(board);
               return 1;
            case 2:
               printBoard(board);
               return 2;
            default:
               break;
         }
         System.out.printf("%s's turn, enter the move (-1 to save): ",
                           redTurn ? "red" : "yellow");
         if (!(kb.hasNextInt())) {
            kb.nextLine();
            continue;
         }
         int index = kb.nextInt();
         kb.nextLine();
         if (index == -1) {
            return -1;
         }
         if (index < 0 || index >= board[0].length) {
            continue;
         }
         if (!(move(board, index, redTurn))) {
            continue;
         }
         redTurn = !redTurn;
         
      
         
      }
      
   }

   /** This method plays a move on the board by adding a new piece 
    * to one of the columns on the correct row which is lowest 
    * empty row on the chosen column.
    * @param board the game board
    * @param index the index of the column a peice is being added
    * @param redTurn a boolean indicating which player's piece is being played
    * @return true if a piece is added successfully added on the 
    * board, false otherwise
    */
   public static boolean move(char[][] board, int index, boolean redTurn) {
      for (int i = board[index].length - 1; i >= 0; --i) {
         if (board[i][index] == '-') {
            board[i][index] = redTurn ? 'R' : 'Y';
            return true;
         }
      }
      return false; // REPLACE ME
   }

   /** This method prints the menu.
    * DO NOT CHANGE THIS!
    */
   public static void printMenu() {
      System.out.println();
      System.out.println("n/N: New game");
      System.out.println("l/L: Load a game");
      System.out.println("q/Q: Quit");
      System.out.println("-------------");
      System.out.print("Enter your choice: ");
   }

   /** This method initialize the board to all empty cells.
    * DO NOT CHANGE THIS!
    * @param board the board
    */
   public static void initBoard(char[][] board) {
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            board[i][j] = '-';
         }
      }
   }

   /** This method prints the board.
    * DO NOT CHANGE THIS!
    * @param board the board
    */
   public static void printBoard(char[][] board) {
      for (int i = 0; i < board.length; i++) {
         for (int j = 0; j < board[0].length; j++) {
            System.out.print(board[i][j] + " ");
         }
         System.out.println();
      }
   }

   /** This method saves the game in an external text file.
    *
    * @param board the current board
    * @param fileName the file name the game is going to be saved to
    * @param redTurn which player's turn it is next time the game will resume
    * @throws IOException
    */
   public static void saveGame(char[][] board, String fileName, 
                      boolean redTurn) throws IOException {
      
      int rows = board.length;
      int cols = board[0].length;
      
      //String outfile = "game2.txt";
      FileOutputStream fileStream = new FileOutputStream(fileName);
      PrintWriter outFS = new PrintWriter(fileStream);
      
      outFS.println(rows + " " + cols);
      for (int r = 0; r < board.length; ++r) {
         for (int c = 0; c < board[0].length; ++c) {
            outFS.print(board[r][c]);
         }
         outFS.println();
      }
      
      if (redTurn) {
         outFS.print("R");
      } else {
         outFS.print("Y");
      }

      outFS.close();
      
   }

   /** This method loads a game from a saved file. The board must be filled
    * according to the content of the file and whose turn it is to make a move
    * is also read and returned as a boolean from this method.
    * @param board the game board
    * @param inFS the open scanner associated with the file the game 
    * is being loaded from
    * @return true if it is red to move, false otherwise
    * @throws IOException
    */
   public static boolean loadGame(char[][] board, 
                 Scanner inFS) throws IOException {
      int rowIndex = 0;
      while (inFS.hasNextLine()) {
         String row = inFS.nextLine();
         if (row.length() != board.length) {
            return row.charAt(0) == 'R';
         }
         for (int colIndex = 0; colIndex < row.length(); ++colIndex) {
            board[rowIndex][colIndex] = row.charAt(colIndex);
         
         
         }
         ++rowIndex;
      }
      throw new IOException("bad file format"); // REPLACE ME
   }

   /** This method decides whether the game is over or not. The game is 
    * over if the player as indicated by red wins the game based on the 
    * current state of the board. The method can also tell if the game 
    * is drawn or not.
    *
    * @param board the game board
    * @return 0 if red wins the game, 1 if
    *         yellow wins, 2 if draw, 3 otherwise.
    */
   public static int isGameOver(char[][] board) {
   
      int result = rowTest(board);
      if (result != 3) {
         return result;
      }
   
      result = columnTest(board);
      if (result == 0 || result == 1) {
         return result;
      }
      result = diagonalTest(board);
      if (result == 0 || result == 1) {
         return result;
      }
      return 3;
   }

   /** This method checks for row 4 in a row.
    * @param board the board
    * @return game winner
    */
   public static int rowTest(char[][] board) {
      
      int emptyCount = 0;
      //row test
      for (int r = 0; r < board.length; ++r) {
         int yellowCount = 0;
         int redCount = 0;
         for (int c = 0; c < board[0].length; ++c) {
            char slot = board[r][c];
            if (slot == 'Y') {
               yellowCount += 1;
               redCount = 0;
            }
            else if (slot == 'R') {
               redCount += 1;
               yellowCount = 0;
            }
            else {
               emptyCount += 1;
               yellowCount = 0;
               redCount = 0;
            }
            if (yellowCount == 4) {
               return 1;
            }
            if (redCount == 4) {
               return 0;
            }
         }
      }
      return emptyCount == 0 ? 2 : 3;
   }

   /** This method checks for column 4 in a row.
    * @param board the board
    * @return game winner
    */
   public static int columnTest(char[][] board) {
   
      //column test
      for (int c = 0; c < board[0].length; ++c) {
         int yellowCount = 0;
         int redCount = 0;
         for (int r = 0; r < board.length; ++r) {
            char slot = board[r][c];
            if (slot == 'Y') {
               yellowCount += 1;
               redCount = 0;
               if (yellowCount == 4) {
                  return 1;
               }
            }
            else if (slot == 'R') {
               redCount += 1;
               yellowCount = 0;
               if (redCount == 4) {
                  return 0;
               }
            }
            else {
               yellowCount = 0;
               redCount = 0;
            }
             
            
         }
      }
      return 3;
   }
   
   /** This method checks for diagonal 4 in a row up.
    * @param board the board
    * @return game winner
    */
   public static int diagonalTest(char[][] board) {
      //diagonal test
      for (int c = 0; c < board[0].length - 3; ++c) {
         for (int r = 0; r < board.length; ++r) {
            for (int offset = 0; offset < 4; ++offset) {
               int result = diagonalUp(board, c, r);
               if (result == 0 || result == 1) {
                  return result;
               }
            
               if (r - 3  >= 0) {
                  char slot0 = board[r][c];
                  char slot1 = board[r - 1][c + 1];
                  char slot2 = board[r - 2][c + 2];
                  char slot3 = board[r - 3][c + 3];
               
                  if (slot0 == slot1 && slot0 == slot2 && slot0 == slot3) {
                     if (slot0 == 'R') {
                        return 0;
                     }
                     if (slot0 == 'Y') {
                        return 1;
                     }
                  }
               }
            }
         }
      }
      return 3;
   }

   /** This method checks for diagonal 4 in a row down.
    * @param board the board
    * @param c columns in board
    * @param r rows in board
    * @return game winner
    */
    
   public static int diagonalUp(char[][] board, int c, int r) {
      if (r + 3 < board.length) {
         char slot0 = board[r][c];
         char slot1 = board[r + 1][c + 1];
         char slot2 = board[r + 2][c + 2];
         char slot3 = board[r + 3][c + 3];
      
         if (slot0 == slot1 && slot0 == slot2 && slot0 == slot3) {
            if (slot0 == 'R') {
               return 0;
            }
            if (slot0 == 'Y') {
               return 1;
            }
         }
      }
      return 3;
   }

}
