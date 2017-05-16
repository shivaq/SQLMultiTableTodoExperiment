package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagedit;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import javax.inject.Inject;
import yasuaki.kyoto.com.sqlmultitabletodoexp.R;
import yasuaki.kyoto.com.sqlmultitabletodoexp.RvItemDecorator;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.model.Tag;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.base.BaseActivity;

public class TagListActivity  extends BaseActivity implements TagListMvpView{

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
    tagListPresenter.loadTagList();

    rvLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    rvEditTag.setLayoutManager(rvLayoutManager);
    rvEditTag.setHasFixedSize(true);
    rvEditTag.addItemDecoration(new RvItemDecorator(this));
    rvEditTag.setAdapter(rvAdapterForTagList);
  }

  @Override
  public void setTagList(List<Tag> tags) {
    rvAdapterForTagList.setTagList(tags);
    rvEditTag.setAdapter(rvAdapterForTagList);
  }
}
