package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagedit;

import java.util.List;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag.TagWithTodoCounts;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseMvpView;

/**
 * Created by Yasuaki on 2017/05/16.
 */

public interface TagListMvpView extends BaseMvpView{

  void setTagList(List<Tag> tags);

  void setTagWithTodoCounts(List<TagWithTodoCounts> tagWithTodoCounts);
}
