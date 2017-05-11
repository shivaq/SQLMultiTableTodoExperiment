package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ApplicationContext;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.component.ActivityComponent;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add.AddEditActivity;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainMvpView {

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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    // inject to retrieve components from object graph
    getActivityComponent().inject(this);

    mainPresenter.onAttachMvpView(this);
    mainPresenter.loadTodo();

    LinearLayoutManager mainTodoLM = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);
    rvMain.setLayoutManager(mainTodoLM);
    rvMain.setHasFixedSize(true);
    rvMain.setAdapter(mainRvAdapter);
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

  @OnClick(R.id.fab_add_todo)
  void onAddFabClicked(){
    Intent intent = new Intent(this, AddEditActivity.class);
    startActivity(intent);
  }
}
