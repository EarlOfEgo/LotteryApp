package de.htwg.mocomp.lotteryapp;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.htwg.mocomp.lotteryapp.database.LotteryAppDatabaseAdapter;
import de.htwg.mocomp.lotteryapp.database.LotteryTicket;

public class ShowLastDrawFragment extends Fragment{

	private LotteryTicket ticket;
	private Intent intent;
	private static final int RESULT_EDIT_TICKET = 0;
	private LotteryAppDatabaseAdapter dbAdapter;
	private RelativeLayout rl;
	private Activity activity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		activity = super.getActivity();
		rl = (RelativeLayout) inflater.inflate(R.layout.showticket, container, false);
		dbAdapter = new LotteryAppDatabaseAdapter(activity);
		dbAdapter.open();
		Cursor c = dbAdapter.getWinningTicket();
		
		View view = inflater.inflate(R.layout.showticket, container, false);
		TextView number1 = (TextView) view.findViewById(R.id.shownumber1);
        TextView number2 = (TextView) view.findViewById(R.id.shownumber2);
        TextView number3 = (TextView) view.findViewById(R.id.shownumber3);
        TextView number4 = (TextView) view.findViewById(R.id.shownumber4);
        TextView number5 = (TextView) view.findViewById(R.id.shownumber5);
        TextView number6 = (TextView) view.findViewById(R.id.shownumber6);
        TextView creationDate = (TextView) view.findViewById(R.id.showCreationDate);
        TextView uuid = (TextView) view.findViewById(R.id.showUUID);
        
        TextView fetchDate = (TextView)  view.findViewById(R.id.textView1);
        fetchDate.setText(R.string.fetchedDate);
        
        
//        System.out.println(c.getColumnCount() + "<->" + c.getCount() + "<->" + c.getColumnName(1));
		
		if(!c.isNull(2)){
			
			number1.setText(c.isNull(1) ? "" : ""+c.getInt(c.getColumnIndexOrThrow("number1")));
			number2.setText(c.isNull(2) ? "" : ""+c.getInt(c.getColumnIndexOrThrow("number2")));
			number3.setText(c.isNull(3) ? "" : ""+c.getInt(c.getColumnIndexOrThrow("number3")));
			number4.setText(c.isNull(4) ? "" : ""+c.getInt(c.getColumnIndexOrThrow("number4")));
			number5.setText(c.isNull(5) ? "" : ""+c.getInt(c.getColumnIndexOrThrow("number5")));
			number6.setText(c.isNull(6) ? "" : ""+c.getInt(c.getColumnIndexOrThrow("number6")));
			
			
			
			String currentDateTimeString = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(c.getLong(8));
	        creationDate.setText(currentDateTimeString);
	        String fetchTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(c.getLong(7));
	        uuid.setText(fetchTime);
		}
		dbAdapter.close();
		
		return view;//inflater.inflate(R.layout.showticket, container, false);
	}
	
	

}
