package com.example.sudokututor1;


import com.example.sudokututor.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
public class PuzzleView extends View 
{
private static final String TAG = "Sudoku";
private float width;    // width of one tile
private float height;   // height of one tile
private int selX;       // X index of selection
private int selY;       // Y index of selection
public int justSelectedValue = 0;
private final Rect selRect = new Rect();
private final Game game;
public PuzzleView(Context context,  AttributeSet attributeSet)
{
	
super(context, attributeSet);	
this.game = (Game) context;
      setFocusable(true);
      setFocusableInTouchMode(true);
      setWillNotDraw (false);
      
      
}

@Override
protected void onSizeChanged(int w, int h, int oldw, int oldh) 
{
      width = w / 9f;
      height = h / 9f;
      getRect(selX, selY, selRect);
      Log.d(TAG, "onSizeChanged: width " + width + ", height "
            + height);
      super.onSizeChanged(w, h, oldw, oldh);
}
@Override
protected void onDraw(Canvas canvas) 
{
	      // Draw the background...
	
      Paint background = new Paint();
      background.setColor(getResources().getColor(
            R.color.puzzle_background));
      canvas.drawRect(0, 0,720,720, background);
      // Draw the board...
      // Define colors for the grid lines
      Paint dark = new Paint();
      dark.setColor(getResources().getColor(R.color.puzzle_dark));
      dark.setStrokeWidth(4);
      Paint hilite = new Paint();
      hilite.setStrokeWidth(0);
      hilite.setColor(getResources().getColor(R.color.puzzle_hilite));
      Paint light = new Paint();
      light.setColor(getResources().getColor(R.color.puzzle_light));
      light.setStrokeWidth(0);
      // Draw the minor grid lines
for (int i = 0; i <=9; i++) {
      canvas.drawLine(0, i * height, getHeight(), i * height,
               light);
      canvas.drawLine(0, i * height + 1, getHeight(), i * height
               + 1, hilite);
      canvas.drawLine(i * width, 0, i * width, getWidth(),
               light);
      canvas.drawLine(i * width + 1, 0, i * width + 1,
    		  getWidth(), hilite);
}

	
      // Draw the major grid lines
for (int i = 0; i <=10; i++) 
{
	if (i % 3 != 0)
		continue;
	
		      canvas.drawLine(0, i * height, getHeight(), i * height,
		               dark);
		      canvas.drawLine(0, i * height + 1,getHeight(), i * height
		               + 1, hilite);
		      canvas.drawLine(i * width, 0, i * width, getWidth(), dark);
		      canvas.drawLine(i * width + 1, 0, i * width + 1,
		    		  getWidth(), hilite);
		}
      
      
     
      Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
      foreground.setColor(getResources().getColor(
            R.color.puzzle_foreground));
      foreground.setStyle(Style.FILL);
      foreground.setTextSize(height * 0.5f);
      foreground.setTextScaleX(width / height);
      foreground.setTextAlign(Paint.Align.CENTER);

      // Draw the number in the center of the tile
      FontMetrics fm = foreground.getFontMetrics();
      // Centering in X: use alignment (and X at midpoint)
      float x = width / 2;
      // Centering in Y: measure ascent/descent first
      float y = height / 2 - (fm.ascent + fm.descent) / 2;
      for (int i = 0; i < 9; i++) {
         for (int j = 0; j < 9; j++) {
        	//Log.e("tile i j value",i+" " + j + " " + this.game.getTileString(i, j));
            canvas.drawText(this.game.getTileString(i, j), i
                  * width + x, j * height + y, foreground);
         }
      }
      

      
      
      // Draw the hints...
      
      // Pick a hint color based on #moves left
      Paint hint = new Paint();
      int c[] = { getResources().getColor(R.color.puzzle_hint_0),
            getResources().getColor(R.color.puzzle_hint_1),
            getResources().getColor(R.color.puzzle_hint_2), };
      Rect r = new Rect();
      for (int i = 0; i < 9; i++) {
         for (int j = 0; j < 9; j++) {
            int movesleft = 9 - game.getUsedTiles(i, j).length;
            getRect(i, j, r);
            hint.setColor(Color.YELLOW);
            if(game.getTile(i, j) == justSelectedValue && justSelectedValue != 0)
            	canvas.drawRect(r, hint);
            	canvas.drawText(this.game.getTileString(i, j), i
                    * width + x, j * height + y, foreground);
            if (movesleft < c.length) {
               
               //hint.setColor(c[movesleft]);
               //canvas.drawRect(r, hint);
            }
         }
      }
      

      
      // Draw the selection...
      
     // Log.e(TAG, "selRect=" + selRect);
      Paint selected = new Paint();
      selected.setColor(getResources().getColor(
            R.color.puzzle_selected));
      canvas.drawRect(selRect, selected);
      
      //Invalidate();
   }

   
@Override
   
public boolean onTouchEvent(MotionEvent event) {
      if (event.getAction() != MotionEvent.ACTION_DOWN)
         return super.onTouchEvent(event);

      select((int) (event.getX() / width),
            (int) (event.getY() / height));
      game.showKeypadOrError(selX, selY,this);
      Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
      return true;
   }
   
   
   
@Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      Log.e(TAG, "onKeyDown: keycode=" + keyCode + ", event="
            + event);

