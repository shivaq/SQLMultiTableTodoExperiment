package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main.MainRvAdapter.MainRvViewHolder;

/**
 * Created by Yasuaki on 2017/05/09.
 */

public class MainRvAdapter extends RecyclerView.Adapter<MainRvViewHolder> {

  private static List<Todo> todoList;
  private RvCallback rvCallback;

  @Inject
  MainRvAdapter() {
    todoList = new ArrayList<>();
  }

  @Override
  public MainRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mainRvItemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.rv_item_todo_list, parent, false);
    return new MainRvViewHolder(mainRvItemView);
  }

  @Override
  public void onBindViewHolder(MainRvViewHolder holder, int position) {

    Todo todo = todoList.get(position);
    String todoItem = todo.todo();
    boolean isTodoChecked = todo.isChecked();

    long todoId = todo._id();
    holder.txtTodo.setText(todoItem);

    CheckBox todoCb = holder.cbTodo;
    todoCb.setChecked(isTodoChecked);

    // API 24 以前にも互換性があるが、相対的にコスト高になるとのこと
    holder.cbTodo.setOnClickListener(view -> {
      if (rvCallback != null) {
        rvCallback.onRvTodoCbClicked(!isTodoChecked, todoId);
      }
    });
//        new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        if(rvCallback != null){
//          rvCallback.onRvTodoCbClicked(todoItem);
//        }
//      }
//    });
  }


  @Override
  public void onViewRecycled(MainRvViewHolder holder) {
    super.onViewRecycled(holder);
    Timber.d("MainRvAdapter:onViewRecycled: ");
  }

  @Override
  public void onViewAttachedToWindow(MainRvViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    Timber.d("MainRvAdapter:onViewAttachedToWindow: ");
  }

  @Override
  public void onViewDetachedFromWindow(MainRvViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    Timber.d("MainRvAdapter:onViewDetachedFromWindow: ");
  }

  @Override
  // スクロール時にはコールされず、更新時には 2度コールされる
  public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    Timber.d("MainRvAdapter:onAttachedToRecyclerView: ");
    if (rvCallback != null) {
      rvCallback.onRvRefreshed();
    }
  }

  @Override
  public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    super.onDetachedFromRecyclerView(recyclerView);
    Timber.d("MainRvAdapter:onDetachedFromRecyclerView: ");
  }

  @Override
  public int getItemCount() {
    return todoList.size();
  }

  /******************  *****************/

  public void setTodoList(List<Todo> todoList) {
    this.todoList = todoList;
  }


  /*********************** ClickListener *****************************/
  interface RvCallback {

    void onRvTodoCbClicked(boolean isChecked, long id);

    void onRvItemClicked(Todo clickedTodo);

    void onRvRefreshed();
  }

  public void registerRvCallback(RvCallback rvCallback) {
    this.rvCallback = rvCallback;
  }


  /**************************************************************/
  class MainRvViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cb_todo)
    CheckBox cbTodo;
    @BindView(R.id.todo_main)
    TextView txtTodo;
    @BindView(R.id.tag_add_main)
    ImageView imgTagAdd;
    @BindView(R.id.txt_tag_list_main)
    TextView txtTagsForTodo;

    public MainRvViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    // CallBack メソッドを、ViewHolder 内のクリックListenerからも呼び出せる
    @OnClick(R.id.container_rv_item_todo)
    void onRvTodoClicked() {
      int adapterPosition = getAdapterPosition();
      Todo clickedTodo = todoList.get(adapterPosition);
      if (rvCallback != null) {
        rvCallback.onRvItemClicked(clickedTodo);
      }
    }
  }
}
