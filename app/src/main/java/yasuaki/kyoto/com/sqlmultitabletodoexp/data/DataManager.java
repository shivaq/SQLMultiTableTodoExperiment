package yasuaki.kyoto.com.sqlmultitabletodoexp.data;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.local.DbCrudHelper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;

/**
 * リモート、ローカル、プレファレンス、Service と、
 * 各種 データ取得処理の仲介地点
 */
@Singleton
public class DataManager {

  private final DbCrudHelper dbCrudHelper;

  @Inject
  public DataManager(DbCrudHelper dbCrudHelper) {
    this.dbCrudHelper = dbCrudHelper;
  }

  public Observable<List<Todo>> loadTodo() {
    Timber.d("DataManager:loadTodo: ");

    return dbCrudHelper.loadTodo()
        .map(new Func1<Cursor, List<Todo>>(){

          @Override
          public List<Todo> call(Cursor cursor) {

            List<Todo> todoList = new ArrayList<>();
            try{
              if (cursor.moveToFirst()) {
                for(int i = 0; i < cursor.getCount(); i++){
                  Todo todo = Todo.MAPPER.map(cursor);
                  Timber.d("DataManager:call: todo is %s", todo);
                  todoList.add(todo);
                  cursor.moveToNext();
                }
              }
            }finally{
              cursor.close();
            }
            return todoList;
          }
        });
  }
}