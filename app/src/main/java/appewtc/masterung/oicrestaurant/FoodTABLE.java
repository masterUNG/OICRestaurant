package appewtc.masterung.oicrestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 3/6/15 AD.
 */
public class FoodTABLE {

    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeDatabase, readDatabase;

    public FoodTABLE(Context context) {

        objMyOpenHelper = new MyOpenHelper(context);
        writeDatabase = objMyOpenHelper.getWritableDatabase();
        readDatabase = objMyOpenHelper.getReadableDatabase();

    }   // Constructor

}   // Main Class
