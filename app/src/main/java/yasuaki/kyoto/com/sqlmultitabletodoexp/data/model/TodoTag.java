package yasuaki.kyoto.com.sqlmultitabletodoexp.data.model;

import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.Todo_TagModel;

@AutoValue
public abstract class TodoTag implements Todo_TagModel{

  public static final Factory<TodoTag> TODO_TAG_FACTORY = new Factory<>(AutoValue_TodoTag::new);

  public static final RowMapper<TodoTag> TAG_FOR_TODO_ROW_MAPPER =
      TODO_TAG_FACTORY.select_tags_for_todoidMapper();

  public static final RowMapper<TodoTag> TODO_FOR_TAG_ROW_MAPPER =
      TODO_TAG_FACTORY.select_todos_for_tagidMapper();
}
