package yasuaki.kyoto.com.sqlmultitabletodoexp;

import android.app.Application;
import com.facebook.stetho.Stetho;
import javax.inject.Inject;
import timber.log.Timber;
import yasuaki.kyoto.com.sqlmultitabletodoexp.data.DataManager;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.component.ApplicationComponent;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.component.DaggerApplicationComponent;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.module.ApplicationModule;

public class MyApplication extends Application {

  // This DataManager might be used somewhere that doesn't use presenter.
  // Where is it?
  @Inject
  DataManager mDataManager;

  private ApplicationComponent mApplicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    mApplicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .build();

    mApplicationComponent.inject(this);

    //timber 使用準備
    if (BuildConfig.DEBUG) {
      Timber.uprootAll();
      Timber.plant(new Timber.DebugTree());
    }

    // setup stetho
    Stetho.InitializerBuilder initializerBuilder =
        Stetho.newInitializerBuilder(this);

    initializerBuilder.enableWebKitInspector(
        Stetho.defaultInspectorModulesProvider(this));

    initializerBuilder.enableDumpapp(
        Stetho.defaultDumperPluginsProvider(this)
    );

    Stetho.Initializer initializer = initializerBuilder.build();
    Stetho.initialize(initializer);
  }

  public ApplicationComponent getApplicationComponent() {
    return mApplicationComponent;
  }

  // Needed to replace the component with a test specific one
  public void setComponent(ApplicationComponent applicationComponent) {
    mApplicationComponent = applicationComponent;
  }
}