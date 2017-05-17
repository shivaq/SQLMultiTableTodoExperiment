package yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import javax.inject.Inject;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.tagdetail.RvAdapterTagDetail.TagDetailViewHolder;

/**
 * Created by Yasuaki on 2017/05/17.
 */

public class RvAdapterTagDetail extends RecyclerView.Adapter<TagDetailViewHolder>{

  @Inject
  RvAdapterTagDetail(){

  }

  @Override
  public TagDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(TagDetailViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }
  /**************************************************************/

  /**************************************************************/
  class TagDetailViewHolder extends RecyclerView.ViewHolder{

    public TagDetailViewHolder(View itemView) {
      super(itemView);
    }
  }
}
