package de.htwg.mocomp.lotteryapp;

import java.text.SimpleDateFormat;

import de.htwg.mocomp.lotteryapp.database.LotteryTicket;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowLastDraw extends Activity{
	
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
        
        TextView fetchDate = (TextView) findViewById(R.id.textView1);
        fetchDate.setText(R.string.fetchedDate);
        
        
		
		if(ticket != null){
			number1.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(0))));
			number2.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(1))));
			number3.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(2))));
			number4.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(3))));
			number5.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(4))));
			number6.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(5))));
			
			String currentDateTimeString = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ticket.getTicketCreationDate());
	        creationDate.setText(currentDateTimeString);
	        String fetchTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ticket.getTicketFetchedTime());
	        uuid.setText(fetchTime);
		}
		
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
