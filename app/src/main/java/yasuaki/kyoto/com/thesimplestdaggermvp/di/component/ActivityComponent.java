package yasuaki.kyoto.com.thesimplestdaggermvp.di.component;

import dagger.Component;
import yasuaki.kyoto.com.thesimplestdaggermvp.di.PerActivity;
import yasuaki.kyoto.com.thesimplestdaggermvp.di.module.ActivityModule;
import yasuaki.kyoto.com.thesimplestdaggermvp.ui.add.AddEditActivity;
import yasuaki.kyoto.com.thesimplestdaggermvp.ui.main.MainActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);
    void inject(AddEditActivity activity);
}
