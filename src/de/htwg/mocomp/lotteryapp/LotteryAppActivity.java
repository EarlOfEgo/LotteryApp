package de.htwg.mocomp.lotteryapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import de.htwg.mocomp.lotteryapp.database.LotteryAppDatabaseAdapter;
import de.htwg.mocomp.lotteryapp.networking.CheckTicketService;
import de.htwg.mocomp.lotteryapp.networking.GetWinningTicket;

public class LotteryAppActivity extends Activity {
	private static final Fragment CONTENT_VIEW_ID = null;
	private LotteryAppDatabaseAdapter dbAdapter;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		GetWinningTicket lt = new GetWinningTicket();
		//lt.setDbAdapter(dbAdapter);
		lt.execute("http://85.214.74.39/api/json/ticket");
		
		
		Intent intent = new Intent(this, ShowLastDrawFragment.class);
		intent.putExtra("POJO", lt.getWinningTicket());

		// setup action bar for tabs
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);
	    Tab tab = actionBar.newTab()
	            .setText("MY TICKETS")//R.string.myTickets)
	            .setTabListener(new TabListener<ShowListFragment>(
	                    this, "LISTVIEW", ShowListFragment.class));
	   
	    actionBar.addTab(tab);


	    tab = actionBar.newTab()
	        .setText("LAST DRAW")////R.string.showLastDraw)
	        .setTabListener(new TabListener<ShowLastDrawFragment>(
	                this, "SHOWVIEW", ShowLastDrawFragment.class));
	    actionBar.addTab(tab);
	    
	    
	    System.out.println("START WATCHER");
	    startService(new Intent(this, CheckTicketService.class));

		
	}

	

}
