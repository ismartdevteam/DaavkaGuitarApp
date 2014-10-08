package mn.skyware.database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Artist  {
@DatabaseField(generatedId=true)
public int id;
@DatabaseField(canBeNull=false)
public String name;

}
