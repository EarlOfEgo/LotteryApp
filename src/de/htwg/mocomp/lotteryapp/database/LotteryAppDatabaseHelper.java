package de.htwg.mocomp.lotteryapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LotteryAppDatabaseHelper extends SQLiteOpenHelper{

	private static final String DBNAME = "lotteryticketapp";
	private static final int DBVERSION = 1;
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
	private static final String AMOUNT_OF_WINNING = "winningamount";
	private static final String TICKET_DATE = "ticketdate";
	private static final String TICKET_FETCH_DATE = "fetchdate";
	private static final String LotteryAppCreateTable = 
									"CREATE TABLE " + TABLE_NAME + " (" +
									TICKET_ID + " integer primary key autoincrement, " +
									TICKET_UUID + " TEXT not NULL, " +
									TICKET_NUMBER1 + " INTEGER not NULL, " +
									TICKET_NUMBER2 + " INTEGER not NULL, " +
									TICKET_NUMBER3 + " INTEGER not NULL, " +
									TICKET_NUMBER4 + " INTEGER not NULL, " +
									TICKET_NUMBER5 + " INTEGER not NULL, " +
									TICKET_NUMBER6 + " INTEGER not NULL, " +
									AMOUNT_OF_WINNING + " INTEGER not NULL, " +
									TICKET_DATE + " LONG not NULL);";
	
	private static final String LotteryAppCreateTableWinningTicket =
									"CREATE TABLE " + TABLE_WINNING_NAME + " (" +
									TICKET_ID + " integer primary key autoincrement, " +
									TICKET_NUMBER1 + " INTEGER not NULL, " +
									TICKET_NUMBER2 + " INTEGER not NULL, " +
									TICKET_NUMBER3 + " INTEGER not NULL, " +
									TICKET_NUMBER4 + " INTEGER not NULL, " +
									TICKET_NUMBER5 + " INTEGER not NULL, " +
									TICKET_NUMBER6 + " INTEGER not NULL, " +
									TICKET_FETCH_DATE + " LONG not NULL, " +
									TICKET_DATE + " LONG not NULL);";


	public LotteryAppDatabaseHelper(Context context) {
		super(context, DBNAME, null, DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub//
		System.out.println("LALALALALALALAAL");
		System.out.println(LotteryAppCreateTable);
		db.execSQL(LotteryAppCreateTable);
		db.execSQL(LotteryAppCreateTableWinningTicket);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WINNING_NAME);
		onCreate(db);
	}

}
