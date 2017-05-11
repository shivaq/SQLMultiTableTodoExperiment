package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main;

import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.DataManager;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.PerActivity;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BasePresenter;

@PerActivity//Use this scope annotation to make presenter configure persistent
public class MainPresenter implements BasePresenter<MainMvpView> {

    private final DataManager dataManager;
    private MainMvpView mainMvpView;
    private final CompositeSubscription subscription;

    // A presenter is integrated into object graph.
    // Then a presenter could summon instances and be summoned from components.
    @Inject
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        subscription = new CompositeSubscription();
    }

    @Override
    public void onAttachMvpView(MainMvpView mvpView) {
        mainMvpView = mvpView;
    }

    @Override
    public void onDetachMvpView() {
        mainMvpView = null;
        subscription.unsubscribe();
    }

    public boolean isViewAttached() {
        return mainMvpView != null;
    }

  public void loadTodo() {
    Timber.d("MainPresenter:loadTodo: ");
      subscription.add(dataManager.loadTodo()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Subscriber<List<Todo>>(){

              @Override
              public void onCompleted() {

              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onNext(List<Todo> todoList) {
                  mainMvpView.setTodo(todoList);
              }
          })
      );
//      mainMvpView.setTodo(Utility.dummyTodoCreater());
      // TODO:fetch data from DB
  }
}