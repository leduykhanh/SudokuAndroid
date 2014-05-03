package com.example.sudokututor1;


import com.example.sudokututor.R;
import com.example.sudokututor1.Complete;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Game extends Activity {
   private static final String TAG = "Sudoku";
   public static final String KEY_DIFFICULTY =
      "com.example.sudokututor.difficulty";
   public static final int DIFFICULTY_EX_EASY = 0;
   public static final int DIFFICULTY_EASY = 1;
   public static final int DIFFICULTY_MEDIUM = 2;
   public static final int DIFFICULTY_HARD = 3;
   public static final int DIFFICULTY_EVIL = 4;
   public static final int BLANK = 5;


   private int puzzle[] = new int[9 * 9];

   private final String blankPuzzle = 
	   "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
   
   private final String exEasyPuzzle =
	   "039070008752410030000903472360085240820000017090742600005860304608304000903007861";
   private final String easyPuzzle =
	   "070020406060310029500000708050700204007806900406002080209000005640089070701030090";
   private final String mediumPuzzle =
      "650000070000506000014000005007009000002314700000700800500000630000201000030000097";
   
   private final String hardPuzzle =
      "100020007050009000009107540870000000003090700000000014091704200000300090300060008";
   
   private final String evilPuzzle =
      "000900001003060008005008000084300100100000004009007580000100300700050900200004000";
   int model[][];

   
   //Jangkoo
   
   
   private PuzzleView puzzleView;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.d(TAG, "onCreate");

      int diff = getIntent().getIntExtra(KEY_DIFFICULTY,
            BLANK);
      puzzle = getPuzzle(diff);
      calculateUsedTiles();

      puzzleView = new PuzzleView(this, null);
      
      puzzleView.requestFocus();
      puzzleView.setWillNotDraw (false);
      setContentView(R.layout.game) ;
      
      //Jangkoo
      
      Button solve = (Button) findViewById(R.id.solve_sudoku);
      solve.setOnClickListener(new View.OnClickListener(){
          @Override
			public void onClick(View v) {
          	
             solveSudoku();
          }});
      //setContentView(puzzleView);
     
   }
   // ...
   

   
   /** Given a difficulty level, come up with a new puzzle */
   private int[] getPuzzle(int diff) {
      String puz;
      // TODO: Continue last game
      switch (diff) {
      case DIFFICULTY_EVIL:
          puz = evilPuzzle;
          break;
      case DIFFICULTY_HARD:
         puz = hardPuzzle;
         break;
      case DIFFICULTY_MEDIUM:
         puz = mediumPuzzle;
         break;
      case DIFFICULTY_EASY:
         puz = easyPuzzle;
         break;
      case DIFFICULTY_EX_EASY:
         puz = exEasyPuzzle;
         break;
      case BLANK:
      default:
          puz = blankPuzzle;
          break;
         
      }
      return fromPuzzleString(puz);
   }
   

   
   /** Convert an array into a puzzle string */
   static private String toPuzzleString(int[] puz) {
      StringBuilder buf = new StringBuilder();
      for (int element : puz) {
         buf.append(element);
      }
      return buf.toString();
   }

   /** Convert a puzzle string into an array */
   static protected int[] fromPuzzleString(String string) {
      int[] puz = new int[string.length()];
      for (int i = 0; i < puz.length; i++) {
         puz[i] = string.charAt(i) - '0';
      }
      return puz;
   }
   

   
   /** Return the tile at the given coordinates */
   public int getTile(int x, int y) {
      return puzzle[y * 9 + x];
   }

   /** Change the tile at the given coordinates */
   private void setTile(int x, int y, int value) {
      puzzle[y * 9 + x] = value;
   }
   

   
   /** Return a string for the tile at the given coordinates */
   protected String getTileString(int x, int y) {
      int v = getTile(x, y);
      if (v == 0)
         return "";
      else
         return String.valueOf(v);
   }
   
   /****** Check to see if the game is complete **/
   protected boolean isSolved() {
       for (int element : puzzle) {
           if (element == 0)
               return false;
       }
       return true;
   }
   
   /** Change the tile only if it's a valid move */
   protected boolean setTileIfValid(int x, int y, int value) {
      int tiles[] = getUsedTiles(x, y);
	  //for(int k : tiles) {
		// Log.e("0 0 ", x+" "+y+" "+k + " " + value);
	  //}
       if (value != 0) {
         for (int tile : tiles) {
            if (tile == value){
              // Log.e("false at",tile + "");
            	return false;
               
            }
         }
      }
      setTile(x, y, value);
      calculateUsedTiles();
    //check if the game is complete after each valid move
      /*if (isSolved() == true) { 
          Intent complete = new Intent(this, Complete.class); 
          startActivity(complete);} 
          else
          {
              return false;
          }*/
return true;
}
   
   

   
   /** Open the keypad if there are any valid moves */
   protected void showKeypadOrError(int x, int y,PuzzleView p) {
      int tiles[] = getUsedTiles(x, y);
      if (tiles.length == 9) {
         Toast toast = Toast.makeText(this,
               R.string.no_moves_label, Toast.LENGTH_SHORT);
         toast.setGravity(Gravity.CENTER, 0, 0);
         toast.show();
      } else {
         Log.d(TAG, "showKeypad: used=" + toPuzzleString(tiles));
         Dialog v = new Keypad(this, tiles, p);
         v.show();
      }
   }
   

   
   /** Cache of used tiles */
   private final int used[][][] = new int[9][9][];

   /** Return cached used tiles visible from the given coords */
   protected int[] getUsedTiles(int x, int y) {
      return used[x][y];
   }
   

   
   /** Compute the two dimensional array of used tiles */
   private void calculateUsedTiles() {
      for (int x = 0; x < 9; x++) {
         for (int y = 0; y < 9; y++) {
            used[x][y] = calculateUsedTiles(x, y);
             Log.d(TAG, "used[" + x + "][" + y + "] = "
             + toPuzzleString(used[x][y]));
         }
      }
   }
   

   
   /** Compute the used tiles visible from this position */
   private int[] calculateUsedTiles(int x, int y) {
      int c[] = new int[9];
      // horizontal
      for (int i = 0; i < 9; i++) { 
         if (i == y)
            continue;
         int t = getTile(x, i);
         if (t != 0)
            c[t - 1] = t;
      }
      // vertical
      for (int i = 0; i < 9; i++) { 
         if (i == x)
            continue;
         int t = getTile(i, y);
         if (t != 0)
            c[t - 1] = t;
      }
      // same cell block
      int startx = (x / 3) * 3; 
      int starty = (y / 3) * 3;
      for (int i = startx; i < startx + 3; i++) {
         for (int j = starty; j < starty + 3; j++) {
            if (i == x && j == y)
               continue;
            int t = getTile(i, j);
            if (t != 0)
               c[t - 1] = t;
         }
      }
      // compress
      int nused = 0; 
      for (int t : c) {
         if (t != 0)
            nused++;
      }
      int c1[] = new int[nused];
      nused = 0;
      for (int t : c) {
    	  if (t != 0)
            c1[nused++] = t;
      }
      return c1;
   }
   
   int backButtonCount=0;
   @Override
