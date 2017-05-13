package yasuaki.kyoto.com.sqlmultitabletodoexp.data;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.functions.Func1;
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

    return dbCrudHelper.loadTodo()
        // カーソルを、List に変換する
        // map →Observable が emit した各アイテムに、function を適用していく
        .map(new Func1<Cursor, List<Todo>>(){
          @Override
          public List<Todo> call(Cursor cursor) {

            List<Todo> todoList = new ArrayList<>();
            try{
              if (cursor.moveToFirst()) {
                for(int i = 0; i < cursor.getCount(); i++){
                  Todo todo = Todo.MAPPER.map(cursor);
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

  public void insertTodo(String todo) {
    dbCrudHelper.insertTodo(todo);
  }

  public void updateTodoIsChecked(boolean isChecked, long id){
    dbCrudHelper.updateTodoIsChecked(isChecked, id);
  }

  public void updateTodo(String addedTodo, long todoId) {
    dbCrudHelper.updateTodoString(addedTodo, todoId);
  }

  public int deleteTodo(long todoId) {
    return dbCrudHelper.deleteTodo(todoId);
  }
}