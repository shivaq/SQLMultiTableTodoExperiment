package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.RvItemDecorator;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo.TodoForTag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseActivity;

public class TagDetailActivity extends BaseActivity implements TagDetailMvpView {

  public static final String TAG_EXTRA = "yasuaki.kyoto.com.sqlmultitabletodoexp.TAG_EXTRA";
  @Inject
  TagDetailPresenter tagDetailPresenter;
  @Inject
  RvAdapterTagDetail rvAdapterTagDetail;

  @BindView(R.id.rv_tag_detail)
  RecyclerView RvTagEdit;
  @BindView(R.id.edit_tag)
  EditText tagEditText;

  private boolean isDataModified;
  private Tag tagFromTagListActivity;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tag_detail);
    ButterKnife.bind(this);
    getActivityComponent().inject(this);
    tagEditText.setOnTouchListener(touchListener);

    tagDetailPresenter.onAttachMvpView(this);

    Intent intentFromTagList = getIntent();
    tagFromTagListActivity = intentFromTagList.getParcelableExtra(TAG_EXTRA);
    tagEditText.setText(tagFromTagListActivity.tag());
    if (tagFromTagListActivity != null) {
      long tagId = tagFromTagListActivity._id();
//      Toast.makeText(this, "tag is " + tagFromTagListActivity, Toast.LENGTH_SHORT).show();
      tagDetailPresenter.loadTodoForTag(tagId);
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
        LinearLayoutManager.VERTICAL, false);
    RvTagEdit.setLayoutManager(linearLayoutManager);
    RvTagEdit.setHasFixedSize(true);
    RvTagEdit.addItemDecoration(new RvItemDecorator(this));
    RvTagEdit.setAdapter(rvAdapterTagDetail);

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    tagDetailPresenter.onDetachMvpView();
  }

  private View.OnTouchListener touchListener = new OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
      isDataModified = true;
      return false;
    }
  };
  /****************************** mvp implementation **************************************/
  @Override
  public void setTodoForTagList(List<TodoForTag> todoForTagList) {
    List<Todo> todoList = new ArrayList();
    for(TodoForTag todoForTag: todoForTagList){

      todoForTag.todo_tag().todo_id();
      Timber.d("TagDetailActivity:setTodoForTagList:todoId is %s, todo is %s", todoForTag.todo_tag().todo_id(),todoForTag.todo() );
      todoList.add(todoForTag.todo());
    }
    Timber.d("TagDetailActivity:setTodoForTagList: todoList size is %s, todoForTagList size is %s", todoList.get(0), todoForTagList.size());
    rvAdapterTagDetail.setTodoList(todoList);
    RvTagEdit.setAdapter(rvAdapterTagDetail);
  }
}

