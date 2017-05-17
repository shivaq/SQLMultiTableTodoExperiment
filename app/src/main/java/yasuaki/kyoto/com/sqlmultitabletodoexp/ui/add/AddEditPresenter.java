package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.DataManager;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BasePresenter;

public class AddEditPresenter implements BasePresenter<AddEditMvpView> {

  private final DataManager dataManager;
  private AddEditMvpView addEditMvpView;
  private final CompositeSubscription subscription;

  @Inject
  public AddEditPresenter(DataManager dataManager) {
    this.dataManager = dataManager;
    subscription = new CompositeSubscription();
  }

  @Override
  public void onAttachMvpView(AddEditMvpView mvpView) {
    addEditMvpView = mvpView;
  }

  @Override
  public void onDetachMvpView() {
    addEditMvpView = null;
    subscription.unsubscribe();
  }

  /*********************** save update delete ****************************/
  public void saveTodo(String todo, String addedTag, List<Long> checkedTagList) {
    dataManager.insertTodo(todo, addedTag, checkedTagList);
    addEditMvpView.closeActivity();
  }

  public void updateTodo(String addedTodo, String addedTagStr, long todoId,
      List<Long> checkedTagIdList) {
    dataManager.updateTodo(addedTodo, addedTagStr, todoId, checkedTagIdList);
    Timber.d("AddEditPresenter:updateTodo: updated");
    addEditMvpView.closeActivity();
  }

  public int deleteTodo(long todoId) {
    int deletedRows = dataManager.deleteTodo(todoId);
    addEditMvpView.closeActivity();
    return deletedRows;
  }

  /********************************** load ************************************/
  public void loadPlainTag() {
    Timber.d("AddEditPresenter:loadPlainTag: ");
    subscription.add(
        dataManager.loadTag()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Tag>>() {

              @Override
              public void onCompleted() {

              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onNext(List<Tag> tags) {
                addEditMvpView.setPlainTagList(tags);
              }
            })
    );
  }

  public void loadTodoTag(long todoId) {
    subscription.add(
        dataManager.loadTodoTag(todoId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<List<Long>>() {

              @Override
              public void onCompleted() {

              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onNext(List<Long> tagList) {
                Timber.d("AddEditPresenter:loadTodoTag>onNext: ");
                addEditMvpView.setTodoWithCheckedTag(tagList);
              }
            })
    );
  }
}
