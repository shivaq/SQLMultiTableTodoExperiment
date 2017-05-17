package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Todo;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail.RvAdapterTagDetail.TagDetailViewHolder;

/**
 * Created by Yasuaki on 2017/05/17.
 */

public class RvAdapterTagDetail extends RecyclerView.Adapter<TagDetailViewHolder> {

  private static List<Todo> todoList;

  @Inject
  RvAdapterTagDetail() {
    todoList = new ArrayList();
  }

  @Override
  public TagDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rvTodoItemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.rv_item_edit_tag_todo_list, parent, false);
    return new TagDetailViewHolder(rvTodoItemView);
  }

  @Override
  public void onBindViewHolder(TagDetailViewHolder holder, int position) {
    Todo todo = todoList.get(position);
    holder.tvTodoName.setText(todo.todo());

  }

  @Override
  public int getItemCount() {
    return todoList.size();
  }
  /**************************************************************/
  void setTodoList(List<Todo> todoList){
    RvAdapterTagDetail.todoList = todoList;
  }

  /**************************************************************/
  class TagDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_todo_for_tag)
    TextView tvTodoName;

    public TagDetailViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
