package yasuaki.kyoto.com.sqlmultitabletodoexp.data.model;

import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TodoModel;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.DateAdapter;

@AutoValue
public abstract class Todo implements TodoModel, Parcelable {

  private static final DateAdapter DATE_ADAPTER = new DateAdapter();

  // SQL の INSERT ステートメントに対応する
  public static final Factory<Todo> TODO_FACTORY = new Factory<>(AutoValue_Todo::new);

  // RowMapper →select結果をオブジェクトにして返却
  public static final RowMapper<Todo> SELECT_ALL_MAPPER =
      TODO_FACTORY.select_allMapper();

  public static final RowMapper<SelectTagForTodo> SELECT_BY_TAG_MAPPER =
      TODO_FACTORY.select_tag_for_todoMapper((AutoValue_Todo_SelectTagForTodo::new), Tag.TAG_FACTORY);

  @AutoValue
  public static abstract class SelectTagForTodo implements Select_tag_for_todoModel<Todo, Tag>{

  }
}
