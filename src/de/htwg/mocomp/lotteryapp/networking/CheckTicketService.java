package de.htwg.mocomp.lotteryapp.networking;

import java.util.Timer;
import java.util.TimerTask;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import de.htwg.mocomp.lotteryapp.LotteryAppActivity;



public class CheckTicketService extends Service{
	private Timer timer = new Timer();
	private static final long INTERVAL = 100000;
	private GetWinningTicket winningTicket;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		winningTicket = new GetWinningTicket();
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
					
					@Override
					public void run() {
						if(winningTicket.newTicket() == true){
							
						
							Context context = getApplicationContext();
							NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
							Notification newTicket = new Notification();
							newTicket.icon = R.drawable.btn_plus;
							newTicket.tickerText = "WINNER WINNER CHICKEN DINNER?!";
							newTicket.when = System.currentTimeMillis();
							
							
							Intent notificationintent = new Intent(context, LotteryAppActivity.class);
							PendingIntent intent = PendingIntent.getActivity(context, 0, notificationintent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
							newTicket.setLatestEventInfo(context, "TRULALA", "WINNER WINNER CHICKEN DINNER?!", intent);
							manager.notify(0, newTicket);
							
						}
					}
				}
				, 0, INTERVAL);
		
	}

	
}
