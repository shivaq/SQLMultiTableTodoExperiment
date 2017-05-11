package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main;

import java.util.List;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseMvpView;

public interface MainMvpView extends BaseMvpView {

  void setTodo(List<Todo> todoList);
}
