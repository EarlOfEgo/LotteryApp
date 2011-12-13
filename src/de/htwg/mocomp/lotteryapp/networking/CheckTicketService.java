package de.htwg.mocomp.lotteryapp.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import de.htwg.mocomp.lotteryapp.LotteryAppActivity;
import de.htwg.mocomp.lotteryapp.R;
import de.htwg.mocomp.lotteryapp.database.LotteryAppDatabaseAdapter;
import de.htwg.mocomp.lotteryapp.database.LotteryTicket;



public class CheckTicketService extends Service {
	private Timer timer = new Timer();
	private static final long INTERVAL = 20000;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		startWatcher();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer != null) {
			timer.cancel();
		}
	}


	private void startWatcher() {

		timer.scheduleAtFixedRate(
				new TimerTask() {
//					DoCheck dodo = new DoCheck();
					@Override
					public void run() {
//						SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//					    String ticketDate = settings.getString("oldtime", winningTicket.getNewestTicketDate());
//
//						System.out.println("CHECK CHECK");
//						winningTicket.execute("http://85.214.74.39/api/json/ticket");
//						while(winningTicket.getNewestTicketDate() == null);
//						System.out.println(winningTicket.getNewestTicketDate() + "<->" + ticketDate);
//						LotteryAppDatabaseAdapter dbAdapter = new LotteryAppDatabaseAdapter(getApplicationContext());
//						dbAdapter.open();
//						Cursor c = dbAdapter.getWinningTicket();
//						format(c.getLong(8));
						
						
						String jsonResponse = "";
						HttpClient client = new DefaultHttpClient();
						String url = "http://85.214.74.39/api/json/ticket";
						HttpGet request = new HttpGet(url );
						LotteryTicket ticket = new LotteryTicket();
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
						
						
						LotteryAppDatabaseAdapter dbAdapter = new LotteryAppDatabaseAdapter(getApplicationContext());
						dbAdapter.open();
						Cursor c = dbAdapter.getWinningTicket();
						System.out.println("CHECK CHECK");
						System.out.println(c.getLong(8) +"<->"+ ticket.getTicketCreationDate().getTime());
						
						if(c.getLong(8) != ticket.getTicketCreationDate().getTime()){
							ticket.setTicketFetchedTime(new Date(System.currentTimeMillis()));
							dbAdapter.updateWinningTicket(ticket);
							
							
							Context context = getApplicationContext();
							NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
							Notification newTicket = new Notification();
							newTicket.icon = R.drawable.ticket;
							newTicket.tickerText = "WINNER WINNER CHICKEN DINNER?!";
							newTicket.when = System.currentTimeMillis();
							newTicket.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
							
							
							Intent notificationintent = new Intent(context, LotteryAppActivity.class);
							PendingIntent intent = PendingIntent.getActivity(context, 0, notificationintent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
							newTicket.setLatestEventInfo(context, "TRULALA", "WINNER WINNER CHICKEN DINNER?!", intent);
							manager.notify(0, newTicket);
							
						}
						
						
						dbAdapter.close();
					}
				}
				, 0, INTERVAL);
		
	}
	
	
//	private class DoCheck extends AsyncTask<String, Void, LotteryTicket> {
//		
//		@Override
//		protected LotteryTicket doInBackground(String... params) {
//			
//			LotteryTicket ticket = new LotteryTicket();
//			String jsonResponse = "";
//			for (String url : params) {
//				HttpClient client = new DefaultHttpClient();
//				HttpGet request = new HttpGet(url);
//				HttpResponse response;
//				try {
//					response = client.execute(request);
//					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//					String line = "";
//					while((line = rd.readLine()) != null) {
//						jsonResponse  += line;
//					}
//					
//					JSONObject myjson = new JSONObject(jsonResponse);
//					
//					JSONObject numbers = myjson.getJSONObject("Ticket");
//					
//					List<Integer> rightNumbers = new ArrayList<Integer>();
//					for (int i = 1; i < 7; i++) {
//						rightNumbers.add(numbers.getInt("no" + i));
//					}
//					ticket.setLottaryNumbers(rightNumbers);
//					
//					SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		            Date date1 = sdfToDate.parse(numbers.getString("created"));
//		            ticket.setTicketCreationDate(date1);
//		            
//		            
//		            
//		            
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			return ticket;
//		}
//		
//		
//		@Override
//		protected void onPostExecute(LotteryTicket ticket) {
//			LotteryAppDatabaseAdapter dbAdapter = new LotteryAppDatabaseAdapter(getApplicationContext());
//			dbAdapter.open();
//			Cursor c = dbAdapter.getWinningTicket();
//			
//			
//			if(c.getLong(8) != ticket.getTicketCreationDate().getTime()){
//				System.out.println("NEW");
//				
//				
//				Context context = getApplicationContext();
//				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//				Notification newTicket = new Notification();
//				newTicket.icon = R.drawable.ticket;
//				newTicket.tickerText = "WINNER WINNER CHICKEN DINNER?!";
//				newTicket.when = System.currentTimeMillis();
//				newTicket.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
//				
//				
//				Intent notificationintent = new Intent(context, LotteryAppActivity.class);
//				PendingIntent intent = PendingIntent.getActivity(context, 0, notificationintent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
//				newTicket.setLatestEventInfo(context, "TRULALA", "WINNER WINNER CHICKEN DINNER?!", intent);
//				manager.notify(0, newTicket);
//				
//			}
////			dbAdapter.open();
////			ticket.setTicketFetchedTime(new Date(System.currentTimeMillis()));
////			Cursor c = dbAdapter.getWinningTicket();
////			if(c == null){
////				dbAdapter.insertNewWinningTicket(ticket);
////			} else {
////				System.out.println(c.getLong(8) + "<->" + ticket.getTicketCreationDate().getTime());
////				if(c.getLong(8) != ticket.getTicketCreationDate().getTime()) {
////					dbAdapter.updateWinningTicket(ticket);
////					System.out.println("UPDATE");
////					
////				}
////			}
//			
//			
//		}
//			
//			
//		}

	
}