public void onBackPressed()
   {
	  
	   backButtonCount++;
	   
       if(backButtonCount>= 2)
       {   finish();
           Intent intent = new Intent(this, Sudoku.class);
           startActivity(intent);
           backButtonCount=0;
       }
       else
       {
           Toast.makeText(this, "Press the back button once again to return to the menu.", Toast.LENGTH_LONG).show();
       }
       
   }
   // Jangkoo added
   protected boolean isValid(int i, int j, int value)
   {
	      int tiles[] = getUsedTiles(i, j);
	       if (value != 0) {
	         for (int tile : tiles) {
	            if (tile == value){
	                  	return false;
	                     }
	         }
	      }
	   return true;
   }
   public void solveSudoku(){
	   Log.e("sovled?","");
	   
	   model = new int[9][9];

	   // Clear all cells
	   for( int row = 0; row < 9; row++ )
	      for( int col = 0; col < 9; col++ )
	         model[row][col] = 0 ;

	   // Create the initial situation
	   model[0][0] = 9 ;
	   model[0][4] = 2 ;
	   model[0][6] = 7 ;
	   model[0][7] = 5 ;

	   model[1][0] = 6 ;
	   model[1][4] = 5 ;
	   model[1][7] = 4 ;

	   model[2][1] = 2 ;
	   model[2][3] = 4 ;
	   model[2][7] = 1 ;

	   model[3][0] = 2 ;
	   model[3][2] = 8 ;

	   model[4][1] = 7 ;
	   model[4][3] = 5 ;
	   model[4][5] = 9 ;
	   model[4][7] = 6 ;

	   model[5][6] = 4 ;
	   model[5][8] = 1 ;

	   model[6][1] = 1 ;
	   model[6][5] = 5 ;
	   model[6][7] = 8 ;

	   model[7][1] = 9 ;
	   model[7][4] = 7 ;
	   model[7][8] = 4 ;

	   model[8][1] = 8 ;
	   model[8][2] = 2 ;
	   model[8][4] = 4 ;
	   model[8][8] = 6 ;
	   //if (!solve(0,0)) Log.e("sovled?","");
	   try {
		   if (!solve(0,0)) Log.e("sovled?","" + solve(0,0,puzzle));
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	      	 /* Handler mHandler = new Handler();
    	      Runnable codeToRun = new Runnable() {
    	          @Override
    	          public void run() {
    	        	  //pz.invalidate();  
    	        	  try {
						Log.e("sovled?",solve(0,0,puzzle)+"");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

    	          }
    	      };
    	     mHandler.postDelayed(codeToRun, 1000);
		*/
		

	   //this.puzzle[0] = 1;
	  
	   /*solve(0,0);
	      for( int r = 0; r <9; r++ )
	          for( int c = 0; c < 9; c++ )
	        	  puzzle[r*9 +c] = model[r][c];*/

   }
   private boolean solve(int i,int j,int[] cells)throws  Exception {
	  if (i == 9) {
           i = 0;
            if (++j == 9)
           {
        	return true;
           }
       }
	   if (isSolved()) {
		
		   return true;
	   }
       if (cells[i*9 +j] != 0)  // skip filled cells
           //return solve(i+1,j,cells);
    	   next(i,j,cells);

       for (int val = 1; val <= 9; ++val) {
    	   
           if (isValid(i,j,val)) {
        	   Log.e("solving",i +""+j +""+cells[i*9 +j] + "" +setTileIfValid(i,j,val)+""+val);
        	   cells[i*9 +j] = val;
	      	      PuzzleView pz = (PuzzleView) findViewById(R.id.puzzleView1);
	     	      pz.invalidate();

               //if (solve(i+1,j,cells))
        	   next(i,j,cells);
                  // return true;
           }
       }
       cells[i*9 +j] = 0; // reset on backtrack
       return false;
   }
   public void next(int i,int j,int[] cells) throws  Exception{
	   if(j<8) solve(i,j+1,cells);
	   else solve(i+1,0,cells);
	   
   }
   /** Checks if num is an acceptable value for the given row */
   protected boolean checkRow( int row, int num )
   {
      for( int col = 0; col < 9; col++ )
         if( model[row][col] == num )
            return false ;

      return true ;
   }

   /** Checks if num is an acceptable value for the given column */
   protected boolean checkCol( int col, int num )
   {
      for( int row = 0; row < 9; row++ )
         if( model[row][col] == num )
            return false ;

      return true ;
   }

   /** Checks if num is an acceptable value for the box around row and col */
   protected boolean checkBox( int row, int col, int num )
   {
      row = (row / 3) * 3 ;
      col = (col / 3) * 3 ;

      for( int r = 0; r < 3; r++ )
         for( int c = 0; c < 3; c++ )
         if( model[row+r][col+c] == num )
            return false ;

      return true ;
   }
   /** Recursive function to find a valid number for one single cell */
   public boolean solve( int row, int col ) 
   {
      // Throw an exception to stop the process if the puzzle is solved
      if( row > 8 )
         return true;

      // If the cell is not empty, continue with the next cell
      if( model[row][col] != 0 )
         return next( row, col ) ;
      else
      {
         // Find a valid number for the empty cell
         for( int num = 1; num < 10; num++ )
         {
            if( checkRow(row,num) && checkCol(col,num) && checkBox(row,col,num) )
            {
               model[row][col] = num ;
              // Log.e("solved","" + row+" " + col + " " + num );

               // Let the observer see it
              // Thread.sleep( 1000 ) ;

               // Delegate work on the next cell to a recursive call
               next( row, col ) ;
            }
         }

         // No valid number was found, clean up and return to caller
         model[row][col] = 0 ;
         return false;
         
      }
   }

   /** Calls solve for the next cell */
   public boolean next( int row, int col ) 
   {
      if( col < 8 )
         return solve( row, col + 1 ) ;
      else
         return solve( row + 1, 0 ) ;
   }
   //end added
 
}


