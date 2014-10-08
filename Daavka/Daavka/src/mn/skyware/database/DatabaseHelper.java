package mn.skyware.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static int databaseVersion = 1;
	private static String databaseName = "daavka.sqlite";
	private Dao<Artist, Integer> artistDao = null;
	private Dao<Song, Integer> songDao = null;
	private Dao<Chord, Integer> chordDao=null;
	private Context _context;
	public DatabaseHelper(Context context) {
		super(context, databaseName, null, databaseVersion);
		// TODO Auto-generated constructor stub
		_context=context;
		if (!checkDataBase()) {

			try {
				this.getReadableDatabase();
				this.close();
				copyDataBase();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTableIfNotExists(connectionSource, Artist.class);
			TableUtils.createTableIfNotExists(connectionSource, Song.class);
			TableUtils.createTableIfNotExists(connectionSource, Chord.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		try {
			TableUtils.createTable(connectionSource, Artist.class);
			TableUtils.createTable(connectionSource, Song.class);
			TableUtils.createTable(connectionSource, Chord.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		onCreate(arg0);
	}

	public Dao<Artist, Integer> getArtDao() throws SQLException {
		if (artistDao == null) {
			artistDao = getDao(Artist.class);

		}
		return artistDao;

	}
	public Dao<Chord, Integer> getChordDao() throws SQLException {
		if (chordDao == null) {
			chordDao = getDao(Chord.class);

		}
		return chordDao;

	}
	public Dao<Song, Integer> getSongDao() throws SQLException {
		if (songDao == null) {
			songDao = getDao(Song.class);

		}
		return songDao;

	}
	private boolean checkDataBase() {
		File dbFile = new File(_context.getApplicationInfo().dataDir
				+ "/databases/" + "daavka.sqlite");
		Log.i("dir", dbFile.getPath());
		return dbFile.exists();
	}
	private void copyDataBase() throws IOException {
		InputStream mInput = _context.getApplicationContext().getAssets()
				.open("daavka.sqlite");
		String outFileName = _context.getApplicationInfo().dataDir
				+ "/databases/" + "daavka.sqlite";
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
		// copyJournal();
	}


}
