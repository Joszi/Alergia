package projekt.inzynierski;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaDanychKuracja {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATA = "data";
	public static final String KEY_TYTUL = "tytul";
	public static final String KEY_OPIS = "opis";

	private static final String DATABASE_NAME = "Leczenie";
	private static final String DATABASE_TABLE = "Kuracja";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATA
					+ " TEXT NOT NULL, " + KEY_TYTUL + " TEXT NOT NULL, "
					+ KEY_OPIS + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public BazaDanychKuracja(Context c) {
		ourContext = c;
	}

	public BazaDanychKuracja open() {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long createEntry(String data, String tytul, String opis) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATA, data);
		cv.put(KEY_TYTUL, tytul);
		cv.put(KEY_OPIS, opis);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public void deleteAllEntries() {
		ourDatabase.delete(DATABASE_TABLE, null, null);
	}

	public void deleteEntry(int id) {
		ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + id, null);
	}

	public String getData() {
		String[] columns = new String[] { KEY_ROWID, KEY_DATA, KEY_TYTUL,
				KEY_OPIS };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String result = "";

		int iData = c.getColumnIndex(KEY_DATA);
		int iTytul = c.getColumnIndex(KEY_TYTUL);
		int iOpis = c.getColumnIndex(KEY_OPIS);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iData) + " " + c.getString(iTytul)
					+ " " + c.getString(iOpis) + " ";
		}
		c.close();
		return result;
	}

	public String getEntry(int id) {
		String[] columns = new String[] { KEY_ROWID, KEY_DATA, KEY_TYTUL,
				KEY_OPIS };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "="
				+ id, null, null, null, null);
		String result = "";

		int iKey = c.getColumnIndex(KEY_ROWID);
		int iData = c.getColumnIndex(KEY_DATA);
		int iTytul = c.getColumnIndex(KEY_TYTUL);
		int iOpis = c.getColumnIndex(KEY_OPIS);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = c.getString(iKey) + " " + c.getString(iTytul)
					+ " " + c.getString(iData) + " " + c.getString(iOpis);
		}
		c.close();
		return result;
	}

	public String getKey(String tytul) {
		String[] columns = new String[] { KEY_ROWID };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_TYTUL + "=?",
				new String[] { tytul }, null, null, null);
		String result = "";

		int iKey = c.getColumnIndex(KEY_ROWID);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = c.getString(iKey);
		}
		c.close();
		return result;
	}

	public String getDateTytul() {
		String[] columns = new String[] { KEY_ROWID, KEY_DATA, KEY_TYTUL };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, KEY_DATA + " DESC");
		String result = "";

		int iData = c.getColumnIndex(KEY_DATA);
		int iTytul = c.getColumnIndex(KEY_TYTUL);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iData) + c.getString(iTytul) + " ";
		}
		c.close();
		return result;
	}

	public String getTytul() {
		String[] columns = new String[] { KEY_ROWID, KEY_TYTUL };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String result = "";

		int iTytul = c.getColumnIndex(KEY_TYTUL);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iTytul);
		}
		c.close();
		return result;
	}

	public String getOpis(String tytul) {
		String[] columns = new String[] { KEY_ROWID, KEY_TYTUL, KEY_OPIS };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_TYTUL + "=?",
				new String[] { tytul }, null, null, null);
		String result = "";

		int iOpis = c.getColumnIndex(KEY_OPIS);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iOpis);
		}
		c.close();
		return result;
	}
}
