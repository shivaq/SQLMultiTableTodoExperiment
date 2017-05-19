package yasuaki.kyoto.com.sqlmultitabletodoexp;


import android.os.Build;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import rx.observers.TestSubscriber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.local.DbCrudHelper;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.local.DbOpenHelper;


@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP_MR1)
@RunWith(RobolectricTestRunner.class)
public class DbCrudHelperTest {

  final DbCrudHelper dbCrudHelper = new DbCrudHelper(new DbOpenHelper(RuntimeEnvironment.application));

  @Before
  public void setUp(){
    dbCrudHelper.clearTables().subscribe();
  }

  @Test
  public void loadTodo(){
    String[] todoArray = MockModel.newTodoStrArray();

    for(String todoStr:todoArray){
      dbCrudHelper.insertTodo(todoStr, null, null);
    }

    TestSubscriber<String> testSubscriber = new TestSubscriber<>();
//    dbCrudHelper.loadTodo().subscribe(testSubscriber);

  }



}
