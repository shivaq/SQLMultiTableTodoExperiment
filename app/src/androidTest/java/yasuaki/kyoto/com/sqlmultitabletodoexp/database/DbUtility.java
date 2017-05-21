package yasuaki.kyoto.com.sqlmultitabletodoexp.database;

import android.content.Context;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.local.DbOpenHelper;

/**
 * Created by Yasuaki on 2017/05/19.
 */

public class DbUtility {

  public static void deleteDatabase(Context context){
    context.deleteDatabase(DbOpenHelper.DATABASE_NAME);
  }

}
