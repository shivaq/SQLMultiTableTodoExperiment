package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.RvItemDecorator;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseActivity;

/**
 * Created by Yasuaki on 2017/05/10.
 */

public class AddEditActivity extends BaseActivity implements AddEditMvpView {

  @Inject
  AddEditPresenter addEditPresenter;
  @Inject
  RvAdapterForTodoTag rvAdapterForTodoTag;

  @BindView(R.id.editTodo)
  EditText editTodo;
  @BindView(R.id.editTag)
  EditText editTag;
  @BindView(R.id.rv_tag_add_act)
  RecyclerView rvTag;

  private Todo todoFromMain;
  private boolean isEditMode;
  private boolean isDataModified;
  private String fromMainTodoString;
  private LayoutManager rvLayoutManager;
  private List<Tag> plainTagList;

  public static final String TODO_EXTRA = "yasuaki.kyoto.com.sqlmultitabletodoexp.TODO_EXTRA";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add);
    ButterKnife.bind(this);
    getActivityComponent().inject(this);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);

    // 入力欄をタッチしたら、isDataModified を true にしているだけ
    editTodo.setOnTouchListener(touchListener);
    editTag.setOnTouchListener(touchListener);

    addEditPresenter.onAttachMvpView(this);

    // Edit モードか 新規追加モードかをチェック
    Intent intentFromMain = getIntent();
    todoFromMain = intentFromMain.getParcelableExtra(TODO_EXTRA);

    if (todoFromMain == null) {
      setTitle(getString(R.string.title_act_add));
      isEditMode = false;
      // menu を無効化
      invalidateOptionsMenu();
    } else {
      setTitle(getString(R.string.title_act_edit));
      isEditMode = true;

      long todoEditId = todoFromMain._id();
      // TodoId と紐付いたタグを取得する
      Timber.d("AddEditActivity:onCreate: todoEditId is %s", todoEditId);
      addEditPresenter.loadTagForTodo(todoEditId);
    }

    rvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    rvTag.setLayoutManager(rvLayoutManager);
    rvTag.setHasFixedSize(true);
    rvTag.addItemDecoration(new RvItemDecorator(this));

    rvTag.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
          InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        return false;
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    addEditPresenter.loadPlainTag();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    addEditPresenter.onDetachMvpView();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_editor, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_delete:
        showDeleteConfirmationDialog();
        return true;
      // Respond to a click on the "Up" arrow button in the app bar
      case android.R.id.home:

        // If the pet hasn't changed, continue with navigating up to parent activity
        if (!isDataModified) {
          NavUtils.navigateUpFromSameTask(AddEditActivity.this);
          return true;
        }

        // Setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that
        // changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "Discard" button, navigate to parent activity.
                NavUtils.navigateUpFromSameTask(AddEditActivity.this);
              }
            };

        // Show a dialog that notifies the user they have unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }


  @Override
  public void onBackPressed() {
    // If the pet hasn't changed, continue with handling back button press
    if (!isDataModified) {
      super.onBackPressed();
      return;
    }

    // Create a click listener to handle the user confirming that changes should be discarded.
    DialogInterface.OnClickListener discardButtonClickListener =
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            // User clicked "Discard" button, close the current activity.
            finish();
          }
        };

    // Show dialog that there are unsaved changes
    showUnsavedChangesDialog(discardButtonClickListener);
  }
  /**********************************************************/


  private void deleteTodo() {
    if (todoFromMain != null) {
      int deletedRows = addEditPresenter.deleteTodo(todoFromMain._id());
      Timber.d("AddEditActivity:deleteTodo: deletedRows is %s", deletedRows);
    }
  }

  /*********************** Mvp implementation ***********************************/
  @Override
  public void closeActivity() {
    this.finish();
  }

  @Override
  public void setPlainTagList(List<Tag> plainTagList) {
    this.plainTagList = plainTagList;
    rvAdapterForTodoTag.setPlainTagList(plainTagList);
    rvTag.setAdapter(rvAdapterForTodoTag);
  }

  @Override
  public void setTodoWithCheckedTag(List<Long> checkedTagIdForTodoList) {
    // Todoに紐付いたタグを取得してきてここに至る
    fromMainTodoString = todoFromMain.todo();

    // Todoに紐付いたタグをAdapter に渡す
    rvAdapterForTodoTag.setCheckedTagIdForTodoList(checkedTagIdForTodoList);
    editTodo.setText(fromMainTodoString);
  }

  /*********************** OnTouchListener **********************/

  private View.OnTouchListener touchListener = new OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      isDataModified = true;
      return false;
    }
  };

  /*********************** show Dialog **********************/
  private void showDeleteConfirmationDialog() {
    // Create an AlertDialog.
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.delete_dialog_msg);
    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        // User clicked the "Delete" button, so delete the pet.
        deleteTodo();
        finish();
      }
    });
    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        // User clicked the "Cancel" button, so dismiss the dialog
        // and continue editing the pet.
        if (dialog != null) {
          dialog.dismiss();
        }
      }
    });

    // Create and show the AlertDialog
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }

  private void showUnsavedChangesDialog(
      DialogInterface.OnClickListener discardButtonClickListener) {

    // Create an AlertDialog.Builder and ,click listeners
    // for the positive and negative buttons on the dialog.
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    //set the message
    builder.setMessage(R.string.unsaved_changes_dialog_msg);
    // Set positive Button
    builder.setPositiveButton(R.string.discard, discardButtonClickListener);

    // Set negative Button with dialog.dismiss function
    builder.setNegativeButton(
        R.string.keep_editing,
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // User clicked the "Keep editing" button,
            // so dismiss the dialog and continue editing the pet.
            if (dialog != null) {
              dialog.dismiss();
            }
          }
        });

    // Create and show the AlertDialog
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
  }


  /*********************** onClick ************************/
  @OnClick(R.id.fab_todo_edit_ok)
  void onOkClicked() {
    String addedTodoStr = editTodo.getText().toString();
    String addedTagStr = editTag.getText().toString();

    boolean isTodoChanged = false;
    if (!addedTodoStr.equals(fromMainTodoString)) {
      isTodoChanged = true;
    }

    List<Long> checkedTagIdList = rvAdapterForTodoTag.getCheckedTagIdForTodoList();

    // TODOが未入力かどうか
    if (addedTodoStr.length() == 0) {
      closeActivity();
      return;
    }

    // 新規入力タグが既存のタグと一致するかどうかチェック
    if (addedTagStr.length() != 0) {
      for (Tag existedTag : plainTagList) {
        if (existedTag.tag().equals(addedTagStr)) {
          if (checkedTagIdList == null) {
            checkedTagIdList = new ArrayList<>();
          }
          // 既存のTag がチェックされたとして登録
          checkedTagIdList.add(existedTag._id());
          // 追加タグ 文字列を空にする
          addedTagStr = "";
        }
      }
    }


    if (isEditMode) {
      // TodoString に変更はあるか && 新規タグが記載されたか && CheckBox の状態に変更はあるか
      if (!isTodoChanged && addedTagStr.length() == 0
          && checkedTagIdList == null) {
        closeActivity();
        return;
      }
      long modifiedTodoId = todoFromMain._id();
      addEditPresenter.updateTodo(addedTodoStr, isTodoChanged, addedTagStr, modifiedTodoId, checkedTagIdList);
    } else {
      addEditPresenter.saveTodo(addedTodoStr, addedTagStr, checkedTagIdList);
    }
  }

}
