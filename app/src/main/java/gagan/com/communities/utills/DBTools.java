package gagan.com.communities.utills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBTools extends SQLiteOpenHelper
{

    public DBTools(Context applicationContext)
    {
        super(applicationContext, "neibr_g.db", null, 1);

    }

    private final String TABLE_NAME = "chat_data";

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createTasks = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY ," +
                "message_id TEXT, " +
                "sender_id TEXT, " +
                "reciever_id TEXT , " +
                "message TEXT, " +
                "time TEXT)";


        db.execSQL(createTasks);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Drop table
        db.execSQL("DROP TABLE " + TABLE_NAME + " IF EXISTS");

        // Create table again
        onCreate(db);

    }


    public static final String message_id = "message_id", sender_id = "sender_id", reciever_id = "reciever_id", message = "message", time = "time";


    public int SAVE_MSG(HashMap<String, String> taskData)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("message_id", taskData.get(message_id));
        values.put("sender_id", taskData.get(sender_id));
        values.put("reciever_id", taskData.get(reciever_id));
        values.put("message", taskData.get(message));
        values.put("time", taskData.get(time));


        int taskID = (int) database.insert(TABLE_NAME, null, values);


        database.close();


        return taskID;
    }

    public ArrayList<HashMap<String, String>> GET_CHAT_DATA(String other_user_id)
    {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
//
//        String         selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE id=" + id;
        String         selectQuery = "SELECT * FROM " + TABLE_NAME + " where  ( sender_id =" + other_user_id + " or  reciever_id =" + other_user_id + ") order by time";
        SQLiteDatabase db          = this.getReadableDatabase();
        Cursor         cursor      = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst())
        {
            do
            {
                HashMap<String, String> map = new HashMap<>();
                map.put("chat_id", cursor.getString(0));
                map.put("message_id", cursor.getString(1));
                map.put("sender_id", cursor.getString(2));
                map.put("reciever_id", cursor.getString(3));
                map.put("message", cursor.getString(4));
                map.put("time", cursor.getString(5));

                list.add(map);
            }
            while (cursor.moveToNext());

        }
        return list;

    }



   /* public int updateTask(int taskID, HashMap<String, String> taskData)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", taskData.get("name"));
        values.put("notes", taskData.get("notes"));
        values.put("deadline", taskData.get("deadline"));
        values.put("isCompleted", taskData.get("isCompleted"));

        values.put("phone", taskData.get("phone"));


        return database.update(TABLE_NAME, values, "id" + " = ?", new String[]{taskID + ""});
    }*/

    public void DELETE_CHAT(String other_user_id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String[] deleteQu = {other_user_id, other_user_id};
        database.delete("chat_data", "tripId=? AND (sender_id=? OR reciever_id=?)", deleteQu);

        database.close();
    }




}
