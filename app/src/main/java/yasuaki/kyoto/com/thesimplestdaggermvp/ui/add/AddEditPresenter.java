package yasuaki.kyoto.com.thesimplestdaggermvp.ui.add;

import javax.inject.Inject;
import yasuaki.kyoto.com.thesimplestdaggermvp.data.DataManager;
import yasuaki.kyoto.com.thesimplestdaggermvp.ui.base.BasePresenter;

public class AddEditPresenter implements BasePresenter<AddEditMvpView> {

  private final DataManager dataManager;
  private AddEditMvpView addEditMvpView;

  @Inject
  public AddEditPresenter(DataManager dataManager){
    this.dataManager = dataManager;
  }

  @Override
  public void onAttachMvpView(AddEditMvpView mvpView) {
    addEditMvpView = mvpView;
  }

  @Override
  public void onDetachMvpView() {
    addEditMvpView = null;
  }

  public void saveTodo(String todo) {
    //TODO: Add data to DB
  }
}
