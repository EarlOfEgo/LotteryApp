package de.htwg.mocomp.lotteryapp;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import de.htwg.mocomp.lotteryapp.database.LotteryTicket;

public class ShowTicketActivity extends Activity{

	private LotteryTicket ticket;
	private Intent intent;
	private static final int RESULT_EDIT_TICKET = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showticket);
		ticket = (LotteryTicket) getIntent().getSerializableExtra("POJO");
		
		TextView number1 = (TextView)findViewById(R.id.shownumber1);
        TextView number2 = (TextView)findViewById(R.id.shownumber2);
        TextView number3 = (TextView)findViewById(R.id.shownumber3);
        TextView number4 = (TextView)findViewById(R.id.shownumber4);
        TextView number5 = (TextView)findViewById(R.id.shownumber5);
        TextView number6 = (TextView)findViewById(R.id.shownumber6);
        TextView creationDate = (TextView)findViewById(R.id.showCreationDate);
        TextView uuid = (TextView)findViewById(R.id.showUUID);
        
        
		
		if(ticket != null){
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,1,0, R.string.edit); 
		menu.add(0,2,0, R.string.delete);
		menu.add(0,3,0, R.string.back); 
		
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
				finish();
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
