package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import android.database.Cursor;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqlbrite.SqlBrite.Builder;
import com.squareup.sqlbrite.SqlBrite.Logger;
import com.squareup.sqlbrite.SqlBrite.Query;
import com.squareup.sqldelight.SqlDelightStatement;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TodoModel.Insert_todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;

@Singleton
public class DbCrudHelper {

  private final BriteDatabase briteDatabase;
  private Todo.Insert_todo insertTodo;

  @Inject
  public DbCrudHelper(DbOpenHelper openHelper) {

    SqlBrite sqlBrite = new Builder()
        .logger(new Logger() {
          @Override
          public void log(String message) {
            Timber.d("DbCrudHelper:log: %s", message);
          }
        })
        .build();

    briteDatabase = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
    // SqlDelight で生成された コンパイル済みSQLステートメントをインスタンス化
    insertTodo = new Insert_todo(briteDatabase.getWritableDatabase());
  }

  public Observable<Cursor> loadTodo() {
    // SqlDelight が生成した SQL ステートメント
    SqlDelightStatement selectAllQuery = Todo.TODO_FACTORY.select_all();

    // 返り値は QueryObservable
    return briteDatabase.createQuery(
        Todo.TABLE_NAME,
        selectAllQuery.statement,
        new String[0])
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        });
  }

  public void insertTodo(String todo) {
    long now = System.currentTimeMillis();
    insertTodo.bind(todo, false, now, now);
    briteDatabase.executeInsert(insertTodo.table, insertTodo.program);
  }
}
