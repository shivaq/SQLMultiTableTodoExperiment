package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.taglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.RvItemDecorator;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag.TagWithTodoCounts;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseActivity;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail.TagDetailActivity;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.taglist.RvAdapterForTagList.RvTagCallback;

public class TagListActivity  extends BaseActivity implements TagListMvpView, RvTagCallback{

  @Inject
  TagListPresenter tagListPresenter;
  @Inject
  RvAdapterForTagList rvAdapterForTagList;

  @BindView(R.id.rv_edit_tag_act)
  RecyclerView rvEditTag;

  private LayoutManager rvLayoutManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tag_list);
    ButterKnife.bind(this);
    getActivityComponent().inject(this);

    tagListPresenter.onAttachMvpView(this);
//    tagListPresenter.loadTagList();
    tagListPresenter.loadTagWithTodoCounts();


    rvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    rvEditTag.setLayoutManager(rvLayoutManager);
    rvEditTag.setHasFixedSize(true);
    rvEditTag.addItemDecoration(new RvItemDecorator(this));
    rvEditTag.setAdapter(rvAdapterForTagList);
    rvAdapterForTagList.registerRvTagCallback(this);
  }

  /******************************** mvp implementation  ***************************************/
  @Override
  public void setTagList(List<Tag> tags) {
    rvAdapterForTagList.setTagList(tags);
    rvEditTag.setAdapter(rvAdapterForTagList);
  }

  @Override
  public void setTagWithTodoCounts(List<TagWithTodoCounts> tagWithTodoCounts) {
    List<Tag> tagList = new ArrayList();
    List<Long> tagCountsList = new ArrayList();
    for(TagWithTodoCounts tagWithCounts: tagWithTodoCounts){
      tagList.add(tagWithCounts.tag());
      tagCountsList.add(tagWithCounts.tag_count());
    }
    rvAdapterForTagList.setTagList(tagList);
    rvAdapterForTagList.setTagCountList(tagCountsList);
    rvEditTag.setAdapter(rvAdapterForTagList);
  }

  /******************************** callback implementation  ***************************************/
  @Override
  public void onRvTagItemClicked(Tag clickedTag) {
    Intent intent = new Intent(this, TagDetailActivity.class);
    intent.putExtra(TagDetailActivity.TAG_EXTRA, clickedTag);
    startActivity(intent);
  }
}
