import java.util.ArrayList;
import java.time.Year;
import java.util.*;
import java.util.Random;
class Minesweeper {
  private int[][] hiddenB; // hidden board
  private int[][] shownB; // board shown to users
  private boolean[][] switchB = {
    {false,false,false,false,false,false,false,false,false},
    {false,false,false,false,false,false,false,false,false},
    {false,false,false,false,false,false,false,false,false},
    {false,false,false,false,false,false,false,false,false},
    {false,false,false,false,false,false,false,false,false},
    {false,false,false,false,false,false,false,false,false},
    {false,false,false,false,false,false,false,false,false},
    {false,false,false,false,false,false,false,false,false},
    {false,false,false,false,false,false,false,false,false},
  }; // board full of booleans indicating whether or not a value should be shown to users
  // true = display #
  // false = placeholder
  private Scanner input; // user input
  private int targetR;
  private int targetC;
  
  public void createGrid() // creates a new Grid
  {
    hiddenB = new int[9][9];
    shownB = new int[9][9];

    // put bombs in random locations in the grid
    for (int i=0; i<10; i++){
      Random random = new Random();
      int randomC = (random.nextInt(9)); //random 0 to 9
      int randomR = (random.nextInt(9)); //random 0 to 9

      if (hiddenB[randomC][randomR]==9){ // if there is already a bomb
        i--;
      }
      else{
        hiddenB[randomC][randomR] = 9;
      }
    }
  }

  public void computeNums(){
    // go through the board
    // detect bombs
    // call function for everything around the bombs
    // if true then add 1
    for(int i =0; i<9; i++){
      for(int j = 0; j<9; j++){
        // check if it is a bomb
        if (hiddenB[i][j] == 9){
          // top left
          if (detectValidCell(i-1,j-1)){
            hiddenB[i-1][j-1] ++;
          }
          // top
          if (detectValidCell(i-1, j)){
            hiddenB[i-1][j] ++;
          }
          // top right
          if (detectValidCell(i-1, j+1)){
            hiddenB[i-1][j+1] ++;
          }
          //left
          if (detectValidCell(i, j-1)){
            hiddenB[i][j-1] ++;
          }
          //right
          if (detectValidCell(i, j+1)){
            hiddenB[i][j+1] ++;
          }
          //bottom left
          if (detectValidCell(i+1, j-1)){
            hiddenB[i+1][j-1] ++;
          }
          //bottom
          if (detectValidCell(i+1, j)){
            hiddenB[i+1][j] ++;
          }
          //bottom right
          if (detectValidCell(i+1, j+1)){
            hiddenB[i+1][j+1] ++;
          }
        }
      }
    }
    
  }

  private boolean detectValidCell(int x, int y){
    // in bounds
    // is not a bomb
    if (x<0 || y<0 || x>8 || y>8){
      return false;
    }
    if (hiddenB[x][y]==9){
      return false;
    }
    return true;
    
  }
  

  public void userInput(){ // gets user input & stores it in variables
    Scanner input = new Scanner(System.in);
    System.out.println("");
    System.out.println("Enter row:");
    targetR = input.nextInt()-1; // enter target row

    System.out.println("Enter column:");
    targetC = input.nextInt()-1; // enter target column

  } // gets userInput & stores in targetR and targetC


  public void displayGrid(boolean all){
    // display the screen (displays shown grid)
    System.out.println("   1   2   3   4   5   6   7   8   9");
    System.out.println("  -------------------------------------");
    for (int i = 0; i<9; i++){
      System.out.print(i+1);
      System.out.print("| ");
      // middle values in rows
      // check
      for(int j = 0; j<8; j++){
        if (switchB[i][j] || all)// if we display hidden
        {
        System.out.print(hiddenB[i][j]);
      }
        else // if its just a placeholder
        { 
        System.out.print(" ");
      }
        System.out.print(" | ");
      }
      // last value in row
      // check
      if (switchB[i][8] || all)// if we show
        {
        System.out.print(hiddenB[i][0]);
      }
        else // if its just a placeholder
        { 
        System.out.print(" ");
      }
      System.out.print(" |");
      System.out.println("");
    }
    System.out.print("  -------------------------------------");
  } // displays hidden if switch is true

