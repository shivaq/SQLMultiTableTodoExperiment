package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import javax.inject.Inject;
import javax.inject.Singleton;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ApplicationContext;

@Singleton
public class DbOpenHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "todo.db";

  @Inject
  public DbOpenHelper(@ApplicationContext  Context context){
    // Db 名を指定しないと、db が保存されない
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Todo.CREATE_TABLE);

  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
