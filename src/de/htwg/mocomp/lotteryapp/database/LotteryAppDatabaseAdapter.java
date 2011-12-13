package de.htwg.mocomp.lotteryapp.database;

import java.util.ArrayList;

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
	private static final String TICKET_DATE = "ticketdate";
	private static final String TICKET_FETCH_DATE = "fetchdate";
	
	public LotteryAppDatabaseAdapter(Context context) {
		this.context = context;
	}
	
	public LotteryAppDatabaseAdapter open() throws SQLException{
		
		dbHelper = new LotteryAppDatabaseHelper(context);
		if(database == null)
			database = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public long insertNewTicket(LotteryTicket ticket){
		ContentValues contentValues = new ContentValues();
		contentValues.put(TICKET_UUID, ticket.getUuid());
		int i = 1;
		for(Integer number : ticket.getLottaryNumbers()){
			contentValues.put("number" + i++, number);
		}
		
		contentValues.put(TICKET_DATE, ticket.getTicketCreationDate().getTime());
		return database.insert(TABLE_NAME, null, contentValues);
	}
	
	public Cursor getAllTickets(){
		return database.query(TABLE_NAME,
							new String[] {	TICKET_ID, TICKET_UUID, TICKET_NUMBER1, TICKET_NUMBER2,
											TICKET_NUMBER3, TICKET_NUMBER4, TICKET_NUMBER5, TICKET_NUMBER6, TICKET_DATE }, 
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
		int i = 1;
		for (Integer n: ticket.getLottaryNumbers()) {
			values.put("number"+ i++, n);
		}
		database.update(TABLE_NAME, values , TICKET_ID + " = " + ticket.getId(), null);
	}
	
	
//	public Cursor getWinningTickets(int Number) {
//		ArrayList<LotteryTicket> tickets = new ArrayList<LotteryTicket>();
//		String query = "SELECT * FROM " + TABLE_NAME +
//						" WHERE " + TICKET_NUMBER1 + " = " + Number +
//						" OR "  + TICKET_NUMBER2 + " = " + Number +
//						" OR "  + TICKET_NUMBER3 + " = " + Number +
//						" OR "  + TICKET_NUMBER4 + " = " + Number +
//						" OR "  + TICKET_NUMBER5 + " = " + Number +
//						" OR "  + TICKET_NUMBER6 + " = " + Number + ";";
//		Cursor cursor = database.rawQuery(query, null);
//		if(cursor != null){
//			cursor.moveToFirst();
//			return cursor;
//		} else
//			return null;
//	}
	
	
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
}
