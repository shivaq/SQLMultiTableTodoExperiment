package yasuaki.kyoto.com.sqlmultitabletodoexp.data.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TodoModel;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.DateAdapter;

@AutoValue
public abstract class Todo implements TodoModel, Parcelable {

  public static Todo create(long _id, String todoStr,long registerredDate, long updatedDate, boolean isChecked ){
    return new AutoValue_Todo(_id, todoStr, registerredDate, updatedDate, isChecked);
  }

  private static final DateAdapter DATE_ADAPTER = new DateAdapter();

  // SQL の INSERT ステートメントに対応する
  public static final Factory<Todo> TODO_FACTORY = new Factory<>(AutoValue_Todo::new);

  // RowMapper →select結果をオブジェクトにして返却
  public static final RowMapper<Todo> SELECT_ALL_MAPPER =
      TODO_FACTORY.select_allMapper();

  public static final RowMapper<TodoForTag> SELECT_TODO_FOR_TAG_MAPPER =
      TODO_FACTORY
          .select_todo_for_tagMapper((AutoValue_Todo_TodoForTag::new), TodoTag.TODO_TAG_FACTORY);

  public static final RowMapper<Todo> SELECT_TODO_BY_ID_MAPPER =
      TODO_FACTORY.select_todo_by_idMapper();

  @AutoValue
  public static abstract class TodoForTag implements Select_todo_for_tagModel<Todo, TodoTag> {

  }
}
