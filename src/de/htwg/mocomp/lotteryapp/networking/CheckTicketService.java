package de.htwg.mocomp.lotteryapp.networking;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import de.htwg.mocomp.lotteryapp.LotteryAppActivity;
import de.htwg.mocomp.lotteryapp.R;



public class CheckTicketService extends Service {
	private Timer timer = new Timer();
	private static final long INTERVAL = 10000;
	
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
		final String PREFS_NAME = "ticketDate";

		timer.scheduleAtFixedRate(
				new TimerTask() {
					
					@Override
					public void run() {
						GetWinningTicket winningTicket = new GetWinningTicket();
						SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					    String ticketDate = settings.getString("oldtime", winningTicket.getNewestTicketDate());

						System.out.println("CHECK CHECK");
						winningTicket.execute("http://85.214.74.39/api/json/ticket");
						while(winningTicket.getNewestTicketDate() == null);
//						System.out.println(winningTicket.getNewestTicketDate() + "<->" + ticketDate);
						if(!winningTicket.getNewestTicketDate().equals(ticketDate) ){
							System.out.println("NEW");
							ticketDate = winningTicket.getNewestTicketDate();
							
							
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
						
						
						SharedPreferences string = getSharedPreferences(PREFS_NAME, 0);
						SharedPreferences.Editor editor = string.edit();
						editor.putString("oldtime", ticketDate);

					     // Commit the edits!
					    editor.commit();

					}
				}
				, 0, INTERVAL);
		
	}

	
}
