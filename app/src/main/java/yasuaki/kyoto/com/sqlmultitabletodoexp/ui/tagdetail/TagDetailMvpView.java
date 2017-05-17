package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail;

import java.util.List;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo.TodoForTag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseMvpView;

/**
 * Created by Yasuaki on 2017/05/17.
 */

interface TagDetailMvpView extends BaseMvpView {

  void setTodoForTagList(List<TodoForTag> tagList);
}
