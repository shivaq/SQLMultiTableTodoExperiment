package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import static yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag.TAG_FACTORY;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqlbrite.SqlBrite.Builder;
import com.squareup.sqlbrite.SqlBrite.Logger;
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
  private Todo.Update_isChecked updateIsChecked;
  private Todo.Update_todoString updateTodoString;
  private Todo.Delete_todo deleteTodo;

  private Tag.Insert_tag insertTag;

  private TodoTag.Insert_todo_tag insertTodoTag;
  private TodoTag.Delete_todo_by_todo_id deleteTodoByTodoId;

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
    SQLiteDatabase sqLiteWritableDatabase = briteDatabase.getWritableDatabase();
    // SqlDelight で生成された コンパイル済みSQLステートメントをインスタンス化
    insertTodo = new Insert_todo(sqLiteWritableDatabase);
    updateIsChecked = new Update_isChecked(sqLiteWritableDatabase);
    updateTodoString = new Update_todoString(sqLiteWritableDatabase);
    deleteTodo = new Delete_todo(sqLiteWritableDatabase);

    insertTag = new Insert_tag(sqLiteWritableDatabase);
    insertTodoTag = new Insert_todo_tag(sqLiteWritableDatabase);
    deleteTodoByTodoId = new Delete_todo_by_todo_id(sqLiteWritableDatabase);
  }

  public Observable<Cursor> loadTodo() {
    // SqlDelight が生成した SQL ステートメント
    SqlDelightStatement selectAllQuery = Todo.TODO_FACTORY.select_all();

    // 返り値は QueryObservable
    return briteDatabase.createQuery(
        Todo.TABLE_NAME,
        selectAllQuery.statement,
        selectAllQuery.args)
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


  public long insertTodo(String todo, String addedTag, List<Long> checkedTagList) {
    Timber.d("DbCrudHelper:insertTodo: ");
    long now = System.currentTimeMillis();
    long tagId;

    // Todoテーブル にInsert
    insertTodo.bind(todo, false, now, now);
    long todoId = briteDatabase.executeInsert(insertTodo.table, insertTodo.program);

    // 新規挿入時に、タグも追加されているかどうか
    if (addedTag.length() == 0) {
      tagId = 0;
    } else {
      // タグテーブルにInsert
      tagId = insertTag(addedTag);
      // TodoTag テーブルにも挿入
      insertTodoTag.bind(todoId, tagId);
      long todoTagId = briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
    }

    if (checkedTagList != null) {
      for (long checkedTagId : checkedTagList) {
        insertTodoTag.bind(todoId, checkedTagId);
        briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
      }
    }
    return todoId;
  }

  public long insertTag(String addedTag) {
    long now = System.currentTimeMillis();
    insertTag.bind(addedTag, now);
    return briteDatabase.executeInsert(insertTag.table, insertTag.program);
  }

  public void updateTodoIsChecked(boolean isChecked, long id) {

    updateIsChecked.bind(isChecked, id);
    briteDatabase.executeUpdateDelete(updateIsChecked.table, updateIsChecked.program);
  }

  public void updateTodoString(String addedTodo, long todoId) {
    updateTodoString.bind(addedTodo, todoId);
    int id = briteDatabase.executeUpdateDelete(updateTodoString.table, updateTodoString.program);
  }

  public int deleteTodo(long todoId) {
    deleteTodoByTodoId.bind(todoId);
    briteDatabase.executeUpdateDelete(deleteTodoByTodoId.table, deleteTodoByTodoId.program);
    deleteTodo.bind(todoId);
    return briteDatabase.executeUpdateDelete(deleteTodo.table, deleteTodo.program);
  }


}
