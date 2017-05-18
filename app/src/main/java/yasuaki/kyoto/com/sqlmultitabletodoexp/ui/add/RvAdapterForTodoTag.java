package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add.RvAdapterForTodoTag.TagRvViewHolder;


public class RvAdapterForTodoTag extends RecyclerView.Adapter<TagRvViewHolder> {

  private List<Tag> plainTagList;
  private List<Long> checkedTagIdForTodoList;
  private static boolean isCBModified;

  @Inject
  RvAdapterForTodoTag() {
    plainTagList = new ArrayList<>();
    checkedTagIdForTodoList = new ArrayList<>();
  }

  @Override
  public TagRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rvTagItemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.rv_item_edit_todo_tag_list, parent, false);
    return new TagRvViewHolder(rvTagItemView);
  }

  @Override
  public void onBindViewHolder(TagRvViewHolder holder, int position) {

    // このアイテムポジションに対応する、タグリストを取得
    Tag plainTag = plainTagList.get(position);
    String plainTagStr = plainTag.tag();
    long plainTagId = plainTag._id();

    CheckBox tagCB = holder.tagCB;
    holder.tagTv.setText(plainTagStr);

    // チェック状態のタグリストに、このアイテムポジションに対応する、タグがあれば true
    if (checkedTagIdForTodoList != null && checkedTagIdForTodoList.contains(plainTagId)) {
      // ソフトウェアキーボードを閉じても、未チェック CB がチェックされてしまう現象がおきる
      // 最悪の場合、リンクテーブルに isChecked 列を入れる（Main の todoList みたいに）
      tagCB.setChecked(true);
    }

    // Listener for CheckBox
    tagCB.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (tagCB.isChecked()) {

          checkedTagIdForTodoList.add(plainTagId);
          isCBModified = true;
        } else {
          checkedTagIdForTodoList.remove(plainTagId);
          isCBModified = true;
        }
      }
    });
  }


  @Override
  public int getItemCount() {
    return plainTagList.size();
  }

  /**************************************************************/
  public void setPlainTagList(List<Tag> tagList) {
    this.plainTagList = tagList;
  }

  public void setCheckedTagIdForTodoList(List<Long> checkedTagIdForTodoList) {
    this.checkedTagIdForTodoList = checkedTagIdForTodoList;
  }

  public List<Long> getCheckedTagIdForTodoList() {
    if (!isCBModified) {
      checkedTagIdForTodoList = null;
    }
    return checkedTagIdForTodoList;
  }


  /**************************************************************/
  class TagRvViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cb_tag)
    CheckBox tagCB;
    @BindView(R.id.tag_name)
    TextView tagTv;
    @BindView(R.id.container_rv_item_todo_tag)
    LinearLayout rvItemContainer;

    public TagRvViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
