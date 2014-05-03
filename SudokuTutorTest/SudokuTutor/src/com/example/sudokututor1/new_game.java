package com.example.sudokututor1;

import com.example.sudokututor.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class new_game extends Activity implements OnClickListener {
	private static final String TAG = "Sudoku";
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game);

	      // Set up click listeners for all the buttons
	      View newBlankButton = findViewById(R.id.new_blank_button);
	      newBlankButton.setOnClickListener(this);
	      View newDiffButton = findViewById(R.id.new_diff_button);
	      newDiffButton.setOnClickListener(this);
	      View newBackButton = findViewById(R.id.new_back_button);
	      newBackButton.setOnClickListener(this);
	   }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.new_blank_button:
				Intent game = new Intent(this, Game.class);
		          startActivity(game);
		          break;
			case R.id.new_diff_button:
				openNewGameDialog();
	        break;
			case R.id.new_back_button:
				finish();
	        break;
			}
		}
		
  private void openNewGameDialog() {
      new AlertDialog.Builder(this)
           .setTitle(R.string.new_game_title)
           .setItems(R.array.difficulty,
            new DialogInterface.OnClickListener() {
               @Override
			public void onClick(DialogInterface dialoginterface,
                     int i) {
                  startGame(i);
               }
            })
           .show();
   }
  private void startGame(int i) {
      Log.d(TAG, "clicked on " + i);
      Intent game = new Intent(this, Game.class);
      game.putExtra(Game.KEY_DIFFICULTY, i);
      startActivity(game);
     
   }


}

