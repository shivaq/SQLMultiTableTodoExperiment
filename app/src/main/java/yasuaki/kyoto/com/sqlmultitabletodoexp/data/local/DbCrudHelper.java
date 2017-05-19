package yasuaki.kyoto.com.sqlmultitabletodoexp.data.local;

import static yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag.TAG_FACTORY;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.squareup.sqlbrite.SqlBrite.Builder;
import com.squareup.sqlbrite.SqlBrite.Query;
import com.squareup.sqldelight.SqlDelightStatement;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
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

  // Parse cursor here version
  public Observable<List<Todo>> loadTodo() {
    // SqlDelight が生成した SQL ステートメント
    SqlDelightStatement selectAllTodoQuery = Todo.TODO_FACTORY.select_all();

    // query -> cursor -> todoList
    return briteDatabase.createQuery(
        Todo.TABLE_NAME,
        selectAllTodoQuery.statement,
        selectAllTodoQuery.args)
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        })
        .map(new Func1<Cursor, List<Todo>>() {

          @Override
          public List<Todo> call(Cursor cursor) {
            List<Todo> todoList = new ArrayList<>();
            if (cursor.moveToFirst()) {
              while (cursor.moveToNext()) {
                Todo allTodo = Todo.SELECT_ALL_MAPPER.map(cursor);
                todoList.add(allTodo);
              }
            }
            return todoList;
          }
        });
  }


  // Parse cursor in DataManager version
  public Observable<Cursor> loadTag() {
    SqlDelightStatement selectAllTagQuery = TAG_FACTORY.select_all();
    return briteDatabase.createQuery(
        Tag.TABLE_NAME,
        selectAllTagQuery.statement,
        selectAllTagQuery.args)
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        });
  }

  public Observable<Cursor> loadTagForTodo(long todoId) {
    SqlDelightStatement selectTagsForTodoId =
        Tag.TAG_FACTORY.select_tag_for_todo_id(todoId);
    return briteDatabase.createQuery(
        Tag.TABLE_NAME, selectTagsForTodoId.statement, selectTagsForTodoId.args)
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        });
  }

  public Observable<Cursor> loadTodoForTag(long tagId) {
    SqlDelightStatement selectTodosForTag =
        Todo.TODO_FACTORY.select_todo_for_tag(tagId);
    return briteDatabase.createQuery(
        Todo.TABLE_NAME,
        selectTodosForTag.statement,
        selectTodosForTag.args)
        .map(new Func1<Query, Cursor>() {
          @Override
          public Cursor call(Query query) {
            return query.run();
          }
        });
  }

  public Observable<Cursor> loadTagWithTodoCounts() {
    SqlDelightStatement selectAllTagWithCounts = TAG_FACTORY.select_all_with_todo_counts();

    // COUNT 順 に対応する 手打ちステートメント
    String selectAllTagWithCountsQuery = "SELECT * , COUNT(todo_tag._id) AS tag_count"
        + " FROM tag LEFT OUTER JOIN todo_tag ON tag._id = todo_tag.tag_id"
        + " GROUP BY tag"
        + " ORDER BY tag_count ASC;";

    return briteDatabase.createQuery(
        Tag.TABLE_NAME, selectAllTagWithCounts.statement, selectAllTagWithCounts.args)
//        Tag.TABLE_NAME, selectAllTagWithCountsQuery, selectAllTagWithCounts.args)
        .map(new Func1<Query, Cursor>() {
               @Override
               public Cursor call(Query query) {
                 return query.run();
               }
             }
        );
  }

  /***************************** insert **********************************************/

  public void insertTodo(String todoStr, String addedTagStr, List<Long> checkedTagIdList) {
    Timber.d("DbCrudHelper:insertTodo: ");
    long now = System.currentTimeMillis();

    // Todoテーブル にInsert
    insertTodo.bind(todoStr, false, now, now);
    long newTodoId = briteDatabase.executeInsert(insertTodo.table, insertTodo.program);
    Timber.d("DbCrudHelper:insertTodo: Id_%s: %s is inserted into todo table", newTodoId,
        todoStr);
//
//    // 新規挿入時に、タグも追加されているかどうか
//    if (addedTagStr.length() != 0) {
//      // タグテーブルにInsert
//      long newTagId = insertTag(addedTagStr);
//      Timber.d("DbCrudHelper:insertTodo: ID_%s: %s is inserted into tag table", newTagId,
//          addedTagStr);
//      // TodoTag テーブルにも挿入
//      insertTodoTag.bind(newTodoId, newTagId);
//      long todoTagId = briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
//      Timber.d("DbCrudHelper:insertTodo: ID_%s is inserted into TodoTag table", todoTagId);
//    }

    addNewTag(addedTagStr, checkedTagIdList, newTodoId);

//    if (checkedTagIdList != null) {
//      for (long checkedTagId : checkedTagIdList) {
//        insertTodoTag.bind(newTodoId, checkedTagId);
//        briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
//      }
//    }
  }

  public long insertTag(String addedTag) {
    long now = System.currentTimeMillis();
    insertTag.bind(addedTag, now);
    return briteDatabase.executeInsert(insertTag.table, insertTag.program);
  }

  /***************************** update **********************************************/
  public void updateTodoString(String addedTodoStr, boolean isTodoChanged,
      String addedTagStr,
      long todoId,
      List<Long> checkedTagIdList) {

    // TodoTable をアップデート
    if (isTodoChanged) {
      updateTodoString.bind(addedTodoStr, todoId);
      int updatedTodoId = briteDatabase
          .executeUpdateDelete(updateTodoString.table, updateTodoString.program);
      Timber.d("DbCrudHelper:updateTodoString: todoId %s is updated", updatedTodoId);
    }

    // 追加 Tag String が空でなければ更新
    addNewTag(addedTagStr, checkedTagIdList, todoId);
  }

  private void addNewTag(String addedTagStr, List<Long> checkedTagIdList, long todoId) {

    // 新規追加 Tag String が空でなければ 新規 Tag 挿入
    if (addedTagStr.length() != 0) {
      // タグテーブルにInsert
      long newTagId = insertTag(addedTagStr);

      Timber.d("DbCrudHelper:addNewTag: id %s %s is inserted to tag table", newTagId,
          addedTagStr);

      if (checkedTagIdList == null) {
        // 他に追加タグがないので、ここで リンクテーブルを更新
        insertTodoTag.bind(todoId, newTagId);
        long todoTagId = briteDatabase
            .executeInsert(insertTodoTag.table, insertTodoTag.program);
        Timber.d("DbCrudHelper:addNewTag: %s rows are inserted", todoTagId);
      } else {
        if (checkedTagIdList == null) {
          checkedTagIdList = new ArrayList();
        }
        // 他にも追加タグがあるので、他のタグと一緒にあとでリンクテーブル更新
        checkedTagIdList.add(newTagId);
      }
    }
    if (checkedTagIdList != null) {
      updateLinkTable(todoId, checkedTagIdList);
    }

  }

  // update TodoTag table by drop and re-insert
  private void updateLinkTable(long todoId, List<Long> checkedTagIdList) {
    int insertCount = 0;
    deleteTodoByTodoId.bind(todoId);
    long deletedRows = briteDatabase
        .executeUpdateDelete(deleteTodoByTodoId.table, deleteTodoByTodoId.program);
    Timber.d("DbCrudHelper:updateLinkTable: %s rows are deleted", deletedRows);

    if (checkedTagIdList != null) {

      for (long tagId : checkedTagIdList) {
        insertTodoTag.bind(todoId, tagId);
        briteDatabase.executeInsert(insertTodoTag.table, insertTodoTag.program);
        insertCount++;
      }
      Timber.d("DbCrudHelper:updateTodoString: %s rows are inserted to linkTable",
          insertCount);
    }
  }

  public void updateTodoIsChecked(boolean isTodoChecked, long todoId) {
    updateTodoIsChecked.bind(isTodoChecked, todoId);
    briteDatabase
        .executeUpdateDelete(updateTodoIsChecked.table, updateTodoIsChecked.program);
  }

  /***************************** delete **********************************************/

  public int deleteTodo(long todoId) {
    deleteTodoByTodoId.bind(todoId);
    long deletedRows = briteDatabase
        .executeUpdateDelete(deleteTodoByTodoId.table, deleteTodoByTodoId.program);
    deleteTodo.bind(todoId);
    return briteDatabase.executeUpdateDelete(deleteTodo.table, deleteTodo.program);
  }

  // すべてのテーブルを delete する。
  public Observable<Void> clearTables() {
    return Observable.create(new OnSubscribe<Void>() {
      @Override
      public void call(Subscriber<? super Void> subscriber) {
        BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
        try {
          // sqlite_master は、すべての Sqlite DB が持つ、DB スキーマを定義した特別なテーブル
          Cursor cursor = briteDatabase
              .query("SELECT name FROM sqlite_master WHERE type='table'");
          while (cursor.moveToNext()) {
            // $1 テーブル名が格納された列からテーブル名をことごとく取得 $2:Where 句 が null →全部削除となるのかな。
            briteDatabase.delete(cursor.getString(cursor.getColumnIndex("name")), null);
          }
          cursor.close();
          transaction.markSuccessful();
          subscriber.onCompleted();
        } finally {
          transaction.end();
        }
      }
    });
  }
}
