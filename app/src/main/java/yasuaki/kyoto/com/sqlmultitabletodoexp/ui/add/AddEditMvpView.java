package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import java.util.List;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseMvpView;

/**
 * Created by Yasuaki on 2017/05/10.
 */

public interface AddEditMvpView extends BaseMvpView{

  void closeActivity();

  void setTag(List<Tag> tags);

  void setTodo(List<Long> tagIdList);
}
