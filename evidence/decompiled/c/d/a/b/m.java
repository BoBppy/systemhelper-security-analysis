package c.d.a.b;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class m extends SQLiteOpenHelper {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static Integer f1341b = 1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static String f1342c = "signalhelper";
    public static String d = "signal";
    private static AtomicInteger e = new AtomicInteger();
    private static m f = null;

    private m(Context context, String str, int i) {
        this(context, str, null, i);
    }

    private m(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    public static synchronized m a(Context context, String str, int i) {
        e.incrementAndGet();
        if (f == null) {
            f = new m(context, str, i);
        }
        return f;
    }

    public synchronized void a() {
        h.a("SignalDatabaseHelper", "clearSignalDB-->");
        getWritableDatabase().delete(d, null, null);
    }

    public synchronized void a(String str) {
        h.a("SignalDatabaseHelper", "insertRecord:" + str);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("create table if not exists signal (ID integer primary key autoincrement, jsonString text)");
        Cursor cursorQuery = writableDatabase.query(d, new String[]{"ID", "jsonString"}, null, null, null, null, null);
        if (cursorQuery.getCount() >= 100) {
            cursorQuery.moveToFirst();
            writableDatabase.delete(d, "ID=?", new String[]{String.valueOf(cursorQuery.getInt(cursorQuery.getColumnIndex("ID")))});
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("jsonString", str);
        writableDatabase.insert(d, null, contentValues);
        cursorQuery.close();
    }

    public synchronized JSONArray b() {
        JSONArray jSONArray;
        Cursor cursorQuery = getWritableDatabase().query(d, new String[]{"jsonString"}, null, null, null, null, null);
        CursorWindow cursorWindow = new CursorWindow("queryWindow", 20971520L);
        AbstractWindowedCursor abstractWindowedCursor = (AbstractWindowedCursor) cursorQuery;
        abstractWindowedCursor.setWindow(cursorWindow);
        jSONArray = new JSONArray();
        if (abstractWindowedCursor.getCount() > 0) {
            while (abstractWindowedCursor.moveToNext()) {
                try {
                    jSONArray.put(new JSONObject(abstractWindowedCursor.getString(abstractWindowedCursor.getColumnIndex("jsonString"))));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        } else {
            h.a("SignalDatabaseHelper", "signal database is null");
        }
        abstractWindowedCursor.close();
        cursorWindow.close();
        return jSONArray;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper, java.lang.AutoCloseable
    public synchronized void close() {
        super.close();
        if (e.decrementAndGet() == 0) {
            f = null;
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public synchronized void onCreate(SQLiteDatabase sQLiteDatabase) {
        h.a("SignalDatabaseHelper", "onCreate-->");
        sQLiteDatabase.execSQL("create table if not exists signal (ID integer primary key autoincrement, jsonString text)");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public synchronized void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
