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
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add.TagRvAdapter.TagRvViewHolder;


public class TagRvAdapter extends RecyclerView.Adapter<TagRvViewHolder> {

  private static List<Tag> tagList;
  private static List<Long> checkedTagList;

  @Inject
  TagRvAdapter() {
    tagList = new ArrayList<>();
    checkedTagList = new ArrayList<>();
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

    CheckBox tagCB = holder.tagCB;
    holder.tagTv.setText(tagStr);

    if (checkedTagList.contains(tagId)) {
      for(long checkedTag: checkedTagList){
        Timber.d("TagRvAdapter:onBindViewHolder: checkedTag is %s, tagId is %s", checkedTag, tagId);
      }
    }

    if (checkedTagList.size() != 0 && checkedTagList.contains(tagId)) {
      Timber.d("TagRvAdapter:onBindViewHolder: ");
      tagCB.setChecked(true);
    }

    // todo OBJ ロード時に、紐付いているタグ情報を渡す
    // そのタグのIDと、現在のRvItem のタグ のID とが一致したら、チェック状態にする



    tagCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          checkedTagList.add(tagId);
          Timber.d("TagRvAdapter:onCheckedChanged: remove isChecked is %s list size is %s", isChecked, checkedTagList.size());
        } else {
          checkedTagList.remove(tagId);
          Timber.d("TagRvAdapter:onCheckedChanged: add isChecked is %s  list size is %s", isChecked, checkedTagList.size());

        }
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
  public void setTagList(List<Tag> tagList) {
    this.tagList = tagList;
  }

  public void setCheckedTagList(List<Long> checkedTagList) {
    Timber.d("TagRvAdapter:setCheckedTagList: checkedTagList size is %s", checkedTagList.size());
    this.checkedTagList = checkedTagList;
  }

  public List<Long> getCheckedTag() {
    Timber.d("TagRvAdapter:getCheckedTag: checkedTagList size is %s", checkedTagList.size());
    return checkedTagList;
  }

  /**************************************************************/
  class TagRvViewHolder extends RecyclerView.ViewHolder {

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
