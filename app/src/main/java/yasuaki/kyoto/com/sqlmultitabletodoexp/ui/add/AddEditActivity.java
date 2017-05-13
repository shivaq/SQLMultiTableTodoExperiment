package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
  private boolean isDataModified;
  private String fromMainTodoString;

  public static final String TODO_EXTRA = "yasuaki.kyoto.com.sqlmultitabletodoexp.TODO_EXTRA";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add);
    ButterKnife.bind(this);
    getActivityComponent().inject(this);
    addEditPresenter.onAttachMvpView(this);

    // 入力欄をタッチしたら、isDataModified を true にしているだけ
    editTodo.setOnTouchListener(touchListener);



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
      setTodo();
    }
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
    switch(item.getItemId()){
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

  private void setTodo(){
    fromMainTodoString = todoFromMain.todo();
    editTodo.setText(fromMainTodoString);
  }

  private void deleteTodo(){
    if (todoFromMain != null) {
      int deletedRows = addEditPresenter.deleteTodo(todoFromMain._id());
      Timber.d("AddEditActivity:deleteTodo: deletedRows is %s", deletedRows);
    }
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
      if (addedTodo.equals(fromMainTodoString)) {
        closeActivity();
        return;
      }
      long todoId = todoFromMain._id();
      Timber.d("AddEditActivity:onOkClicked: editMode");
      addEditPresenter.updateTodo(addedTodo, todoId);
    } else {
      addEditPresenter.saveTodo(addedTodo);
    }
  }
}
