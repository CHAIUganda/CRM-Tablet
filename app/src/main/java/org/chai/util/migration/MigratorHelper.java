package org.chai.util.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 06-Apr-15.
 */
public abstract class MigratorHelper {
    public abstract void onUpgrade(SQLiteDatabase db);

    public void dropColumns(SQLiteDatabase db, String createTableCmd, String tableName, List<String> colsToRemove) {
        try {
            List<String> updatedTableColumns = getTableColumns(db, tableName);
            updatedTableColumns.removeAll(colsToRemove);

            String columnsSeperated = TextUtils.join(",", updatedTableColumns);

            db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tableName + "_old;");

            // Creating the table on its new format (no redundant columns)
            db.execSQL(createTableCmd);

            // Populating the table with the data
            db.execSQL("INSERT INTO " + tableName + "(" + columnsSeperated + ") SELECT "
                    + columnsSeperated + " FROM " + tableName + "_old;");
            db.execSQL("DROP TABLE " + tableName + "_old;");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getTableColumns(SQLiteDatabase db, String tableName) {
        List<String> columns = new ArrayList<String>();
        String cmd = "pragma table_info(" + tableName + ");";
        Cursor cur = db.rawQuery(cmd, null);

        while (cur.moveToNext()) {
            columns.add(cur.getString(cur.getColumnIndex("name")));
        }
        cur.close();

        return columns;
    }
}
