package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import javax.inject.Inject;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ApplicationContext;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseActivity;

/**
 * Created by Yasuaki on 2017/05/10.
 */

public class AddEditActivity extends BaseActivity implements AddEditMvpView {

  @Inject
  AddEditPresenter addEditPresenter;
  @Inject
  @ApplicationContext
  Context context;

  @BindView(R.id.editTodo)
  EditText editTodo;

  public static final int REQUEST_ADD_TODO = 1;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add);
    ButterKnife.bind(this);
    getActivityComponent().inject(this);
    addEditPresenter.onAttachMvpView(this);

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    addEditPresenter.onDetachMvpView();
  }

  /*********************** mvp implementation **********************/


  /*********************** onClick ************************/
  @OnClick(R.id.fab_todo_edit_ok)
  void onOkClicked(){
    String addedTodo = editTodo.getText().toString();
    addEditPresenter.saveTodo(addedTodo);
    Toast.makeText(context, addedTodo, Toast.LENGTH_SHORT).show();
  }
}