package yasuaki.kyoto.com.sqlmultitabletodoexp.di.module;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.ApplicationContext;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

}
