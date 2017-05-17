package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagedit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagedit.RvAdapterForTagList.TagListViewHolder;

/**
 * Created by Yasuaki on 2017/05/16.
 */
public class RvAdapterForTagList extends RecyclerView.Adapter<TagListViewHolder>{

  private static List<Tag> tagList;
  private static List<Long> tagCountList;

  @Inject
  RvAdapterForTagList(){
    tagList = new ArrayList<>();
  }

  @Override
  public TagListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rvTagListItemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.rcy_edit_tag_list_item, parent, false);
    return new TagListViewHolder(rvTagListItemView);
  }

  @Override
  public void onBindViewHolder(TagListViewHolder holder, int position) {

    Tag tag = tagList.get(position);
    String tagStr = tag.tag();
    holder.tvTagName.setText(tagStr);

    long tagCount = tagCountList.get(position);
    String todoForThisTagNum;
    if (tagCount > 1) {
      todoForThisTagNum = tagCount + " todos";
    } else {
      todoForThisTagNum = tagCount + " todo";
    }
    holder.tvTagCounter.setText(todoForThisTagNum);

    holder.lmItemContainer.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Timber.d("RvAdapterForTagList:onClick: Yes!");
      }
    });
  }

  @Override
  public int getItemCount() {
    return tagList.size();
  }

  /**************************************************************/
  public void setTagList(List<Tag> tagList){
    RvAdapterForTagList.tagList = tagList;
  }

  public void setTagCountList(List<Long> tagCountList) {
    RvAdapterForTagList.tagCountList = tagCountList;
  }

  /**************************************************************/
  class TagListViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tv_tag_name)
    TextView tvTagName;
    @BindView(R.id.tv_tag_used_count)
    TextView tvTagCounter;
    @BindView(R.id.container_rv_item_edit_tag)
    LinearLayout lmItemContainer;
    
    public TagListViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
