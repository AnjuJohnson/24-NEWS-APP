package com.ottapp.android.basemodule.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ottapp.android.basemodule.services.CategoryAssociationsService;

import java.util.List;

public class CategoryMenuAssociationViewModel extends AndroidViewModel {

  //  private FlowersRepository mRepository;
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<CategoryAssosiationDataModel>> mAllMenus;
    public CategoryMenuAssociationViewModel(@NonNull Application application) {
        super(application);
        mAllMenus = CategoryAssociationsService.getServices().getAllLive(0);
    }

    public LiveData<List<CategoryAssosiationDataModel>> getAllCategory() { return mAllMenus; }

}
