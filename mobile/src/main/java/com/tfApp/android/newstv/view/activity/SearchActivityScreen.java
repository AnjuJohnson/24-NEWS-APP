package com.tfApp.android.newstv.view.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.tfApp.android.newstv.R;
import com.tfApp.android.newstv.presenter.activity.SearchScreenActivityPresenter;
import com.tfApp.android.newstv.presenter.activity.iview.SearchScreenActivityIView;
import com.tfApp.android.newstv.view.fragment.HomeFragment;
import com.tfApp.android.newstv.view.fragment.SearchVideoGridFragment;
import com.tfApp.android.newstv.view.fragment.YoutubeVideoGridVideoGridFragment;
import com.ottapp.android.basemodule.view.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivityScreen extends BaseActivity<SearchScreenActivityPresenter<SearchScreenActivityIView>, SearchScreenActivityIView> implements SearchScreenActivityIView, SearchView.OnQueryTextListener {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageView voiceSearch, searchBack;
    private SearchView searchView;
    private SearchVideoGridFragment fragment;
    private Timer timer;
    private TimerTask timerTask;
    //private SearchScreenActivityPresenter getPresenter();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search_screen);
        //getPresenter()=initializePresenter();
        initialization();
        
        getPresenter().setFragment();
        eventListener();
        getPresenter().onCreate();

    }

    //to manage the ui events
    private void eventListener() {

        searchView.setOnQueryTextListener(this);
        voiceSearch.setOnClickListener(view -> promptSpeechInput());
        searchBack.setOnClickListener(v -> onBackPressed());

    }

    private void startTask() {
        cancelTimers();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                initSearch();
            }
        };
        timer.schedule(timerTask, 500);
    }

    private void initSearch() {
        if (searchView != null && searchView.getQuery() != null) {
            String query = searchView.getQuery().toString();
            setupAdapter(query);
        } else {
            setupAdapter(null);
        }
    }

    private void setupAdapter(String query) {
        if (query != null) {
            getPresenter().loadResult(query);
        } else {
            getPresenter().clearResults();
        }
    }

    private void cancelTimers() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    @Override
    public void onBackPressed() {
//        Intent intent = getIntent();
//        Bundle bundle = new Bundle();
//        Fragment fragmentBase = new HomeFragment();
//        fragmentBase.setArguments(bundle);
//        if (fragmentBase != null)
//            showFragment(fragmentBase);
        Intent intent = new Intent(this,HolderActivity.class);
        startActivity(intent);

    }


    private void showFragment(Fragment fragment) {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    //initialization of ui components
    private void initialization() {
        fragment = new SearchVideoGridFragment();
        searchView = findViewById(R.id.serch_data);
        voiceSearch = findViewById(R.id.voice_search);
        searchBack = findViewById(R.id.search_back);
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
    }


    @Override
    protected SearchScreenActivityPresenter<SearchScreenActivityIView> initializePresenter() {
        return new SearchScreenActivityPresenter<>(this);
    }

    @Override
    public void toggleProgress(boolean visibility, boolean cancelable) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.voice_search_menu, menu);
        return true;
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String voice_query = result.get(0);
                    setupAdapter(voice_query);
                    searchView.setOnQueryTextListener(null);
                    searchView.setQuery(voice_query, false);
                    searchView.setOnQueryTextListener(SearchActivityScreen.this);
                }
                break;
            }

        }
    }


    @Override
    public SearchView getSearchView() {
        return findViewById(R.id.serch_data);
    }

    @Override
    public ImageView getVoiceSearchView() {
        return findViewById(R.id.voice_search);
    }

    @Override
    public ImageView getRootView() {
        return findViewById(R.id.window_background);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        startTask();
        return true;
    }


}
