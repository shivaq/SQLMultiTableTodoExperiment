package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite.Query;
import com.squareup.sqldelight.SqlDelightStatement;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;

/**
 * Created by Yasuaki on 2017/05/10.
 */

public class DbCrudHelper {

  private final BriteDatabase briteDatabase;
  private Cursor todoCursor;

  @Inject
  public DbCrudHelper(BriteDatabase briteDatabase) {
    this.briteDatabase = briteDatabase;
  }

  public BriteDatabase getBriteDb() {
    return briteDatabase;
  }


  public Observable<Cursor> loadTodo() {
    SQLiteDatabase sqLiteDatabase = briteDatabase.getReadableDatabase();
    SqlDelightStatement selectAllQuery = Todo.TODO_FACTORY.select_all();

    Timber.d("DbCrudHelper:call: loadTodo todoCursor is %s", todoCursor);
    return  briteDatabase.createQuery(Todo.TABLE_NAME, selectAllQuery.statement, new String[0])
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        });
      }

}
