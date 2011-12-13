package de.htwg.mocomp.lotteryapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import de.htwg.mocomp.lotteryapp.database.LotteryAppDatabaseAdapter;
import de.htwg.mocomp.lotteryapp.database.LotteryTicket;
import de.htwg.mocomp.lotteryapp.networking.CheckTicketService;

public class LotteryAppActivity extends Activity {
	private static final Fragment CONTENT_VIEW_ID = null;
	private LotteryAppDatabaseAdapter dbAdapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		dbAdapter = new LotteryAppDatabaseAdapter(this);
		
		
		ActionBar bar = getActionBar();
	    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    ActionBar.Tab tabA = bar.newTab().setText(R.string.myTickets);
	    ActionBar.Tab tabB = bar.newTab().setText(R.string.showLastDraw);

	    Fragment fragmentA = new ShowListFragment();
	    Fragment fragmentB = new ShowLastDrawFragment();
	    Bundle mBundle = new Bundle();

		fragmentB.setArguments(mBundle);

	    tabA.setTabListener(new MyTabsListener(fragmentA));
	    tabB.setTabListener(new MyTabsListener(fragmentB));

	    bar.addTab(tabA);
	    bar.addTab(tabB);

//	    new DoCheck().execute("http://85.214.74.39/api/json/ticket");
	    
	    
	    Intent a = new Intent(this, CheckTicketService.class);
	    startService(a);

		
	}

	protected class MyTabsListener implements ActionBar.TabListener
	 {

		private Fragment fragment;
		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			System.out.println("RESELECT");

		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.add(R.id.fragment_container, fragment, null);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			ft.remove(fragment);
		}

	}
	
	private class DoCheck extends AsyncTask<String, Void, LotteryTicket> {
		
		@Override
		protected LotteryTicket doInBackground(String... params) {
			
			LotteryTicket ticket = new LotteryTicket();
			String jsonResponse = "";
			for (String url : params) {
				System.out.println(url);
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(request);
					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					String line = "";
					while((line = rd.readLine()) != null) {
						jsonResponse  += line;
					}
					
					JSONObject myjson = new JSONObject(jsonResponse);
					
					JSONObject numbers = myjson.getJSONObject("Ticket");
					
					List<Integer> rightNumbers = new ArrayList<Integer>();
					for (int i = 1; i < 7; i++) {
						rightNumbers.add(numbers.getInt("no" + i));
					}
					ticket.setLottaryNumbers(rightNumbers);
					
					SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		            Date date1 = sdfToDate.parse(numbers.getString("created"));
		            ticket.setTicketCreationDate(date1);
		            
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return ticket;
		}
		
		
		@Override
		protected void onPostExecute(LotteryTicket ticket) {
			dbAdapter.open();
			ticket.setTicketFetchedTime(new Date(System.currentTimeMillis()));
			Cursor c = dbAdapter.getWinningTicket();
			if(c == null){
				dbAdapter.insertNewWinningTicket(ticket);
			} else {
				System.out.println(c.getLong(8) + "<->" + ticket.getTicketCreationDate().getTime());
				if(c.getLong(8) != ticket.getTicketCreationDate().getTime()) {
					dbAdapter.updateWinningTicket(ticket);
					System.out.println("UPDATE");
					
				}
			}
			
		}

	
//	private Map<Integer, LotteryTicket> getWinningTickets(List<Integer> rightNumbers) {
//		//TODO JSON ITERATE
//		
//		Map<Integer, LotteryTicket> tickets = new HashMap<Integer, LotteryTicket>();
//		for (Integer integer : rightNumbers) {
//			Cursor cursor = dbAdapter.getWinningTickets(integer);
//			for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {
//			    if(hasItem) {
//			    	LotteryTicket ticket = new LotteryTicket();
//			    	ticket.setId(cursor.getInt(0));
//			    	ticket.setUuid(UUID.fromString(cursor.getString(1)));
//			    	ticket.setTicketCreationDate(new Date(cursor.getLong(8)));
//			    	List<Integer> lottaryNumbers = new ArrayList<Integer>();
//			    	for (int i = 1; i < 7; i++) {
//						lottaryNumbers.add(cursor.getInt(i));
//					}
//			    	ticket.setLottaryNumbers(lottaryNumbers);
//			    	if(!tickets.containsKey(ticket.getId())){
//			    		tickets.put(ticket.getId(), ticket);
//			    	}
//			    }
//			}
//		}
//		return tickets;
//	}
//
//	public LotteryAppDatabaseAdapter getDbAdapter() {
//		return dbAdapter;
//	}
//
//	public void setDbAdapter(LotteryAppDatabaseAdapter dbAdapter) {
//		this.dbAdapter = dbAdapter;
//	}

	}

}
