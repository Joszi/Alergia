package projekt.inzynierski;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaDanychAlergia {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_DZIEN = "dzien_tygodnia";
	public static final String KEY_DATA = "data";
	public static final String KEY_ALERGEN = "rodzaj_alergenu";
	public static final String KEY_WARTOSC = "wartosc_pylenia";

	private static final String DATABASE_NAME = "Alergia";
	private static final String DATABASE_TABLE = "Pylenie";
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
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DZIEN
					+ " TEXT NOT NULL, " + KEY_DATA + " TEXT NOT NULL, "
					+ KEY_ALERGEN + " TEXT NOT NULL, " + KEY_WARTOSC
					+ " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public BazaDanychAlergia(Context c) {
		ourContext = c;
	}

	public BazaDanychAlergia open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long createEntry(String dzien, String data, String alergen,
			String wartosc) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_DZIEN, dzien);
		cv.put(KEY_DATA, data);
		cv.put(KEY_ALERGEN, alergen);
		cv.put(KEY_WARTOSC, wartosc);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public void deleteAllEntries() {
		ourDatabase.delete(DATABASE_TABLE, null, null);
	}

	public String getData() {
		String[] columns = new String[] { KEY_ROWID, KEY_DZIEN, KEY_DATA,
				KEY_ALERGEN, KEY_WARTOSC };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		String result = "";

		int iDzien = c.getColumnIndex(KEY_DZIEN);
		int iData = c.getColumnIndex(KEY_DATA);
		int iAlergen = c.getColumnIndex(KEY_ALERGEN);
		int iWartosc = c.getColumnIndex(KEY_WARTOSC);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iDzien) + " " + c.getString(iData)
					+ " " + c.getString(iAlergen) + " " + c.getString(iWartosc)
					+ "\n";
		}
		c.close();
		return result;
	}

	public String getDays() {
		String[] columns = new String[] { KEY_ROWID, KEY_DZIEN, KEY_DATA };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null,
				KEY_DZIEN, null, KEY_ROWID + " ASC");
		String result = "";

		int iDzien = c.getColumnIndex(KEY_DZIEN);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iDzien) + " ";
		}
		c.close();
		return result;
	}

	public String getDate() {
		String[] columns = new String[] { KEY_ROWID, KEY_DATA };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null,
				KEY_DATA, null, KEY_ROWID + " ASC");
		String result = "";

		int iData = c.getColumnIndex(KEY_DATA);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iData) + " ";
		}
		c.close();
		return result;
	}

	public String getAlergenWartosc(String dzien) {
		String[] columns = new String[] { KEY_ROWID, KEY_DATA, KEY_ALERGEN,
				KEY_WARTOSC };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DATA + "=?",
				new String[] { dzien }, null, null, KEY_ALERGEN + " ASC");
		String result = "";

		int iAlergen = c.getColumnIndex(KEY_ALERGEN);
		int iWartosc = c.getColumnIndex(KEY_WARTOSC);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iAlergen) + c.getString(iWartosc)
					+ " ";
		}
		c.close();
		return result;
	}

	public String getAlergen() {
		String[] columns = new String[] { KEY_ROWID, KEY_ALERGEN };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null,
				KEY_ALERGEN, null, KEY_ALERGEN + " ASC");
		String result = "";

		int iAlergen = c.getColumnIndex(KEY_ALERGEN);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iAlergen) + " ";
		}
		c.close();
		return result;
	}

	public String getDateAlergen(String data, String alergen) {
		String[] columns = new String[] { KEY_ROWID, KEY_ALERGEN, KEY_WARTOSC };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_DATA
				+ "=? AND " + KEY_ALERGEN + "=?",
				new String[] { data, alergen }, null, null, null);
		String result = "";

		int iAlergen = c.getColumnIndex(KEY_ALERGEN);
		int iWartosc = c.getColumnIndex(KEY_WARTOSC);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iAlergen) + c.getString(iWartosc)
					+ " ";
		}
		c.close();
		return result;
	}
}
