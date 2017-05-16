package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add.RvAdapterForTodoTag.TagRvViewHolder;


public class RvAdapterForTodoTag extends RecyclerView.Adapter<TagRvViewHolder> {

  private static List<Tag> plainTagList;
  private static List<Long> checkedTagIdList;
  private static boolean isCBModified;

  @Inject
  RvAdapterForTodoTag() {
    plainTagList = new ArrayList<>();
    checkedTagIdList = new ArrayList<>();
  }

  @Override
  public TagRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rvTagItemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.rcy_tag_list_item, parent, false);
    return new TagRvViewHolder(rvTagItemView);
  }

  @Override
  public void onBindViewHolder(TagRvViewHolder holder, int position) {

    Tag plainTag = plainTagList.get(position);
    String plainTagStr = plainTag.tag();
    long plainTagId = plainTag._id();

    CheckBox tagCB = holder.tagCB;
    holder.tagTv.setText(plainTagStr);

    if (checkedTagIdList != null && checkedTagIdList.contains(plainTagId)) {
      tagCB.setChecked(true);
    }

    // Listener for CheckBox
    tagCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          checkedTagIdList.add(plainTagId);
          isCBModified = true;
        } else {
          checkedTagIdList.remove(plainTagId);
          isCBModified = true;
        }
      }
    });
    // Listener for RvItem CheckBox にセットしていないと、クリック後の処理がなされない
    holder.rvItemContainer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (tagCB.isChecked()) {
          checkedTagIdList.remove(plainTagId);
          isCBModified = true;
          tagCB.setChecked(false);
        } else {
          checkedTagIdList.add(plainTagId);
          isCBModified = true;
          tagCB.setChecked(true);
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
    RvAdapterForTodoTag.plainTagList = tagList;
  }

  public void setCheckedTagList(List<Long> checkedTagIdList) {
    RvAdapterForTodoTag.checkedTagIdList = checkedTagIdList;
    Timber.d("RvAdapterForTodoTag:setCheckedTagList: TagId");
  }

  public List<Long> getCheckedTagIdList() {
    if (!isCBModified) {
      checkedTagIdList = null;
    }
    return checkedTagIdList;
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
