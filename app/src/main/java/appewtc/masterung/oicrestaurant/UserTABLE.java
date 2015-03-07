package appewtc.masterung.oicrestaurant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 3/6/15 AD.
 */
public class UserTABLE {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeDatabase, readDatabase;

    public static final String TABLE_USER = "userTABLE";
    public static final String COLUMN_ID_USER = "_id";
    public static final String COLUMN_USER = "User";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_NAME = "Name";

    public UserTABLE(Context context) {

        objMyOpenHelper = new MyOpenHelper(context);
        writeDatabase = objMyOpenHelper.getWritableDatabase();
        readDatabase = objMyOpenHelper.getReadableDatabase();

    }   // Constructor

    //Add Value to userTABLE
    public long addValueToUser(String strUser, String strPassword, String strName) {

        return 0;
    }


}   // Main Class
