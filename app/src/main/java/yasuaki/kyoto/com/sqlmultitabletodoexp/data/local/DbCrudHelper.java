package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import static yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag.TAG_FACTORY;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqlbrite.SqlBrite.Builder;
import com.squareup.sqlbrite.SqlBrite.Query;
import com.squareup.sqldelight.SqlDelightStatement;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TagModel.Insert_tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TodoModel.Delete_todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TodoModel.Insert_todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TodoModel.Update_isChecked;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TodoModel.Update_todoString;
import yasuaki.kyoto.com.sqlmultitabletodoexp.Todo_TagModel.Delete_todo_by_todo_id;
import yasuaki.kyoto.com.sqlmultitabletodoexp.Todo_TagModel.Insert_todo_tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.TodoTag;

@Singleton
public class DbCrudHelper {

  private final BriteDatabase briteDatabase;
  private Todo.Insert_todo insertTodo;
  private Todo.Update_isChecked updateTodoIsChecked;
  private Todo.Update_todoString updateTodoString;
  private Todo.Delete_todo deleteTodo;

  private Tag.Insert_tag insertTag;

  private TodoTag.Insert_todo_tag insertTodoTag;
  private TodoTag.Delete_todo_by_todo_id deleteTodoByTodoId;

  @Inject
  public DbCrudHelper(DbOpenHelper openHelper) {

    SqlBrite sqlBrite = new Builder()
//        .logger(new Logger() {
//          @Override
//          public void log(String message) {
//            Timber.d("DbCrudHelper:log: %s", message);
//          }
//        })
        .build();

    briteDatabase = sqlBrite.wrapDatabaseHelper(openHelper, Schedulers.io());
    SQLiteDatabase sqLiteWritableDatabase = briteDatabase.getWritableDatabase();

    // SqlDelight で生成された コンパイル済みSQLステートメントをインスタンス化
    insertTodo = new Insert_todo(sqLiteWritableDatabase);
    updateTodoIsChecked = new Update_isChecked(sqLiteWritableDatabase);
    updateTodoString = new Update_todoString(sqLiteWritableDatabase);
    deleteTodo = new Delete_todo(sqLiteWritableDatabase);

    insertTag = new Insert_tag(sqLiteWritableDatabase);
    insertTodoTag = new Insert_todo_tag(sqLiteWritableDatabase);
    deleteTodoByTodoId = new Delete_todo_by_todo_id(sqLiteWritableDatabase);
  }

  /***************************** load **********************************************/
  public Observable<Cursor> loadTodo() {
    // SqlDelight が生成した SQL ステートメント
    SqlDelightStatement selectAllTodoQuery = Todo.TODO_FACTORY.select_all();

    // 返り値は QueryObservable
    return briteDatabase.createQuery(
        Todo.TABLE_NAME,
        selectAllTodoQuery.statement,
        selectAllTodoQuery.args)
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        });
  }

  public Observable<Cursor> loadTag() {
    SqlDelightStatement selectAllTagQuery = TAG_FACTORY.select_all();
    return briteDatabase.createQuery(
        Tag.TABLE_NAME, selectAllTagQuery.statement, selectAllTagQuery.args)
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        });
  }

  public Observable<Cursor> loadTodoTag(long todoId) {
    SqlDelightStatement selectTagsForTodoId = TodoTag.TODO_TAG_FACTORY
        .select_tags_for_todoid(todoId);
    return briteDatabase.createQuery(
        TodoTag.TABLE_NAME, selectTagsForTodoId.statement, selectTagsForTodoId.args)
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        });
  }

  /***************************** insert **********************************************/

  public long insertTodo(String todoStr, String addedTagStr, List<Long> checkedTagIdList) {
    Timber.d("DbCrudHelper:insertTodo: ");
    long now = System.currentTimeMillis();

    // Todoテーブル にInsert
    insertTodo.bind(todoStr, false, now, now);
    long newTodoId = briteDatabase.executeInsert(insertTodo.table, insertTodo.program);
    Timber.d("DbCrudHelper:insertTodo: Id_%s: %s is inserted into todo table", newTodoId, todoStr);

    // 新規挿入時に、タグも追加されているかどうか
    if (addedTagStr.length() != 0) {
      // タグテーブルにInsert
      long newTagId = insertTag(addedTagStr);
      Timber.d("DbCrudHelper:insertTodo: ID_%s: %s is inserted into tag table", newTagId, addedTagStr);
      // TodoTag テーブルにも挿入
      insertTodoTag.bind(newTodoId, newTagId);
      long todoTagId = briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
      Timber.d("DbCrudHelper:insertTodo: ID_%s is inserted into TodoTag table", todoTagId);
    }

    if (checkedTagIdList != null) {
      for (long checkedTagId : checkedTagIdList) {
        insertTodoTag.bind(newTodoId, checkedTagId);
        briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
      }
    }
    return newTodoId;
  }

  public long insertTag(String addedTag) {
    long now = System.currentTimeMillis();
    insertTag.bind(addedTag, now);
    return briteDatabase.executeInsert(insertTag.table, insertTag.program);
  }

  /***************************** update **********************************************/
  public void updateTodoString(String addedTodoStr, String addedTagStr, long todoId, List<Long> checkedTagIdList) {

    updateTodoString.bind(addedTodoStr, todoId);
    int id = briteDatabase.executeUpdateDelete(updateTodoString.table, updateTodoString.program);

    // tag も新規作成されていた場合、挿入
    if (addedTagStr.length() != 0) {
      // タグテーブルにInsert
      long newTagId = insertTag(addedTagStr);

      if(checkedTagIdList != null){
        // todoTag の再挿入時に追加させる
        checkedTagIdList.add(newTagId);
      } else{
        // TodoTag テーブルに挿入
        insertTodoTag.bind(todoId, newTagId);
        long todoTagId = briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
      }
    }

    // update TodoTag table by drop and re-insert
    if (checkedTagIdList != null) {

      deleteTodoByTodoId.bind(todoId);
      long deletedRows = briteDatabase.executeUpdateDelete(deleteTodoByTodoId.table, deleteTodoByTodoId.program);
      Timber.d("DbCrudHelper:updateTodoString: %s rows were deleted for todo_id %s", deletedRows, todoId);
      for (long tagId : checkedTagIdList) {
        insertTodoTag.bind(todoId, tagId);
        long todoTagId = briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
        Timber.d("DbCrudHelper:updateTodoString: Id_%s: inserted for todo id %s", todoTagId, todoId);
      }
    }
  }

  public void updateTodoIsChecked(boolean isTodoChecked, long todoId) {
    updateTodoIsChecked.bind(isTodoChecked, todoId);
    briteDatabase.executeUpdateDelete(updateTodoIsChecked.table, updateTodoIsChecked.program);
  }

  /***************************** delete **********************************************/

  public int deleteTodo(long todoId) {
    deleteTodoByTodoId.bind(todoId);
    briteDatabase.executeUpdateDelete(deleteTodoByTodoId.table, deleteTodoByTodoId.program);
    deleteTodo.bind(todoId);
    return briteDatabase.executeUpdateDelete(deleteTodo.table, deleteTodo.program);
  }
}
