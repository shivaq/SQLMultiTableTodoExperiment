package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.TodoTag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ApplicationContext;

@Singleton
public class DbOpenHelper extends SQLiteOpenHelper implements SQLiteTransactionListener {

  public static final int DATABASE_VERSION = 16;
  private static final String DATABASE_NAME = "todo.db";

  @Inject
  public DbOpenHelper(@ApplicationContext Context context) {
    // Db 名を指定しないと、db が保存されない
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Timber.d("DbOpenHelper:DbOpenHelper: ");
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Todo.CREATE_TABLE);
    db.execSQL(Tag.CREATE_TABLE);
    db.execSQL(TodoTag.CREATE_TABLE);
    Timber.d("DbOpenHelper:onCreate: ");
  }

  @Override
  public void onOpen(SQLiteDatabase db) {
    super.onOpen(db);
    db.execSQL("PRAGMA foreign_keys=ON");
    Timber.d("DbOpenHelper:onOpen: ");
  }

  /**
   * テーブルの定義ががらっと変わってしまうような大きな変更の場合は、
   * 一旦Selectしてテーブル作成後に新しい定義でInsertするようなデータ移行
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Timber.d("DbOpenHelper:onUpgrade: oldVersion is %s, newVersion is %s", oldVersion, newVersion);
    if (oldVersion < newVersion) {
      // どのバージョンで、どんな変更をしたかは変わるじゃろ？
      if (oldVersion == 1) {
        db.execSQL(
            "ALTER TABLE " + Todo.TABLE_NAME + " ADD COLUMN isChecked INTEGER DEFAULT 0"
        );
      }
      if (oldVersion == 3) {
        db.execSQL(Tag.CREATE_TABLE);
      }
      if (oldVersion == 4) {
        db.execSQL(
            "ALTER TABLE " + Todo.TABLE_NAME + " ADD COLUMN tag INTEGER"
        );
      }
      if (oldVersion == 5) {
        db.execSQL(TodoTag.CREATE_TABLE);
      }
      if (oldVersion == 6) {
        db.execSQL(
            "DROP TABLE IF EXISTS " + TodoTag.TABLE_NAME
        );
      }
      if (oldVersion == 7) {
        db.execSQL(TodoTag.CREATE_TABLE);
      }
      if (oldVersion == 8) {
        // "ALTER TABLE " + Todo.TABLE_NAME + " DROP COLUMN tag" の代わり
        String columnsToCopyForTodo = " _id, todo, registerd_date, updated_date, isChecked ";
        db.execSQL(
            "BEGIN TRANSACTION; "
                + "ALTER TABLE " + Todo.TABLE_NAME + " RENAME TO temp_" + Todo.TABLE_NAME + ";"

                + Todo.CREATE_TABLE + ";"

                + " INSERT INTO " + Todo.TABLE_NAME
                + " SELECT" + columnsToCopyForTodo
                + "FROM temp_" + Todo.TABLE_NAME + ";"

                + " DROP TABLE temp_" + Todo.TABLE_NAME + " COMMIT;"
        );
        String columnsToCopyForTag = " _id, tag, registerd_date ";
        db.execSQL(
            "BEGIN TRANSACTION; "
                + "ALTER TABLE " + Tag.TABLE_NAME + " RENAME TO temp_" + Tag.TABLE_NAME

                + Tag.CREATE_TABLE + ";"

                + " INSERT INTO " + Tag.TABLE_NAME
                + " SELECT " + columnsToCopyForTag
                + "FROM temp_" + Tag.TABLE_NAME + ";"

                + " DROP TABLE temp_" + Tag.TABLE_NAME + " COMMIT;"
        );
      }
      if (oldVersion == 13) {
        db.execSQL("DROP TABLE IF EXISTS " + Todo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Tag.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TodoTag.TABLE_NAME);

        db.execSQL(Todo.CREATE_TABLE);
        db.execSQL(Tag.CREATE_TABLE);
        db.execSQL(TodoTag.CREATE_TABLE);
      }
      if (oldVersion == 15) {
        String columnsToCopyForTodoTag = " _id, todo_id, tag_id ";
        db.beginTransactionWithListener(this);
        try {
          db.execSQL("ALTER TABLE " + TodoTag.TABLE_NAME + " RENAME TO temp_" + TodoTag.TABLE_NAME);
          db.execSQL(TodoTag.CREATE_TABLE);
          db.execSQL("INSERT INTO " + TodoTag.TABLE_NAME
              + " SELECT " + columnsToCopyForTodoTag
              + "FROM temp_" + TodoTag.TABLE_NAME);
          db.execSQL("DROP TABLE temp_" + TodoTag.TABLE_NAME);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          db.endTransaction();
        }
      }
    }
  }

  @Override
  public void onBegin() {
    Timber.d("DbOpenHelper:onBegin: start transaction");
  }
  @Override
  public void onCommit() {
    Timber.d("DbOpenHelper:onCommit: commit transaction");
  }
  @Override
  public void onRollback() {
    Timber.d("DbOpenHelper:onRollback: rollback transaction");

  }
}
