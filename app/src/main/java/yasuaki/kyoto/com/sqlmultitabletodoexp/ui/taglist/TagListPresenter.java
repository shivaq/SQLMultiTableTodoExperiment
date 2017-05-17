package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.taglist;

import java.util.List;
import javax.inject.Inject;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.DataManager;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag.TagWithTodoCounts;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BasePresenter;

/**
 * Created by Yasuaki on 2017/05/16.
 */

public class TagListPresenter implements BasePresenter<TagListMvpView> {

  private final DataManager dataManager;
  private TagListMvpView tagListMvpView;
  private final CompositeSubscription subscription;

  @Inject
  public TagListPresenter(DataManager dataManager){
    this.dataManager = dataManager;
    subscription = new CompositeSubscription();
  }

  @Override
  public void onAttachMvpView(TagListMvpView mvpView) {
    tagListMvpView = mvpView;
  }

  @Override
  public void onDetachMvpView() {
    tagListMvpView = null;
    subscription.unsubscribe();
  }

  public void loadTagList() {
    subscription.add(
        dataManager.loadTag()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<Tag>>(){

          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(List<Tag> tags) {
            tagListMvpView.setTagList(tags);
          }
        })
    );
  }

  public void loadTagWithTodoCounts() {
    subscription.add(
        dataManager.loadTagWithTodoCounts()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<List<TagWithTodoCounts>>(){

          @Override
          public void onCompleted() {

          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onNext(List<TagWithTodoCounts> tagWithTodoCounts) {
            tagListMvpView.setTagWithTodoCounts(tagWithTodoCounts);
          }
        })
    );

  }
}
