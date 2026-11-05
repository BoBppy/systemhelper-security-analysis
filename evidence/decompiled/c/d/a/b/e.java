package c.d.a.b;

import android.content.ContentValues;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class e extends SQLiteOpenHelper {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static Integer f1334b = 1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static String f1335c = "systemhelper";
    public static String d = "dmregister";
    private static AtomicInteger e = new AtomicInteger();
    private static e f = null;

    private e(Context context, String str, int i) {
        this(context, str, null, i);
    }

    private e(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    public static synchronized e a(Context context, String str, int i) {
        e.incrementAndGet();
        if (f == null) {
            f = new e(context, str, i);
        }
        return f;
    }

    public synchronized JSONObject a() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("create table if not exists dmregister (ID integer primary key autoincrement, jsonString text)");
        Cursor cursorQuery = writableDatabase.query(d, new String[]{"ID", "jsonString"}, null, null, null, null, null);
        CursorWindow cursorWindow = new CursorWindow("queryWindow", 20971520L);
        AbstractWindowedCursor abstractWindowedCursor = (AbstractWindowedCursor) cursorQuery;
        abstractWindowedCursor.setWindow(cursorWindow);
        if (abstractWindowedCursor.getCount() > 0) {
            abstractWindowedCursor.moveToLast();
            String string = abstractWindowedCursor.getString(abstractWindowedCursor.getColumnIndex("jsonString"));
            h.a("HistoryDatabaseHelper", "queryOldestRecord:" + string);
            try {
                writableDatabase.delete(d, "ID=?", new String[]{abstractWindowedCursor.getString(abstractWindowedCursor.getColumnIndex("ID"))});
                return new JSONObject(string);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        abstractWindowedCursor.close();
        cursorWindow.close();
        return null;
    }

    public synchronized void a(String str) {
        h.a("HistoryDatabaseHelper", "insertRecord:" + str);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.execSQL("create table if not exists dmregister (ID integer primary key autoincrement, jsonString text)");
        Cursor cursorQuery = writableDatabase.query(d, new String[]{"COUNT(*)"}, null, null, null, null, null);
        cursorQuery.moveToFirst();
        int i = cursorQuery.getInt(0);
        cursorQuery.close();
        int iMax = Math.max(0, i - 10);
        h.a("HistoryDatabaseHelper", "row count :" + i + ", deletecount: " + iMax);
        if (iMax > 0) {
            Cursor cursorQuery2 = writableDatabase.query(d, new String[]{"ID"}, null, null, null, null, "ID ASC", String.valueOf(iMax));
            if (cursorQuery2.moveToFirst()) {
                do {
                    int i2 = cursorQuery2.getInt(cursorQuery2.getColumnIndex("ID"));
                    h.a("HistoryDatabaseHelper", "idToDelete : " + i2);
                    writableDatabase.delete(d, "ID=?", new String[]{String.valueOf(i2)});
                } while (cursorQuery2.moveToNext());
            }
            cursorQuery2.close();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("jsonString", str);
        writableDatabase.insert(d, null, contentValues);
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
        h.a("HistoryDatabaseHelper", "onCreate table-->");
        sQLiteDatabase.execSQL("create table if not exists dmregister (ID integer primary key autoincrement, jsonString text)");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public synchronized void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }
}
