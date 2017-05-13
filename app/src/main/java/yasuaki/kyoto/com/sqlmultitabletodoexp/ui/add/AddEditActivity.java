package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import javax.inject.Inject;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
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

  private Todo todoFromMain;
  private boolean isEditMode;

  public static final String TODO_EXTRA = "yasuaki.kyoto.com.sqlmultitabletodoexp.TODO_EXTRA";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add);
    ButterKnife.bind(this);
    getActivityComponent().inject(this);
    addEditPresenter.onAttachMvpView(this);



    Intent intentFromMain = getIntent();
    todoFromMain = intentFromMain.getParcelableExtra(TODO_EXTRA);
    if (todoFromMain == null) {
      setTitle(getString(R.string.title_act_add));
      isEditMode = false;
    } else {
      setTitle(getString(R.string.title_act_edit));
      isEditMode = true;
      setTodo();
    }


  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    addEditPresenter.onDetachMvpView();
  }

  private void setTodo(){
    editTodo.setText(todoFromMain.todo());
  }

  /*********************** mvp implementation **********************/
  @Override
  public void closeActivity() {
    this.finish();
  }
  /*********************** onClick ************************/
  @OnClick(R.id.fab_todo_edit_ok)
  void onOkClicked(){
    String addedTodo = editTodo.getText().toString();
    if (addedTodo.length() == 0) {
      closeActivity();
      return;
    }
    if (isEditMode) {
      long todoId = todoFromMain._id();
      Timber.d("AddEditActivity:onOkClicked: editMode");
      addEditPresenter.updateTodo(addedTodo, todoId);
    } else {
      addEditPresenter.saveTodo(addedTodo);
    }
  }
}
