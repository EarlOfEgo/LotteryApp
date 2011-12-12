package de.htwg.mocomp.lotteryapp.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import de.htwg.mocomp.lotteryapp.database.LotteryTicket;

public class GetWinningTicket extends AsyncTask<String, Void, LotteryTicket> {

//	private LotteryAppDatabaseAdapter dbAdapter;
	private LotteryTicket winningTicket = null;
	private String newestTicketDate;
	
	@Override
	protected LotteryTicket doInBackground(String... params) {
		
		LotteryTicket ticket = new LotteryTicket();
		String jsonResponse = "";
		for (String url : params) {
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
				
				//System.out.println(jsonResponse);
				
				JSONObject myjson = new JSONObject(jsonResponse);
				
				JSONObject numbers = myjson.getJSONObject("Ticket");
			
				
				List<Integer> rightNumbers = new ArrayList<Integer>();
				for (int i = 1; i < 7; i++) {
					rightNumbers.add(numbers.getInt("no" + i));
				}
				//ticket.setTicketCreationDate(new Date(numbers.getString("created")));
				newestTicketDate = numbers.getString("created");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setWinningTicket(ticket);
		return ticket;
	}
	
	
	@Override
	protected void onPostExecute(LotteryTicket lastDraw) {
//		System.out.println(newestTicketDate);
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


	public LotteryTicket getWinningTicket() {
		return winningTicket;
	}


	public void setWinningTicket(LotteryTicket winningTicket) {
		this.winningTicket = winningTicket;
	}


	public String getNewestTicketDate() {
		return newestTicketDate;
	}



}