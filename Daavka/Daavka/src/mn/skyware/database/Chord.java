package mn.skyware.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Chord {
@DatabaseField(generatedId=true)
public int id;
@DatabaseField
public String name;
@DatabaseField
public String stat;
}
