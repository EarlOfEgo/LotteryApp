package de.htwg.mocomp.lotteryapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
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
import de.htwg.mocomp.lotteryapp.networking.GetWinningTicket;

public class LotteryAppActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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

		
	}

	

}
