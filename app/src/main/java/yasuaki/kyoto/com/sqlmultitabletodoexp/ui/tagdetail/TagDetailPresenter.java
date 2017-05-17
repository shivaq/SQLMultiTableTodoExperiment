package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail;

import javax.inject.Inject;
import rx.subscriptions.CompositeSubscription;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.DataManager;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BasePresenter;

/**
 * Created by Yasuaki on 2017/05/17.
 */

class TagDetailPresenter implements BasePresenter<TagDetailMvpView> {

  private final DataManager dataManager;
  private TagDetailMvpView tagDetailMvpView;
  private final CompositeSubscription subscription;

  @Inject
  public TagDetailPresenter(DataManager dataManager) {
    this.dataManager = dataManager;
    subscription = new CompositeSubscription();
  }

  @Override
  public void onAttachMvpView(TagDetailMvpView mvpView) {
    tagDetailMvpView = mvpView;
  }

  @Override
  public void onDetachMvpView() {
    tagDetailMvpView = null;
    subscription.unsubscribe();
  }
  /*********************** save update delete ****************************/

  /********************************** load ************************************/

}
