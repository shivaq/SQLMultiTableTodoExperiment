package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main;

import java.util.List;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseMvpView;

public interface MainMvpView extends BaseMvpView {

  void setTodo(List<String> strings);
}
