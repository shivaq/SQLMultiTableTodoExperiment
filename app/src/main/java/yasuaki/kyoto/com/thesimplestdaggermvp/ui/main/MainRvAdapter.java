package yasuaki.kyoto.com.thesimplestdaggermvp.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import yasuaki.kyoto.com.thesimplestdaggermvp.R;
import yasuaki.kyoto.com.thesimplestdaggermvp.ui.main.MainRvAdapter.MainRvViewHolder;

/**
 * Created by Yasuaki on 2017/05/09.
 */

public class MainRvAdapter extends RecyclerView.Adapter<MainRvViewHolder>{

  private static List<String> todoList;

  @Inject
  MainRvAdapter(){
    todoList = new ArrayList<>();
  }

  @Override
  public MainRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mainRvItemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.rcy_item_main, parent, false);
    return new MainRvViewHolder(mainRvItemView);
  }

  @Override
  public void onBindViewHolder(MainRvViewHolder holder, int position) {

    String todo = todoList.get(position);
    holder.txtTodo.setText(todo);
  }

  @Override
  public int getItemCount() {
    return todoList.size();
  }

  /******************  *****************/

  public void setTodoList(List<String> todoList){
    this.todoList = todoList;
  }

  /**************************************************************/
  class MainRvViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.todo_checkBox)
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
  }
}
