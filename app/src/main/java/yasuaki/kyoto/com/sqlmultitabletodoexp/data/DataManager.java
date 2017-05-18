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
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag.TagWithTodoCounts;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo.TodoForTag;

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
        .map(new Func1<Cursor, List<Todo>>() {
          @Override
          public List<Todo> call(Cursor cursor) {
            List<Todo> todoList = new ArrayList<>();
            try {
              if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                  // cursor を todoに map する
                  Todo allTodo = Todo.SELECT_ALL_MAPPER.map(cursor);
                  todoList.add(allTodo);
                  cursor.moveToNext();
                }
              }
            } finally {
              cursor.close();
            }
            return todoList;
          }
        });
  }

  public Observable<List<Tag>> loadTag() {
    Timber.d("DataManager:loadPlainTag: ");
    return dbCrudHelper.loadTag()
        .map(new Func1<Cursor, List<Tag>>() {

          @Override
          public List<Tag> call(Cursor cursor) {
            List<Tag> tagList = new ArrayList<>();
            try {
              if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                  Tag tag = Tag.TAG_ROW_MAPPER.map(cursor);
                  tagList.add(tag);
                  cursor.moveToNext();
                }
              }
            } finally {
              cursor.close();
            }
            return tagList;
          }
        });
  }

  public Observable<List<Long>> loadTagForTodo(long todoId) {
    return dbCrudHelper.loadTagForTodo(todoId)
        .map(new Func1<Cursor, List<Long>>() {

          @Override
          public List<Long> call(Cursor cursor) {
            List<Long> tagIdForTodoList = new ArrayList();
            try {
              if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                  long tagId = Tag.TAG_ID_FOR_TODO_MAPPER.map(cursor);
                  tagIdForTodoList.add(tagId);

                  cursor.moveToNext();
                }
              }
            } finally {
              cursor.close();
            }
            return tagIdForTodoList;
          }
        });
  }

  public Observable<List<TodoForTag>> loadTodoForTag(long tagId) {
    return dbCrudHelper.loadTodoForTag(tagId)
        .map(new Func1<Cursor, List<TodoForTag>>(){

          @Override
          public List<TodoForTag> call(Cursor cursor) {
            List<TodoForTag> todoForTagList = new ArrayList();
            try{
              if(cursor.moveToFirst()){
                for(int i = 0; i < cursor.getCount(); i++){

                  TodoForTag todoForTag = Todo.SELECT_TODO_FOR_TAG_MAPPER.map(cursor);
                  todoForTagList.add(todoForTag);
                  cursor.moveToNext();
                }
              }
            } finally{
              cursor.close();
            }
            return todoForTagList;
          }
        });
  }

  public Observable<List<TagWithTodoCounts>> loadTagWithTodoCounts() {
    return dbCrudHelper.loadTagWithTodoCounts()
        .map(new Func1<Cursor, List<TagWithTodoCounts>>(){

          @Override
          public List<TagWithTodoCounts> call(Cursor cursor) {
            List<TagWithTodoCounts> tagWithTodoCountsList = new ArrayList();
            try{
              if(cursor.moveToFirst()){
                for(int i = 0; i < cursor.getCount(); i++){
                  TagWithTodoCounts tagWithTodoCounts = Tag.TAGWITHTODOCOUNTS_ROW_MAPPER.map(cursor);
                  tagWithTodoCountsList.add(tagWithTodoCounts);
                  cursor.moveToNext();
                }
              }
            }finally{
              cursor.close();
            }
            return tagWithTodoCountsList;
          }
        });
  }

  public void insertTodo(String todo, String addedTag, List<Long> checkedTagList) {
    dbCrudHelper.insertTodo(todo, addedTag, checkedTagList);
  }

  public void updateTodoIsChecked(boolean isChecked, long id) {
    dbCrudHelper.updateTodoIsChecked(isChecked, id);
  }

  public void updateTodo(String addedTodo, boolean isTodoChanged,String addedTagStr, long todoId,
      List<Long> checkedTagIdList) {
    dbCrudHelper.updateTodoString(addedTodo, isTodoChanged,addedTagStr, todoId, checkedTagIdList);
  }

  public int deleteTodo(long todoId) {
    return dbCrudHelper.deleteTodo(todoId);
  }



}