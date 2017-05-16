package yasuaki.kyoto.com.sqlmultitabletodoexp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;
import javax.inject.Inject;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ActivityContext;

/**
 * Created by Yasuaki on 2017/05/16.
 */

public class RvItemDecorator extends RecyclerView.ItemDecoration {

  private Drawable divider;

  @Inject
  public RvItemDecorator(@ActivityContext Context context) {
    divider = ContextCompat.getDrawable(context, R.drawable.line_divider);
  }

  @Override
  public void onDrawOver(Canvas c, RecyclerView parent, State state) {
    super.onDrawOver(c, parent, state);
    // get the left and right bounds position of the parent Rv.
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();

    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);

      RecyclerView.LayoutParams params = (LayoutParams) child.getLayoutParams();
      int top = child.getBottom() + params.bottomMargin;
      int bottom = top + divider.getIntrinsicHeight();

      divider.setBounds(left, top, right, bottom);
      divider.draw(c);
    }
  }
}