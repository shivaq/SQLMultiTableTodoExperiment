package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base;

/**
 * Created by Yasuaki on 2017/04/13.
 */
//@PerActivity
public interface BasePresenter<V extends BaseMvpView> {

    void onAttachMvpView(V mvpView);

    void onDetachMvpView();
}
