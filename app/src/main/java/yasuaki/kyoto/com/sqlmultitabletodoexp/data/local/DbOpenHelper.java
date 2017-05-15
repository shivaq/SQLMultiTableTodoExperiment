package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import javax.inject.Inject;
import javax.inject.Singleton;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ApplicationContext;

@Singleton
public class DbOpenHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 5;
  private static final String DATABASE_NAME = "todo.db";

  @Inject
  public DbOpenHelper(@ApplicationContext Context context) {
    // Db 名を指定しないと、db が保存されない
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Todo.CREATE_TABLE);
    db.execSQL(Tag.CREATE_TABLE);
  }

  @Override
  public void onOpen(SQLiteDatabase db) {
    super.onOpen(db);
    db.execSQL("PRAGMA foreign_keys=ON");
  }

  /**
   * テーブルの定義ががらっと変わってしまうような大きな変更の場合は、
   * 一旦Selectしてテーブル作成後に新しい定義でInsertするようなデータ移行
   */
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
    }
  }
}
