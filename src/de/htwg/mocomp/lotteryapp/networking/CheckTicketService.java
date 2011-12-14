package de.htwg.mocomp.lotteryapp.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import android.content.res.Resources;
import android.database.Cursor;
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
		super.onCreate();
		startWatcher();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timer != null) {
			timer.cancel();
		}
	}


	private void startWatcher() {

		timer.scheduleAtFixedRate(
				new TimerTask() {
					@Override
					public void run() {
						
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
							return;
//							e.printStackTrace();
						}
						
						
						LotteryAppDatabaseAdapter dbAdapter = new LotteryAppDatabaseAdapter(getApplicationContext());
						dbAdapter.open();
						Cursor c = dbAdapter.getWinningTicket();
						System.out.println("CHECK CHECK");
//						System.out.println(c.getLong(8) +"<->"+ ticket.getTicketCreationDate().getTime());
				
						if(c.getLong(8) != ticket.getTicketCreationDate().getTime()){
							ticket.setTicketFetchedTime(new Date(System.currentTimeMillis()));
							dbAdapter.updateWinningTicket(ticket);
							
							
							Resources res = getResources();
							String tickerText = String.format(res.getString(R.string.newTicketAvailable));
							String notificationText = String.format(res.getString(R.string.newTicketAvailableNotification));
							
							
							Context context = getApplicationContext();
							NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
							Notification newTicket = new Notification();
							newTicket.icon = R.drawable.ticket;
							newTicket.tickerText = notificationText;
							newTicket.when = System.currentTimeMillis();
							newTicket.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
							
							
							Intent notificationintent = new Intent(context, LotteryAppActivity.class);
							notificationintent.putExtra("SERVICE", true);
							PendingIntent intent = PendingIntent.getActivity(context, 0, notificationintent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
							newTicket.setLatestEventInfo(context, tickerText, notificationText, intent);
							manager.notify(0, newTicket);
							
						}
						
						
						dbAdapter.close();
					}
				}
				, 0, INTERVAL);
	}
}
