package de.htwg.mocomp.lotteryapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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
		registerForContextMenu(listOfTickets);
		onClickOnItem();
		longClickOnItem();
		populateDate();
		
	}

	private void onClickOnItem() {
		listOfTickets.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
		    	updateActualTicket(position);
				Intent intent = new Intent(LotteryAppActivity.this, ShowTicketActivity.class);
				intent.putExtra("POJO", ticket);
				startActivityForResult(intent, RESULT_CREATE_TICKET);
		    	
		    }
		});
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
		menu.add(0,1,0, R.string.createNewTicket); 
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
	    	  ticket = (LotteryTicket) data.getSerializableExtra("POJO");
	    	  if(ticket.isDelete()){    		  
	    		  deleteTicket();
	    	  } else {
		    	  if(ticket.getId() == -1){
		    		  ticket.setTicketCreationDate(new Date(System.currentTimeMillis()));
		    		  int id = (int) dbAdapter.insertNewTicket(ticket);
		    		  Resources res = getResources();
		    		  String message = String.format(res.getString(R.string.successfullyCreated), id );
		    		  makeAToast(message); 
		    	  } else {
		    		  dbAdapter.updateTicket(ticket);
		    		  Resources res = getResources();
		    		  String message = String.format(res.getString(R.string.successfullyEdited), ticket.getId());
		    		  makeAToast(message); 
		    	  } 
		    	  populateDate();
	    	  }
	      } 
	      break; 
	    } 
	  } 
	}

	private void deleteTicket() {
		int id = ticket.getId();
		dbAdapter.removeTicket(id);
		populateDate();
		Resources res = getResources();
		String message = String.format(res.getString(R.string.successfullyDeleted), id );
		makeAToast(message);
		
	}

	private void makeAToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		Resources res = getResources();
		String ticketInfo = String.format(res.getString(R.string.chooseTicketToLoad));
		menu.setHeaderTitle(ticketInfo + ticket.getId());
		
		String editString = String.format(res.getString(R.string.edit));
		String deleteString = String.format(res.getString(R.string.delete));
//		String backString = String.format(res.getString(R.string.back));
		String[] menuItems = new String[] {editString, deleteString};//, backString};
		for (int i = 0; i<menuItems.length; i++) {
		  menu.add(Menu.NONE, i, i, menuItems[i]);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  switch (item.getItemId()) {
	  case 0:
		  Intent intent = new Intent(this, CreateTicketActivity.class);
		  intent.putExtra("POJO", ticket);
		  startActivityForResult(intent, RESULT_CREATE_TICKET);
	    return true;
	  case 1:
		  deleteTicket();
	    return true;
//	  case 2:
//		  return true;
	  default:
		  return super.onContextItemSelected(item);
	  }
	}
	
	private void longClickOnItem() {		
		listOfTickets.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id3) {	
				updateActualTicket(position);
				return false;
			}
		});
	}

	private void updateActualTicket(int position) {
		cursor = (Cursor) cursorAdapter.getItem(position);
		ticket = new LotteryTicket(); 
		cursor = dbAdapter.getTicket(cursor.getInt(cursor.getColumnIndex("_id")));
		ticket.setId(cursor.getInt(0));	 
		ticket.setUuid(UUID.fromString(cursor.getString(1)));
		List<Integer> a = new ArrayList<Integer>();
		for (int i = 2; i <= 7; i++) {
			a.add(cursor.getInt(i));
		}
		ticket.setLottaryNumbers(a);
		ticket.setTicketCreationDate(new Date(cursor.getLong(8)));
	}

}
