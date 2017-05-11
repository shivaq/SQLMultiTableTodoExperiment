package yasuaki.kyoto.com.sqlmultitabletodoexp.data;

import android.support.annotation.NonNull;
import com.squareup.sqldelight.ColumnAdapter;
import java.util.Calendar;

public class DateAdapter implements ColumnAdapter<Calendar, Long> {

  @NonNull
  @Override
  public Calendar decode(Long databaseValue) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(databaseValue);
    return calendar;
  }

  @Override
  public Long encode(@NonNull Calendar value) {
    return value.getTimeInMillis();
  }
}
