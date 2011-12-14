package de.htwg.mocomp.lotteryapp.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LotteryAppDatabaseAdapter {

	private Context context;
	private LotteryAppDatabaseHelper dbHelper;
	private SQLiteDatabase database;
	

	private static final String TABLE_NAME = "tickets";
	private static final String TABLE_WINNING_NAME = "winningtickets";
	public static final String TICKET_ID = "_id"; 
	public static final String TICKET_UUID = "uuid";
	private static final String TICKET_NUMBER1 = "number1";
	private static final String TICKET_NUMBER2 = "number2";
	private static final String TICKET_NUMBER3 = "number3";
	private static final String TICKET_NUMBER4 = "number4";
	private static final String TICKET_NUMBER5 = "number5";
	private static final String TICKET_NUMBER6 = "number6";
	public static final String AMOUNT_OF_WINNING = "winningamount";
	public static final String TICKET_DATE = "ticketdate";
	private static final String TICKET_FETCH_DATE = "fetchdate";
	
	public LotteryAppDatabaseAdapter(Context context) {
		this.context = context;
	}
	
	public LotteryAppDatabaseAdapter open() throws SQLException{
		if(dbHelper != null)
			dbHelper.close();
		dbHelper = new LotteryAppDatabaseHelper(context);
		if(database == null) {
			database = dbHelper.getWritableDatabase();
			database.setLockingEnabled(false);
		}
			
		return this;
	}
	
	public void close() {
		if(dbHelper != null)
			dbHelper.close();
	}
	
	public long insertNewTicket(LotteryTicket ticket){
		ContentValues contentValues = new ContentValues();
		contentValues.put(TICKET_UUID, ticket.getUuid());
		int i = 1;
		for(Integer number : ticket.getLottaryNumbers()){
			contentValues.put("number" + i++, number);
		}
		contentValues.put(AMOUNT_OF_WINNING, 0);
		
		
		contentValues.put(TICKET_DATE, ticket.getTicketCreationDate().getTime());
		return database.insert(TABLE_NAME, null, contentValues);
	}
	
	public Cursor getAllTickets(){
		return database.query(TABLE_NAME,
							new String[] {	TICKET_ID, TICKET_UUID, TICKET_NUMBER1, TICKET_NUMBER2,
											TICKET_NUMBER3, TICKET_NUMBER4, TICKET_NUMBER5, TICKET_NUMBER6, TICKET_DATE, AMOUNT_OF_WINNING }, 
							null, null, null, null, null);
	}
	
	public int removeTicket(long id){
		return database.delete(TABLE_NAME, TICKET_ID + "=" + id, null);
	}
	
	public Cursor getTicket(int id){
		String select = "SELECT * FROM " + TABLE_NAME +" WHERE _id == " + id;
		Cursor cursor = database.rawQuery(select, null);
		if(cursor != null) 
			cursor.moveToFirst();
		return cursor;
	}
	
	public void updateTicket(LotteryTicket ticket){
		ContentValues values = new ContentValues();
		values.put(TICKET_UUID, ticket.getUuid().toString());
		values.put(TICKET_DATE, ticket.getTicketCreationDate().getTime());
		values.put(AMOUNT_OF_WINNING, 0);
		int i = 1;
		for (Integer n: ticket.getLottaryNumbers()) {
			values.put("number"+ i++, n);
		}
		database.update(TABLE_NAME, values , TICKET_ID + " = " + ticket.getId(), null);
	}
	
	

	
	
	public long insertNewWinningTicket(LotteryTicket ticket){
		ContentValues contentValues = new ContentValues();
		contentValues.put(TICKET_FETCH_DATE, ticket.getTicketFetchedTime().getTime());
		int i = 1;
		for(Integer number : ticket.getLottaryNumbers()){
			contentValues.put("number" + i++, number);
		}
		contentValues.put(TICKET_DATE, ticket.getTicketCreationDate().getTime());
		return database.insert(TABLE_WINNING_NAME, null, contentValues);
	}
	
	
	public void updateWinningTicket(LotteryTicket ticket){
		ContentValues values = new ContentValues();
		values.put(TICKET_FETCH_DATE, ticket.getTicketFetchedTime().getTime());
		values.put(TICKET_DATE, ticket.getTicketCreationDate().getTime());
		int i = 1;
		for (Integer n: ticket.getLottaryNumbers()) {
			values.put("number"+ i++, n);
		}
		database.update(TABLE_WINNING_NAME, values , TICKET_ID + " = " + 1, null);
	}
	
	public Cursor getWinningTicket(){
		String select = "SELECT * FROM " + TABLE_WINNING_NAME +" WHERE _id == " + 1;
		Cursor cursor = database.rawQuery(select, null);
		if(cursor != null) 
			cursor.moveToLast();
		return cursor;
	}
	
	
	
	public int checkAmountOfRightNumbers() {
		Cursor winningCursor = getWinningTicket();
		List<Integer> winningNumbers = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++) {
			winningNumbers.add(winningCursor.getInt(i));
		}
		winningCursor.close();
		
		
		Cursor allTickets = getAllTickets();
		allTickets.moveToFirst();
		int winningTickets = 0;
		while (allTickets.isAfterLast() == false) {
			int winAmount = 0;
			for (int i = 1; i < 7; i++) {
//				System.out.println(allTickets.getColumnIndex("number"+i));
				if(winningNumbers.contains(allTickets.getInt(allTickets.getColumnIndex("number"+i)))){
					winAmount++;
				}
			}
			if(winAmount > 0) winningTickets++;
			ContentValues values = new ContentValues();
			values.put(AMOUNT_OF_WINNING, winAmount);
			database.update(TABLE_NAME, values , TICKET_ID + " = " + allTickets.getInt(0), null);
						
       	    allTickets.moveToNext();
        }
		
		return winningTickets;
	}
}
