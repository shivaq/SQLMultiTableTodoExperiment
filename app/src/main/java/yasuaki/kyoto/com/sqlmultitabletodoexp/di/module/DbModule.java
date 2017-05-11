package yasuaki.kyoto.com.sqlmultitabletodoexp.di.module;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import rx.schedulers.Schedulers;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.local.DbOpenHelper;

@Module
public class DbModule {

  @Provides
  @Singleton
  SQLiteOpenHelper provideOpenHelper(Application application) {
    return new DbOpenHelper(application);
  }

  @Provides
  @Singleton
  SqlBrite provideSqlBrite(){
    return new SqlBrite.Builder()
        .build();
  }

  @Provides
  @Singleton
  BriteDatabase provideBriteDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper){
    BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    return db;
  }
}
