package yasuaki.kyoto.com.sqlmultitabletodoexp.data.model;


import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import com.squareup.sqldelight.RowMapper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.TagModel;

@AutoValue
public abstract class Tag implements TagModel, Parcelable{

  public static final Factory<Tag> TAG_FACTORY = new Factory<>(AutoValue_Tag::new);

  // RowMapper →select結果をオブジェクトにして返却
  public static final RowMapper<Tag> TAG_ROW_MAPPER = TAG_FACTORY.select_allMapper();

  public static final RowMapper<Tag> TAG_BY_ID_ROW_MAPPER = TAG_FACTORY.select_tag_by_idMapper();

}
