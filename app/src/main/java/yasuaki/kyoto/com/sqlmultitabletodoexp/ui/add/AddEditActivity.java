package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

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
    addEditPresenter.loadPlainTag();



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
      addEditPresenter.loadTodoTag(todoEditId);
    }

    rvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    rvTag.setLayoutManager(rvLayoutManager);
    rvTag.setHasFixedSize(true);
    rvTag.addItemDecoration(new RvItemDecorator(this));
    rvTag.setAdapter(rvAdapterForTodoTag);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Timber.d("AddEditActivity:onResume: ");
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
  public void setTodoWithCheckedTag(List<Long> checkedTagIdList) {
    // Todoに紐付いたタグを取得してきてここに至る
    fromMainTodoString = todoFromMain.todo();

//    checkedTagIdListBeforeEdit = checkedTagIdList;
//    Timber.d("AddEditActivity:setTodoWithCheckedTag: checkedTagIdListBeforeEdit is %s", checkedTagIdListBeforeEdit);
    // Todoに紐付いたタグをAdapter に渡す
    rvAdapterForTodoTag.setCheckedTagList(checkedTagIdList);
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


  /*********************** onClick ************************/
  @OnClick(R.id.fab_todo_edit_ok)
  void onOkClicked() {
    String addedTodoStr = editTodo.getText().toString();
    String addedTagStr = editTag.getText().toString();

    List<Long> checkedTagIdList = rvAdapterForTodoTag.getCheckedTagIdList();
    boolean cbIsModified = false;

    // TODOが未入力かどうか
    if (addedTodoStr.length() == 0) {
      closeActivity();
      return;
    }

    // 新規入力タグが既存のタグと一致するかどうかチェック
    if (addedTagStr.length() != 0) {
      for(Tag addedTag: plainTagList){
        if(addedTag.tag().equals(addedTagStr)){
          if (checkedTagIdList == null) {
            checkedTagIdList = new ArrayList<>();
          }
          long tagId = addedTag._id();
          checkedTagIdList.add(tagId);
          addedTagStr = "";
        }
      }
    }

    if (checkedTagIdList != null) {
      cbIsModified = true;
    }

    if (isEditMode) {
      // TodoString に変更はあるか && 新規タグが記載されたか && CheckBox の状態に変更はあるか
      if (addedTodoStr.equals(fromMainTodoString) && addedTagStr.length() == 0 && !cbIsModified) {
        closeActivity();
        return;
      }
      long modifiedTodoId = todoFromMain._id();
      addEditPresenter.updateTodo(addedTodoStr, addedTagStr, modifiedTodoId, checkedTagIdList);
    } else {
      addEditPresenter.saveTodo(addedTodoStr, addedTagStr, checkedTagIdList);
    }
  }

}
