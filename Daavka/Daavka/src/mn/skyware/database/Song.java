package mn.skyware.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Song {
	@DatabaseField(generatedId = true)
	public int id;
	@DatabaseField
	public int artist_id;
	@DatabaseField
	public String youtube_url;
	@DatabaseField
	public String lyric;
	@DatabaseField
	public String name;
	@DatabaseField
	public int level;
	@DatabaseField
	public String strum;
	@DatabaseField
	public boolean isfav;
}
