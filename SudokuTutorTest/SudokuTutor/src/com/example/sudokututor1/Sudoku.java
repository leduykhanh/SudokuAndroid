package com.example.sudokututor1;

import com.example.sudokututor.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Sudoku extends Activity implements OnClickListener {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_sudoku);

      // Set up click listeners for all the buttons
      View continueButton = findViewById(R.id.continue_button);
      continueButton.setOnClickListener(this);
      View newBlankButton = findViewById(R.id.new_button);
      newBlankButton.setOnClickListener(this);
      View aboutButton = findViewById(R.id.about_button);
      aboutButton.setOnClickListener(this);
      View exitButton = findViewById(R.id.exit_button);
      exitButton.setOnClickListener(this);
   }

   @Override
public void onClick(View v) {
      switch (v.getId()) {
      case R.id.about_button:
         Intent i = new Intent(this, about_app.class);
         startActivity(i);
         break;
      // More buttons go here (if any) ...
      case R.id.new_button:
    	  Intent newgame = new Intent(this, new_game.class);
          startActivity(newgame);
          break;

   
      case R.id.exit_button:
         finish();
         break;
      }
   }
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.settings:
         startActivity(new Intent(this, prefs.class));
         return true;
      // More items go here (if any) ...
      }
      return false;
   }

}