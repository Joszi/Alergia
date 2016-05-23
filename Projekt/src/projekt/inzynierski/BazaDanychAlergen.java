package projekt.inzynierski;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaDanychAlergen {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAZWA = "nazwa_alergenu";
	public static final String KEY_OPIS = "opis_alergenu";

	private static final String DATABASE_NAME = "Alergen";
	private static final String DATABASE_TABLE = "Opisy";
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
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAZWA
					+ " TEXT NOT NULL, " + KEY_OPIS + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public BazaDanychAlergen(Context c) {
		ourContext = c;
	}

	public BazaDanychAlergen open() {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long createEntry(String nazwa, String opis) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAZWA, nazwa);
		cv.put(KEY_OPIS, opis);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public void deleteAllEntries() {
		ourDatabase.delete(DATABASE_TABLE, null, null);
	}

	public String getOpis(String nazwa) {
		String[] columns = new String[] { KEY_ROWID, KEY_NAZWA, KEY_OPIS };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_NAZWA + "=?",
				new String[] { nazwa }, null, null, null);
		String result = "";

		int iOpis = c.getColumnIndex(KEY_OPIS);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iOpis) + " ";
		}
		c.close();
		return result;
	}
}
