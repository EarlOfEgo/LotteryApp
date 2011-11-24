package de.htwg.mocomp.lotteryapp;

import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import de.htwg.mocomp.lotteryapp.database.LotteryAppDatabaseAdapter;
import de.htwg.mocomp.lotteryapp.database.LotteryTicket;

public class LotteryAppActivity extends Activity {
	private LotteryAppDatabaseAdapter dbAdapter;
	private ListView listOfTickets;
	private Cursor cursor;
	private CursorAdapter cursorAdapter;
	private static final int RESULT_CREATE_TICKET = 0;
	private LotteryTicket ticket;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listoftickets);
		
		dbAdapter = new LotteryAppDatabaseAdapter(this);
		dbAdapter.open();
		
		listOfTickets = (ListView) findViewById(R.id.listViewTickets);
		listOfTickets.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		            int position, long id) {
		    	System.out.println("TRULALALA");
		          // When clicked, show a toast with the TextView text
//		          Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
//		              Toast.LENGTH_SHORT).show();
		        }
		    });

		populateDate();
		
	}
	
	
	private void populateDate(){
		cursor = dbAdapter.getAllTickets();
		
		String[] from = new String[] {dbAdapter.TICKET_ID, dbAdapter.TICKET_UUID};
		int[] to = new int[]{R.id.ticketNumberID, R.id.ticketUUID};
		
		cursorAdapter = new SimpleCursorAdapter(this, R.layout.listitem, cursor, from, to);
		listOfTickets.setAdapter(cursorAdapter);
		
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(dbAdapter != null)
			dbAdapter.close();
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,1,0, "R.string.createNewTicket"); 
		menu.add(0,2,0, R.string.quit); 
		
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch (item.getItemId()){
			case 1:
				Intent intent = new Intent(this, CreateTicketActivity.class);
				startActivityForResult(intent, RESULT_CREATE_TICKET);
				break;
			case 2:
				this.finish();
				break;
		}
		 
		return true;
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data);
	  switch(requestCode) { 
	    case (RESULT_CREATE_TICKET) :{ 
	      if (resultCode == Activity.RESULT_OK) {
	    	  ticket = new LotteryTicket();
	    	  ticket.setUuid(UUID.fromString(data.getStringExtra("UUID")));
	    	  for (int i = 1; i <= 6; i++) {
	    		  ticket.addLotteryNumber(data.getIntExtra("" + i,0));
	    	  }
	    	  
	    	  dbAdapter.insertNewTicket(ticket);
	    	  populateDate();
	      } 
	      break; 
	    } 
	  } 
	}

}
