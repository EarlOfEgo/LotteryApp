package de.htwg.mocomp.lotteryapp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import de.htwg.mocomp.lotteryapp.database.LotteryTicket;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class CreateTicketActivity extends Activity implements SeekBar.OnSeekBarChangeListener{
    private static final String PREFS_NAME = "my_tickets";
	private TextView numbers;
    private TextView number1;
    private TextView number2;
    private TextView number3;
    private TextView number4;
    private TextView number5;
    private TextView number6;
    private boolean ticketGenerated;
    private Stack<Integer> previousNumbers;
    private List<Integer> lottaryNumbers;
	private int number; 
	private SeekBar seekBar;
	private Button chooseNumbers;
	private Button deleteNumbers;
	private Button generateTicket;
	private Button deleteLastNumber;
	private Button saveTicket;
	private Button loadTicket;
	private Set<String> ticketNames;
	private UUID uuid;
	private boolean activeTicket;
    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        
        
        
        number = 1;
        previousNumbers = new Stack<Integer>();
        lottaryNumbers = new ArrayList<Integer>();
        
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        ticketNames = settings.getStringSet(PREFS_NAME, new HashSet<String>());
        
        ticketGenerated = false;
        activeTicket = false;
        
        numbers = (TextView)findViewById(R.id.numberToChoose);
        numbers.setText(String.valueOf(new Integer(number)));
        
        number1 = (TextView)findViewById(R.id.number1);
        number2 = (TextView)findViewById(R.id.number2);
        number3 = (TextView)findViewById(R.id.number3);
        number4 = (TextView)findViewById(R.id.number4);
        number5 = (TextView)findViewById(R.id.number5);
        number6 = (TextView)findViewById(R.id.number6);
        
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);


        
       chooseNumberButton();
       deleteNumbersButton();
       deleteLastNumberButton();
       generateTicketButton();
       saveTicketButton();
       
       
       
       
    }

	private void saveTicketButton() {
		// TODO Auto-generated method stub
		saveTicket = (Button) findViewById(R.id.saveButton);
		saveTicket.setEnabled(ticketGenerated);
		saveTicket.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("UUID", uuid.toString());
				int i = 1;
				for(Integer a : lottaryNumbers)
					intent.putExtra(""+ i++, a);
				
				if (getParent() == null) {
				    setResult(Activity.RESULT_OK, intent);
				} else {
				    getParent().setResult(Activity.RESULT_OK, intent);
				}
				finish();
			}});
	}



	private void generateTicketButton() {
		generateTicket = (Button) findViewById(R.id.generateTicketButton);
		generateTicket.setEnabled(lottaryNumbers.size() >= 6 ? true : false);
		generateTicket.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new Dialog(CreateTicketActivity.this);

				dialog.setContentView(R.layout.ticketcreateddialog);
				dialog.setTitle(R.string.ticketCreated);

				TextView uuidText = (TextView) dialog.findViewById(R.id.uuid);
				if(!activeTicket)
					uuid = java.util.UUID.randomUUID();
				uuidText.setText(""+uuid);
				activeTicket = true;
				
				
				
				
				TextView numbersText = (TextView) dialog.findViewById(R.id.numbers);
				String numbers = "";
				for(Integer numb: lottaryNumbers ){
					numbers += numb + ", ";
				}
				
				numbersText.setText(numbers);
				ticketGenerated = true;
				updateAllButtons();
				ImageView image = (ImageView) dialog.findViewById(R.id.image);
				image.setImageResource(R.drawable.ticket);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}});
	}



	private void deleteLastNumberButton() {
		deleteLastNumber = (Button) findViewById(R.id.deleteLastButton);
		deleteLastNumber.setEnabled(previousNumbers.size() <= 0 ? false : true);
		deleteLastNumber.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lottaryNumbers.remove(previousNumbers.pop());

				ticketGenerated = false;
				activeTicket = false;
				updateLottaryField();
				updateAllButtons();
			}});
	}



	private void deleteNumbersButton() {
		deleteNumbers = (Button) findViewById(R.id.clearNumbersButton);
		deleteNumbers.setEnabled(false);
		deleteNumbers.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(CreateTicketActivity.this).setTitle(R.string.deleteNumbers).setNegativeButton(R.string.CANCELButton, null).setPositiveButton(R.string.OKButton, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   lottaryNumbers.clear();
			        	   previousNumbers.clear();
			        	   ticketGenerated = false;
			        	   activeTicket = false;
			        	   updateLottaryField();
			        	   updateAllButtons();
			           }
			       })
				   .show();
			}});
	}



	private void chooseNumberButton() {
		chooseNumbers = (Button) findViewById(R.id.chooseNumbers);
		chooseNumbers.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				lottaryNumbers.add(number);
				previousNumbers.push(number);
				Collections.sort(lottaryNumbers);
				updateLottaryField();
				updateAllButtons();
			}
	    	
	    });
	}

    

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		number = (progress/2)-1 <= 0 ? 1 : (progress/2)-1;
		updateAllButtons();
		
	}


	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		updateAllButtons();
		
	}


	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		updateAllButtons();
	}



	private void updateAllButtons() {
		numbers.setText(String.valueOf(new Integer(number)));
		chooseNumbers.setEnabled(lottaryNumbers.contains(number) ? false : true);
		deleteLastNumber.setEnabled(previousNumbers.size() <= 0 ? false : true);
		if(lottaryNumbers.size() >= 6) chooseNumbers.setEnabled(false);
		generateTicket.setEnabled(lottaryNumbers.size() >= 6 ? true : false);
		deleteNumbers.setEnabled(lottaryNumbers.size() > 0 ? true : false);
		saveTicket.setEnabled(ticketGenerated);
		generateTicket.setText(activeTicket ? R.string.showTicket : R.string.generateTicketButton);
	}



	private void updateLottaryField() {
		number1.setText(lottaryNumbers.size() > 0 ? String.valueOf(new Integer(lottaryNumbers.get(0))) : "");
		number2.setText(lottaryNumbers.size() > 1 ? String.valueOf(new Integer(lottaryNumbers.get(1))) : "");
		number3.setText(lottaryNumbers.size() > 2 ? String.valueOf(new Integer(lottaryNumbers.get(2))) : "");
		number4.setText(lottaryNumbers.size() > 3 ? String.valueOf(new Integer(lottaryNumbers.get(3))) : "");
		number5.setText(lottaryNumbers.size() > 4 ? String.valueOf(new Integer(lottaryNumbers.get(4))) : "");
		number6.setText(lottaryNumbers.size() > 5 ? String.valueOf(new Integer(lottaryNumbers.get(5))) : "");
	}
	
	
    protected void savePreferences(){

       SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
       SharedPreferences.Editor editor = settings.edit();
       editor.putStringSet(PREFS_NAME, ticketNames);
       editor.commit();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,1,0,R.string.quit); 
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		Intent intent = new Intent();
		intent.putExtra("UUID", uuid.toString());
		int i = 1;
		for(Integer a : lottaryNumbers)
			intent.putExtra(""+ i++, a);
		
		if (this.getParent() == null) {
		    setResult(Activity.RESULT_OK, intent);
		} else {
		    getParent().setResult(Activity.RESULT_OK, intent);
		}
		finish();


		return true;
	}


}
