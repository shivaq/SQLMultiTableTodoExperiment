package yasuaki.kyoto.com.thesimplestdaggermvp.ui;

import java.util.List;
import yasuaki.kyoto.com.thesimplestdaggermvp.ui.base.BaseMvpView;

public interface MainMvpView extends BaseMvpView {

  void setTodo(List<String> strings);
}
