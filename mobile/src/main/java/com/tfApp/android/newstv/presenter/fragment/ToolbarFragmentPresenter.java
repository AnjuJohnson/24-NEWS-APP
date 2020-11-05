package com.tfApp.android.newstv.presenter.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.ottapp.android.basemodule.models.AssetVideosDataModel;
import com.ottapp.android.basemodule.models.CategoryAssetsList;
import com.ottapp.android.basemodule.models.MenuDataModel;
import com.ottapp.android.basemodule.presenters.fragment.BaseFragmentPresenter;
import com.ottapp.android.basemodule.services.MenuServices;
import com.ottapp.android.basemodule.view.base.fragment.BaseFragment;
import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.adaptors.TopNavigationAdapter;
import com.tfApp.android.newstv.adaptors.VerticalRecyclerAdapter;
import com.tfApp.android.newstv.models.PopupMenuItem;
import com.tfApp.android.newstv.presenter.fragment.iview.ToolbarFragmentIView;
import com.tfApp.android.newstv.view.fragment.ComingSoonFragment;
import com.tfApp.android.newstv.view.fragment.FavouriteVideoGridFragment;
import com.tfApp.android.newstv.view.fragment.HtmlViewFragment;
import com.tfApp.android.newstv.view.fragment.InfoFragment;
import com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment;

import java.util.ArrayList;
import java.util.List;

public class ToolbarFragmentPresenter<I extends ToolbarFragmentIView> extends BaseFragmentPresenter<I> implements TopNavigationAdapter.OnOptionSelectedListener {
    private int maxLimit = 10;
    private int i;
    private BaseFragment fragmentBase = null;
    private static final String ACTION_HOME = "Home";
    private static final String ACTION_FAVOURITES = "Favourites";
    private static final String ACTION_ABOUT = "About Us";
    private static final String ACTION_CONTACT_US = "Contact Us";
    private static final String ACTION_TERMS = "Terms";
    private static final String ACTION_EXIT = "exit";
    private static final String ACTION_INFO = "Info";
    private static String POS_DEFAULT = ACTION_HOME;
    private AssetVideosDataModel onItemClickVideoData;
    private List<CategoryAssetsList> filteredAssetData;
    private VerticalRecyclerAdapter youtubeSnapAdapter;
    String TAG = "test";

    public ToolbarFragmentPresenter(I iView) {
        super(iView);
    }


    //to set the data into the adapter
    public void setupMenu() {
        List<PopupMenuItem> menuItemList = new ArrayList<>();

        List<MenuDataModel> menuLists = MenuServices.getServices().getAll();
        for(MenuDataModel menuDataModel : menuLists) {
            if (!menuDataModel.getName().equals("Home") &&menuItemList.size() < 2) {
                PopupMenuItem popupMenuItem = new PopupMenuItem(menuDataModel.getId(), menuDataModel.getName(), menuDataModel.getIconUrl(), R.drawable.ic_action_home);
                menuItemList.add(popupMenuItem);
            }
        }


        TopNavigationAdapter navigationAdapter = new TopNavigationAdapter(getIView().getActivity(), menuItemList, 7);
        getIView().getBottomMenu().setLayoutManager(new LinearLayoutManager(getIView().getActivity(), LinearLayoutManager.HORIZONTAL, false));
        getIView().getBottomMenu().setAdapter(navigationAdapter);
        navigationAdapter.setOnOptionSelectedListener(this);
        navigationAdapter.setCurrentSelection(1);

    }



    @Override
    public boolean onOptionSelected(PopupMenuItem popupMenuItem) {
        String action = popupMenuItem.getTitle();
        if (action != null)
            switch (action) {

                case ACTION_FAVOURITES:
                    fragmentBase = new FavouriteVideoGridFragment();
                    break;
                case ACTION_ABOUT:
                    fragmentBase = new HtmlViewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(HtmlViewFragment.WEB_URL, "http://www.flowerstv.in/about-us/");
                    bundle.putString(HtmlViewFragment.PAGE_TITLE, action);
                    fragmentBase.setArguments(bundle);
                    break;
                case ACTION_TERMS:
                    fragmentBase = new HtmlViewFragment();
                    bundle = new Bundle();
                    bundle.putString(HtmlViewFragment.WEB_URL, "http://www.flowerstv.in/terms/");
                    bundle.putString(HtmlViewFragment.PAGE_TITLE, action);
                    fragmentBase.setArguments(bundle);
                    break;
                case ACTION_CONTACT_US:
                    fragmentBase = new HtmlViewFragment();
                    bundle = new Bundle();
                    bundle.putString(HtmlViewFragment.WEB_URL, "http://www.flowerstv.in/contact-us/");
                    bundle.putString(HtmlViewFragment.PAGE_TITLE, action);
                    fragmentBase.setArguments(bundle);
                    break;
                case ACTION_INFO:
                    fragmentBase = new InfoFragment();
                    bundle = new Bundle();
                    bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, action);
                    fragmentBase.setArguments(bundle);
                    break;
                default:

                    fragmentBase = new ComingSoonFragment();

                    bundle = new Bundle();
                    bundle.putSerializable(YoutubeVideoGridVideoGridFragment.LOAD_DATA, popupMenuItem.getId());
                    bundle.putString(YoutubeVideoGridVideoGridFragment.TITLE_TEXT, popupMenuItem.getTitle());
                    bundle.putSerializable(YoutubeVideoGridVideoGridFragment.KEY_LOADER_TYPE,
                            YoutubeVideoGridVideoGridFragment.LOADER_TYPE_MENU);
                    fragmentBase.setArguments(bundle);
                    break;
            }
        if (fragmentBase != null)
            showFragment(fragmentBase);

        return true;
    }
    private void showFragment(Fragment fragment) {
        getIView().getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.container_page, fragment)
                .commit();
    }
}
