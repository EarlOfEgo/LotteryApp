package de.htwg.mocomp.lotteryapp;

import java.text.SimpleDateFormat;

import de.htwg.mocomp.lotteryapp.database.LotteryTicket;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowLastDrawFragment extends Fragment{

	private LotteryTicket ticket;
	private Intent intent;
	private static final int RESULT_EDIT_TICKET = 0;
	private RelativeLayout rl;
	private Activity activity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		activity = super.getActivity();
		rl = (RelativeLayout) inflater.inflate(R.layout.showticket, container, false);
		
//		ticket = (LotteryTicket) activity.getIntent().getSerializableExtra("POJO");
		ticket = (LotteryTicket) getArguments().get("WINNINGTICKET");
		
		TextView number1 = (TextView) rl.findViewById(R.id.shownumber1);
        TextView number2 = (TextView) rl.findViewById(R.id.shownumber2);
        TextView number3 = (TextView) rl.findViewById(R.id.shownumber3);
        TextView number4 = (TextView) rl.findViewById(R.id.shownumber4);
        TextView number5 = (TextView) rl.findViewById(R.id.shownumber5);
        TextView number6 = (TextView) rl.findViewById(R.id.shownumber6);
        TextView creationDate = (TextView) rl.findViewById(R.id.showCreationDate);
        TextView uuid = (TextView) rl.findViewById(R.id.showUUID);
        
        TextView fetchDate = (TextView)  rl.findViewById(R.id.textView1);
        fetchDate.setText(R.string.fetchedDate);
        
        
        
        
		
		if(ticket != null){
			number1.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(0))));
			number2.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(1))));
			number3.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(2))));
			number4.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(3))));
			number5.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(4))));
			number6.setText(String.valueOf(new Integer(ticket.getLottaryNumbers().get(5))));
			
			String currentDateTimeString = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ticket.getTicketCreationDate());
	        creationDate.setText(currentDateTimeString);
	        String fetchTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ticket.getTicketFetchedTime());
	        uuid.setText(fetchTime);
		}
		
		
		return inflater.inflate(R.layout.showticket, container, false);
	}
	
	

}
