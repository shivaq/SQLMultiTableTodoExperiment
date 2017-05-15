package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add.TagRvAdapter.TagRvViewHolder;


public class TagRvAdapter extends RecyclerView.Adapter<TagRvViewHolder> {

  private static List<Tag> tagList;

  @Inject
  TagRvAdapter(){
    tagList = new ArrayList<>();
  }

  @Override
  public TagRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rvTagItemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.rcy_tag_list_item, parent, false);
    return new TagRvViewHolder(rvTagItemView);
  }

  @Override
  public void onBindViewHolder(TagRvViewHolder holder, int position) {

    Tag tag = tagList.get(position);
    String tagStr = tag.tag();
    long tagId = tag._id();
    // TODO:選択有無は、TAG OBJ ではなく TODO OBJ の属性ではないか？
    boolean isTagSelected = tag.isSelected();

    CheckBox tagCB = holder.tagCB;
    holder.tagTv.setText(tagStr);
    tagCB.setChecked(isTagSelected);

    tagCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      }
    });

  }


  @Override
  public void onViewAttachedToWindow(TagRvViewHolder holder) {
    super.onViewAttachedToWindow(holder);
  }

  @Override
  public int getItemCount() {
    return tagList.size();
  }
  /**************************************************************/
  public void setTagList(List<Tag> tagList){
    this.tagList = tagList;
  }

  /**************************************************************/
  class TagRvViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.cb_tag)
    CheckBox tagCB;
    @BindView(R.id.tag_name)
    TextView tagTv;

    public TagRvViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
