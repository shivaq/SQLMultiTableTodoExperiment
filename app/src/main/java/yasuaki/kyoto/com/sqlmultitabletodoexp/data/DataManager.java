package yasuaki.kyoto.com.sqlmultitabletodoexp.data;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
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
    return dbCrudHelper.loadTodo();
  }

  public Observable<List<Tag>> loadTag() {
    Timber.d("DataManager:loadPlainTag: ");
    return dbCrudHelper.loadTag();
  }

  public Observable<List<Long>> loadTagForTodo(long todoId) {
    return dbCrudHelper.loadTagForTodo(todoId);
  }

  public Observable<List<TodoForTag>> loadTodoForTag(long tagId) {
    return dbCrudHelper.loadTodoForTag(tagId);
  }

  public Observable<List<TagWithTodoCounts>> loadTagWithTodoCounts() {
    return dbCrudHelper.loadTagWithTodoCounts();
  }

  public void insertTodo(String todo, String addedTag, List<Long> checkedTagList) {
    dbCrudHelper.insertTodo(todo, addedTag, checkedTagList);
  }

  public void updateTodoIsChecked(boolean isChecked, long id) {
    dbCrudHelper.updateTodoIsChecked(isChecked, id);
  }

  public void updateTodo(String addedTodo, boolean isTodoChanged, String addedTagStr,
      long todoId, List<Long> checkedTagIdList) {
    dbCrudHelper.updateTodoString(addedTodo, isTodoChanged, addedTagStr, todoId, checkedTagIdList);
  }

  public int deleteTodo(long todoId) {
    return dbCrudHelper.deleteTodo(todoId);
  }


}