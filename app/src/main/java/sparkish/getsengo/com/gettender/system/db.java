package sparkish.getsengo.com.gettender.system;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by cyclone on 06/07/15.
 */

public class db extends SQLiteOpenHelper {
    static config cfg;
    private SQLiteDatabase dbA = null;

    private static final int DATABASE_VERSION = Integer.valueOf(cfg.db_version);

    private static final String DATABASE_NAME = String.valueOf(cfg.db_name);
    private static final String[] TABLE_DEFAULT = cfg.db_default;
    private static final String[][] TABLE_UPGRADE = cfg.db_upgrade;

    private static final String paramBefore = "";
    private static final String paramAfter = "";

    private boolean isCreating = false;

    private db dbne;

    //namanya sama kaya class sama aja constructor
    public db(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        cfg = new config(context);
        //super.getReadableDatabase();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase dbne) {
        try {
            isCreating = true;

            for (String sql : TABLE_DEFAULT) {
                if (sql != null && !sql.trim().equals("")) {
                    dbne.execSQL(sql);
                }
            }

        } catch (SQLiteException e) {
            Log.w("SQLL", "error create db on " + e.toString());
        } finally {
            isCreating = false;
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {

        if (dbA != null && dbA.isOpen()) {
            while (dbA.isDbLockedByCurrentThread()) {
                //db is locked, keep looping
            }
        }


        if (isCreating && dbA != null) {
            return dbA;
        } else {
            try {
                return super.getWritableDatabase();
            } catch (Exception e) {
                Log.w("SQLL", "error gw " + e.toString());

                return dbA;
            }
        }
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {

        if (dbA != null && dbA.isOpen()) {
            while (dbA.isDbLockedByCurrentThread()) {
                //db is locked, keep looping
            }
        }

        if (isCreating && dbA != null) {
            return dbA;
        } else {
            try {
                return super.getReadableDatabase();
            } catch (Exception e) {
                Log.w("SQLL", "error gr " + e.toString());

                return dbA;
            }
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase dbne, int oldVersion, int newVersion) {
        try {
            isCreating = true;

            String[] sqlLoop = null;

            switch (oldVersion) {
                case 1:
                    sqlLoop = TABLE_UPGRADE[0];
                    break;
                case 2:
                    sqlLoop = TABLE_UPGRADE[1];
                    break;
            }

            if (sqlLoop != null) {
                for (String sql : sqlLoop) {
                    if (sql != null && !sql.trim().equals("")) {
                        dbne.execSQL(sql);

                        Log.w("SQLL", "updatedb " + sql);
                    }
                }
            }

        } catch (SQLiteException e) {
            Log.w("SQLL", "error upgrade db on " + e.toString());
        } finally {
            isCreating = false;
        }
    }

    public void finalize() throws Throwable {
        if (null != dbne)
            dbne.close();
        if (null != dbA)
            dbA.close();
        super.finalize();
    }

    // Adding data
    public boolean insert(String[][] argAr, String tbl) {

        String[][] data = argAr;

        ContentValues values = new ContentValues();

        for (String[] val : data) if (val[0] != null) values.put(val[0], val[1]);

        boolean ret = true;

        try {
            this.startDB();
            // Inserting Row
            dbA.insert(tbl, null, values);
            // Closing database connection
        } catch (SQLiteException e) {
            Log.w("SQLL", "error insert db data on " + e.toString());
            ret = false;
            this.closeDB();
        } finally {
            this.closeDB();
        }

        return ret;
    }

    public boolean update(String[][] argAr, String tbl) {
        return update(argAr, tbl, "");
    }

    public boolean update(String[][] argAr, String tbl, String param) {

        String[][] data = argAr;
        //SQLiteDatabase db   = this.getWritableDatabase();

        String sql = "update " + tbl + " set ";

        Boolean ret = false;

        ContentValues values = new ContentValues();
        for (int i = 0; i < data.length; i++) {
            if (data[i][0] != null) {
                values.put(data[i][0], data[i][1]);

                sql += data[i][0] + " = '" + data[i][1] + "' ";

                if (data.length > 1 && i < data.length - 1) {
                    sql += ", ";
                }
            }
        }

        if (!param.equals("")) {
            sql += " where " + param;
        }

        try {
            this.startDB();
            dbA.beginTransaction();
            // updating row
            if (sql != null && !sql.trim().equals("")) {
                dbA.execSQL(sql);
            }
            dbA.setTransactionSuccessful();

        } catch (SQLiteException e) {
            Log.w("SQLL", "error update db at " + e.toString());
            this.closeDB();
        } finally {
            ret = true;

            dbA.endTransaction(); // commit or rollback
            // Closing database connection
            this.closeDB();
        }

        return ret;
    }

    public boolean delete(String tbl) {
        return delete(tbl, "");
    }

    public boolean delete(String tbl, String where) {

        //SQLiteDatabase db = this.getWritableDatabase();

        String sql = "delete from " + tbl;

        Boolean ret = false;

        if (!where.equals("")) {
            sql += " where " + where;
        }

        //Log.w("sql delete",sql);

        try {
            this.startDB();
            dbA.beginTransaction();
            // updating row
            if (sql != null && !sql.trim().equals("")) {
                dbA.execSQL(sql);
            }
            dbA.setTransactionSuccessful();

        } catch (SQLiteException e) {
            Log.w("SQLL", "error delete db at " + e.toString());
            this.closeDB();
        } finally {
            ret = true;

            dbA.endTransaction(); // commit or rollback
            // Closing database connection
            this.closeDB();
        }

        return ret;
    }

    public long count(String tbl) {

        //SQLiteDatabase db = this.getWritableDatabase();

        long count = 0;

        try {
            this.startDB();
            count = DatabaseUtils.queryNumEntries(dbA, tbl);
            this.closeDB();
        } catch (Exception e) {

        }

        return count;
    }

    public long count(String tbl, String param) {

        //SQLiteDatabase db = this.getWritableDatabase();

        String sql = "select count(*) from " + tbl + " ";
        if (!param.equals("")) {
            sql += " where " + param;
        }

        Long jml = 0l;

        try {
            this.startDB();
            Cursor cursor = dbA.rawQuery(sql, null);

            if (null != cursor) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    jml = cursor.getLong(0);
                }
                cursor.close();
            }

        } catch (SQLiteException e) {
            Log.w("SQLL", "error count db on" + e.toString());
            this.closeDB();
        } finally {
            this.closeDB();
        }

        return jml;
    }

    public String[][] select(String tbl) {
        return select(tbl, "", "", "");
    }

    public String[][] select(String tbl, String field) {
        return select(tbl, field, "", "", "");
    }

    public String[][] select(String tbl, String field, String param) {
        return select(tbl, field, param, "", "");
    }

    public String[][] select(String tbl, String field, String param, String paramBefore) {
        return select(tbl, field, param, paramBefore, "");
    }

    public String[][] select(String tbl, String field, String param, String paramBefore, String paramAfter) {

        // Select All Query
        String sql = "";

        if (!paramBefore.equals("")) {
            sql += paramBefore + " ";
        }

        if (field.equals("")) {
            field = "*";
        }

        sql += "SELECT  " + field + " FROM " + tbl + " ";

        if (!param.equals("")) {
            sql += param;
        }

        if (!paramAfter.equals("")) {
            sql += " " + paramAfter;
        }

        String[][] ret = new String[0][0];

        try {
            this.startDB();
            Cursor cursor = dbA.rawQuery(sql, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                int i_1 = 0;
                ret = new String[cursor.getCount()][cursor.getColumnCount()];
                do {
                    for (int i_2 = 0; i_2 < cursor.getColumnCount(); i_2++) {
                        ret[i_1][i_2] = cursor.getString(i_2);
                    }
                    i_1++;
                } while (cursor.moveToNext());
            }

        } catch (SQLiteException e) {
            Log.w("SQLL", "select db on " + e.toString());
            this.closeDB();
        } finally {
            this.closeDB();
        }

        // return data
        return ret;
    }

    public boolean custom(String sql) {

        Boolean ret = false;

        try {
            this.startDB();
            dbA.beginTransaction();
            // updating row
            if (sql != null && !sql.trim().equals("")) {
                dbA.execSQL(sql);
            }
            dbA.setTransactionSuccessful();

        } catch (SQLiteException e) {
            Log.d("error custom db at", e.toString());
            ret = false;
            this.closeDB();
        } finally {
            ret = true;

            dbA.endTransaction(); // commit or rollback
            // Closing database connection
            this.closeDB();
        }

        return ret;
    }

    public boolean reset() {
        return custom(cfg.db_reset);
    }

    protected void startDB() {
        try {
            if (dbA != null && dbA.isOpen()) {
                dbA = dbA;
            } else {
                dbA = this.getWritableDatabase();
            }
        } catch (SQLiteException e) {
            Log.w("SQLL", e.toString());
        }
    }

    protected void closeDB() {

        try {
            if (dbA != null && dbA.isOpen()) {

                while (dbA.isDbLockedByCurrentThread()) {
                    //db is locked, keep looping
                }

                dbA.close();
            }

        } catch (SQLiteException e) {
            Log.w("SQLL", e.toString());
        }
    }

    public String getConfig(String param) {
        String ret = "";

        try {
            String[][] data = new String[1][2];

            long cnt = 0;
            cnt = this.count("konfigurasi", "param='" + param + "' ");

            if (cnt > 0) {
                data = this.select("konfigurasi", "nilai", "where param='" + param + "' ");
                ret = data[0][0];
            }
        } catch (Exception e) {
            Log.w("SQLL", e);
        }

        return ret == null ? "" : ret;
    }

    public Boolean setConfig(String param, String nilai) {
        try {
            String[][] data = new String[2][4];

            if (this.count("konfigurasi", "param='" + param + "' ") == 0) {
                data[0][0] = "param";
                data[0][1] = param;
                data[1][0] = "nilai";
                data[1][1] = nilai;
                this.insert(data, "konfigurasi");
            } else {
                data[0][0] = "param";
                data[0][1] = param;
                data[1][0] = "nilai";
                data[1][1] = nilai;
                this.update(data, "konfigurasi", "param='" + param + "' ");
            }
        } catch (Exception e) {
            Log.w("SQLL", e);
            return false;
        } finally {
            return true;
        }
    }

    public int getLastAutoIncrement() {
        int ret;

        String retcfg = this.getConfig("autonum");

        if (!retcfg.equals("")) {
            ret = Integer.valueOf(retcfg) + 1;
            this.setConfig("autonum", String.valueOf(ret));
        } else {
            this.setConfig("autonum", "1");
            ret = 1;
        }

        //Log.w("numnye",String.valueOf(ret));
        return ret;
    }
}
