package de.htwg.mocomp.lotteryapp.database;

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
	public static final String TICKET_ID = "_id"; 
	public static final String TICKET_UUID = "uuid";
	private static final String TICKET_NUMBER1 = "number1";
	private static final String TICKET_NUMBER2 = "number2";
	private static final String TICKET_NUMBER3 = "number3";
	private static final String TICKET_NUMBER4 = "number4";
	private static final String TICKET_NUMBER5 = "number5";
	private static final String TICKET_NUMBER6 = "number6";
	
	public LotteryAppDatabaseAdapter(Context context) {
		this.context = context;
	}
	
	public LotteryAppDatabaseAdapter open() throws SQLException{
		dbHelper = new LotteryAppDatabaseHelper(context);
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
		return database.insert(TABLE_NAME, null, contentValues);
	}
	
	public Cursor getAllTickets(){
		return database.query(TABLE_NAME,
							new String[] {	TICKET_ID, TICKET_UUID, TICKET_NUMBER1, TICKET_NUMBER2,
											TICKET_NUMBER3, TICKET_NUMBER4, TICKET_NUMBER5, TICKET_NUMBER6 }, 
							null, null, null, null, null);
	}
	
	public int removeTicket(long id){
		return database.delete(TABLE_NAME, TICKET_ID + "=" + id, null);
	}
}