      switch (keyCode) {
      case KeyEvent.KEYCODE_DPAD_UP:
         select(selX, selY - 1);
         break;
      case KeyEvent.KEYCODE_DPAD_DOWN:
         select(selX, selY + 1);
         break;
      case KeyEvent.KEYCODE_DPAD_LEFT:
         select(selX - 1, selY);
         break;
      case KeyEvent.KEYCODE_DPAD_RIGHT:
         select(selX + 1, selY);
         break;
      
      
      case KeyEvent.KEYCODE_0:
      case KeyEvent.KEYCODE_SPACE: setSelectedTile(0); break;
      case KeyEvent.KEYCODE_1:     setSelectedTile(1); break;
      case KeyEvent.KEYCODE_2:     setSelectedTile(2); break;
      case KeyEvent.KEYCODE_3:     setSelectedTile(3); break;
      case KeyEvent.KEYCODE_4:     setSelectedTile(4); break;
      case KeyEvent.KEYCODE_5:     setSelectedTile(5); break;
      case KeyEvent.KEYCODE_6:     setSelectedTile(6); break;
      case KeyEvent.KEYCODE_7:     setSelectedTile(7); break;
      case KeyEvent.KEYCODE_8:     setSelectedTile(8); break;
      case KeyEvent.KEYCODE_9:     setSelectedTile(9); break;
      case KeyEvent.KEYCODE_ENTER:
      case KeyEvent.KEYCODE_DPAD_CENTER:
    	  Log.e("entering the check",selX + "");

         game.showKeypadOrError(selX, selY,this);
         break;
         
         
      default:
         return super.onKeyDown(keyCode, event);
      }
      return true;
   }
   

   
   public void setSelectedTile(int tile) {

      if (game.setTileIfValid(selX, selY, tile)) {
    	  justSelectedValue = tile;
         invalidate();// may change hints
         
      } else {
         
         // Number is not valid for this tile
         Log.e(TAG, "setSelectedTile: invalid: " + tile);
         
         startAnimation(AnimationUtils.loadAnimation(game,
               R.anim.shake));
         
         
      }
   }
   // jangkoo
   public void update(Game game){
	   
	   this.invalidate();
   }
   

   
   private void select(int x, int y) {
      //invalidate(selRect);
      selX = Math.min(Math.max(x, 0), 8);
      selY = Math.min(Math.max(y, 0), 8);
      getRect(selX, selY, selRect);
      invalidate(selRect);
   }
   

   
   private void getRect(int x, int y, Rect rect) {
      rect.set((int) (x * width), (int) (y * height), (int) (x
            * width + width), (int) (y * height + height));
   }
   
   public void showToast(String t){
	   
        Toast toast = Toast.makeText(game,
                t, Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER, 0, 0);
          toast.show();
   }
}
