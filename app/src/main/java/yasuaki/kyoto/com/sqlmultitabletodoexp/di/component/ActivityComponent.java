package yasuaki.kyoto.com.sqlmultitabletodoexp.di.component;

import dagger.Component;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.PerActivity;
import yasuaki.kyoto.com.sqlmultitabletodoexp.di.module.ActivityModule;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.add.AddEditActivity;
import yasuaki.kyoto.com.sqlmultitabletodoexp.ui.main.MainActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);
    void inject(AddEditActivity activity);
}
