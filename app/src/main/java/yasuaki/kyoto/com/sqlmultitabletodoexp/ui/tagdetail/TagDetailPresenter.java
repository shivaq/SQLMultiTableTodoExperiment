package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail;

import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.DataManager;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo.TodoForTag;
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

  public void loadTodoForTag(long tagId) {
    subscription.add(
        dataManager.loadTodoForTag(tagId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<TodoForTag>>() {

              @Override
              public void onCompleted() {

              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onNext(List<TodoForTag> todoForTagList) {
                tagDetailMvpView.setTodoForTagList(todoForTagList);
              }
            })
    );

  }
}
