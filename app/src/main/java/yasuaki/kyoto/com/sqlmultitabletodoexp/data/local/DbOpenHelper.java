package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TodoModel.Insert_todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ApplicationContext;


public class DbOpenHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;

  public DbOpenHelper(@ApplicationContext  Context context){
    super(context, null, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Todo.CREATE_TABLE);

    Todo.Insert_todo insertTodo = new Insert_todo(db);
    long now = System.currentTimeMillis();
    insertTodo.bind("トイレ", now, now);
    insertTodo.program.executeInsert();
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
