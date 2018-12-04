package com.ottapp.android.basemodule.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ottapp.android.basemodule.services.MenuServices;

import java.util.List;

public class FlowersViewModel extends AndroidViewModel {

  //  private FlowersRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<MenuDataModel>> mAllMenus;
    public FlowersViewModel(@NonNull Application application) {
        super(application);
        mAllMenus = MenuServices.getServices().getAllLive(0);
    }

    public LiveData<List<MenuDataModel>> getAllMenus() { return mAllMenus; }

}
