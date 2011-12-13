package de.htwg.mocomp.lotteryapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.htwg.mocomp.lotteryapp.database.LotteryAppDatabaseAdapter;
import de.htwg.mocomp.lotteryapp.database.LotteryTicket;

public class ShowTicketActivity extends Activity{

	private LotteryTicket ticket;
	private Intent intent;
	private static final int RESULT_EDIT_TICKET = 0;
	private LotteryAppDatabaseAdapter dbAdapter;
	private TextView number1;
	private TextView number2;
	private TextView number3;
	private TextView number4;
	private TextView number5;
	private TextView number6;
	
	private RelativeLayout background1;
	private RelativeLayout background2;
	private RelativeLayout background3;
	private RelativeLayout background4;
	private RelativeLayout background5;
	private RelativeLayout background6;
	
	private Cursor c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showticket);
		ticket = (LotteryTicket) getIntent().getSerializableExtra("POJO");
		
		number1 = (TextView)findViewById(R.id.shownumber1);
        number2 = (TextView)findViewById(R.id.shownumber2);
        number3 = (TextView)findViewById(R.id.shownumber3);
        number4 = (TextView)findViewById(R.id.shownumber4);
        number5 = (TextView)findViewById(R.id.shownumber5);
        number6 = (TextView)findViewById(R.id.shownumber6);
        TextView creationDate = (TextView)findViewById(R.id.showCreationDate);
        TextView uuid = (TextView)findViewById(R.id.showUUID);
        
        background1 = (RelativeLayout) findViewById(R.id.relative1);
        background2 = (RelativeLayout) findViewById(R.id.relative2);
        background3 = (RelativeLayout) findViewById(R.id.relative3);
        background4 = (RelativeLayout) findViewById(R.id.relative4);
        background5 = (RelativeLayout) findViewById(R.id.relative5);
        background6 = (RelativeLayout) findViewById(R.id.relative6);
        
		if(ticket != null){
			dbAdapter = new LotteryAppDatabaseAdapter(this);
			dbAdapter.open();
			c = dbAdapter.getWinningTicket();
			
			
			
			number1.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(0))));
			number2.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(1))));
			number3.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(2))));
			number4.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(3))));
			number5.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(4))));
			number6.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(5))));
			
			
			
			
			uuid.setText(ticket.getUuid().toString());
			String currentDateTimeString = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ticket.getTicketCreationDate());
	        creationDate.setText(currentDateTimeString);
		}
		
	}

	private int validateTicket(List<Integer> winningNumbers) {
		int winningNumbersAmount = 0;
		if(winningNumbers.contains(ticket.getLottaryNumbers().get(0))){
			background1.setBackgroundColor(Color.GREEN);
			number1.setTextColor(Color.GREEN);
			winningNumbersAmount++;
		}
		if(winningNumbers.contains(ticket.getLottaryNumbers().get(1))){
			background2.setBackgroundColor(Color.GREEN);
			number2.setTextColor(Color.GREEN);
			winningNumbersAmount++;
		}
		if(winningNumbers.contains(ticket.getLottaryNumbers().get(2))){
			background3.setBackgroundColor(Color.GREEN);
			number3.setTextColor(Color.GREEN);
			winningNumbersAmount++;
		}
		if(winningNumbers.contains(ticket.getLottaryNumbers().get(3))){
			background4.setBackgroundColor(Color.GREEN);
			number4.setTextColor(Color.GREEN);
			winningNumbersAmount++;
		}
		if(winningNumbers.contains(ticket.getLottaryNumbers().get(4))){
			background5.setBackgroundColor(Color.GREEN);
			number5.setTextColor(Color.GREEN);
			winningNumbersAmount++;
		}
		if(winningNumbers.contains(ticket.getLottaryNumbers().get(5))){
			background6.setBackgroundColor(Color.GREEN);
			number6.setTextColor(Color.GREEN);
			winningNumbersAmount++;
		}
		return winningNumbersAmount;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,1,0, R.string.edit); 
		menu.add(0,2,0, R.string.delete);
		menu.add(0,3,0, R.string.validateTicket);
		
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
			case 1:
				intent = new Intent(this, CreateTicketActivity.class);
				intent.putExtra("POJO", ticket);
				startActivityForResult(intent, RESULT_EDIT_TICKET);
				break;
			case 2:
				intent = new Intent();
				ticket.setDelete(true);
				intent.putExtra("POJO", ticket);				
				if (getParent() == null) {
				    setResult(Activity.RESULT_OK, intent);
				} else {
				    getParent().setResult(Activity.RESULT_OK, intent);
				}
				finish();
				break;
			case 3:
				List<Integer> winningNumbers = new ArrayList<Integer>();
				for (int i = 1; i < 7; i++) {
					winningNumbers.add(c.getInt(i));
				}
				int winningAmount = validateTicket(winningNumbers);
				if(winningAmount > 0) {
					Toast.makeText(this, "CONGRATS " +winningAmount + "TODO", Toast.LENGTH_SHORT).show();
				}
				break;
		}
		 
		return true;
	}
	
	

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data);
	  switch(requestCode) { 
	    case (RESULT_EDIT_TICKET) :{ 
	      if (resultCode == Activity.RESULT_OK) { 
			ticket = (LotteryTicket) data.getSerializableExtra("POJO");
			intent = new Intent();
			intent.putExtra("POJO", ticket);				
			if (getParent() == null) {
			    setResult(Activity.RESULT_OK, intent);
			} else {
			    getParent().setResult(Activity.RESULT_OK, intent);
			}
			finish();
	      } 
	      break; 
	    } 
	  } 
	}
}
