package de.htwg.mocomp.lotteryapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import de.htwg.mocomp.lotteryapp.database.LotteryAppDatabaseAdapter;
import de.htwg.mocomp.lotteryapp.database.LotteryTicket;
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
//	    ActionBar actionBar = getActionBar();
//	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//	    actionBar.setDisplayShowTitleEnabled(false);
//	    Tab tab = actionBar.newTab()
//	            .setText(R.string.myTickets)
//	            .setTabListener(new TabListener<ShowListFragment>(
//	                    this, "LISTVIEW", ShowListFragment.class));
//	   
//	    actionBar.addTab(tab);
//
//
//	    tab = actionBar.newTab()
//	        .setText(R.string.showLastDraw)
//	        .setTabListener(new TabListener<ShowLastDrawFragment>(
//	                this, "SHOWVIEW", ShowLastDrawFragment.class));
//	    actionBar.addTab(tab);
		
		
		ActionBar bar = getActionBar();
	    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    ActionBar.Tab tabA = bar.newTab().setText(R.string.myTickets);
	    ActionBar.Tab tabB = bar.newTab().setText(R.string.showLastDraw);

	    Fragment fragmentA = new ShowListFragment();
	    Fragment fragmentB = new ShowLastDrawFragment();
	    Bundle mBundle = new Bundle();
	    while(lt.getWinningTicket() == null);
	    mBundle.putSerializable("WINNINGTICKET", lt.getWinningTicket());

		fragmentB.setArguments(mBundle);

	    tabA.setTabListener(new MyTabsListener(fragmentA));
	    tabB.setTabListener(new MyTabsListener(fragmentB));

	    bar.addTab(tabA);
	    bar.addTab(tabB);

	    
	    
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

}
