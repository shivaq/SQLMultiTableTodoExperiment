package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.RvItemDecorator;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ApplicationContext;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.component.ActivityComponent;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add.AddEditActivity;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseActivity;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main.MainRvAdapter.RvCallback;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.taglist.TagListActivity;

public class MainActivity extends BaseActivity
    implements MainMvpView, RvCallback {

  // Inject Presenter from an object graph
  @Inject
  MainPresenter mainPresenter;
  @Inject
  @ApplicationContext
  Context context;
  @Inject
  MainRvAdapter mainRvAdapter;

  // RecyclerViews
  @BindView(R.id.recycler_subject)
  RecyclerView rvMain;


  public MainActivity() {
  }

  private ActivityComponent mActivityComponent;
  private LayoutManager rvLayoutManager;
  private Parcelable rvLayoutState;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    // inject to retrieve components from object graph
    getActivityComponent().inject(this);

    mainPresenter.onAttachMvpView(this);
//    mainPresenter.loadTodo();

    rvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);
    rvMain.setLayoutManager(rvLayoutManager);
    rvMain.setHasFixedSize(true);
    rvMain.addItemDecoration(new RvItemDecorator(this));
    rvMain.setAdapter(mainRvAdapter);
    mainRvAdapter.registerRvCallback(this);
    Timber.d("MainActivity:onCreate: ");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Timber.d("MainActivity:onResume: ");
    mainPresenter.loadTodo();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mainPresenter.onDetachMvpView();
  }

  @Override
  public void setTodo(List<Todo> todoList) {
    mainRvAdapter.setTodoList(todoList);
    // load したリストを Rv の Adapter にセットし、
    // その Adapter を Rv に再セットすることで、load した結果がスクリーンに反映される
    rvMain.setAdapter(mainRvAdapter);
  }

  /********************* OnClick **********************************************/
  @OnClick(R.id.tv_link_tag_list)
  public void onTagListLinkClicked() {
    Intent intent = new Intent(this, TagListActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.fab_add_todo)
  void onAddFabClicked() {
    Intent intent = new Intent(this, AddEditActivity.class);
    startActivity(intent);
  }

  /********************** implement RvCallback **********************/
  @Override
  public void onRvTodoCbClicked(boolean isChecked, long id) {
    // CheckBox クリック →スクロールポジションを保存
    rvLayoutState = rvLayoutManager.onSaveInstanceState();
    mainPresenter.updateTodoIsChecked(isChecked, id);
  }

  @Override
  public void onRvRefreshed() {
    // スクロールポジションを復元
    rvLayoutManager.onRestoreInstanceState(rvLayoutState);
    rvMain.setLayoutManager(rvLayoutManager);
  }

  @Override
  public void onRvItemClicked(Todo clickedTodo) {
    Intent intent = new Intent(this, AddEditActivity.class);
    intent.putExtra(AddEditActivity.TODO_EXTRA, clickedTodo);
    startActivity(intent);
  }

}