  public class IntPair {
    public int x;
    public int y;
    
    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }
  }
  
  public void addEmptyCell(ArrayList<IntPair> emptyCells, int i, int j){
    if (i < 0 || j < 0 || i >8 || j > 8){
      return;
    }
    // top left
      if (hiddenB[i][j] == 0 && switchB[i][j] == false){
        IntPair temp = new IntPair(i,j);
        emptyCells.add(temp);
      }
      else{
        switchB[i][j] = true;
      }
  }
  
  public void processEmptyCells(int i, int j){
    //take cell and process cell
    // process empty cells around it
    ArrayList<IntPair> emptyCells = new ArrayList<IntPair>();
    IntPair pair = new IntPair(i, j);
    emptyCells.add(pair);
    while (emptyCells.size() > 0){
      pair = emptyCells.remove(0);
      int x = pair.x;
      int y = pair.y;
      switchB[x][y] = true;
      //top left
      addEmptyCell(emptyCells, x-1,y-1);
      //top 
      addEmptyCell(emptyCells, x-1,y);
      //top right
      addEmptyCell(emptyCells, x-1,y+1);
      //left
      addEmptyCell(emptyCells, x,y-1);
      //right
      addEmptyCell(emptyCells, x,y+1);
      //bottom left
      addEmptyCell(emptyCells, x+1,y-1);
      //bottom
      addEmptyCell(emptyCells, x+1,y);
      //bottom right
      addEmptyCell(emptyCells, x+1,y+1);
      
    }
  }
  

  public boolean continueGame(){
    if (hiddenB[targetR][targetC] == 9){
      return false;
    }
    else{
      return true;
    }
  } // checks if lost

  public boolean wonGame(){
    boolean won = false;
    // go through the hidden grid & look at the switches
    // if the switches are false and all of them are 9s, then you've won
    for (int r=0; r<9; r++){
      for(int c=0; c<9; c++){
        if (switchB[r][c]==true && hiddenB[r][c] != 9){
          won = true;
        }
        else{
          won = false;
        }
      }
    }
    return won;
  }

  public void playGame(){
    // setting up switchBoard
    for (int i = 0; i<9; i++){
      for (int j = 0; j<9; j++){
        switchB[i][j] = false;
      }
    }
    this.createGrid();
    this.computeNums();
    this.displayGrid(true);
    this.displayGrid(false);
    // first set up board
    // print user prompt
    System.out.println(" ");
    System.out.println("Hello and welcome to Minesweeper!");
    System.out.println("Please select a row and column to start.");
    boolean player = true;
    while (player){ // plays game
      // continue Game?
      this.userInput();

      if (this.continueGame() == false) {
        System.out.print("You've been bombed! Game Over.");
        break;
      }
      if (this.wonGame() == true){
        System.out.println("Yay! You've won the game.");
      }
      // if true, update & display Grid & continue cycle
      // update switchBoard
      if (hiddenB[targetR][targetC]==0){
        System.out.print("running: " + String.format("%d", targetR) + " " + String.format("%d", targetC));

        
        this.processEmptyCells(targetR,targetC);
      }
      else{
        
        switchB[targetR][targetC] = true;
      }
      this.displayGrid(false);

      }
    }

  
  public void Minesweeper(){
    this.playGame();
    //create grid
    // display grid
    // ask user for input (row then column)
    // decide if game is over & end game
    // if not over then display and ask for another input
    
  }
  
}


class Main {
  public static void main(String[] args) {
    System.out.println("Hello world!");
    Minesweeper hello = new Minesweeper();
    hello.playGame();

  }
}
