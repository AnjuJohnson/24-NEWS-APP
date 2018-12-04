package com.ottapp.android.basemodule.services.base;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ottapp.android.basemodule.app.AppDatabase;
import com.ottapp.android.basemodule.app.BaseApplication;

import java.util.List;

/**
 * The type Base service.
 *
 * @param <T> the type parameter
 */
public abstract class BaseService<T> {

    private boolean retryRequired = true;

    /**
     * Instantiates a new Base service.
     */
    public BaseService() {
        AppDatabase appDatabase = BaseApplication.getApplication().getDatabase();
        setupDao(appDatabase);
    }

    /**
     * Sets dao.
     *
     * @param appDatabase the app database
     */
    protected abstract void setupDao(@NonNull AppDatabase appDatabase);

    /**
     * Insert.
     *
     * @param model the model
     */
    public abstract void insert(T model);

    /**
     * Insert all.
     *
     * @param models the models
     */
    public abstract void insertAll(List<T> models);

    /**
     * Gets all.
     *
     * @return the all
     */
    public abstract List<T> getAll();

    public abstract LiveData<List<T>> getAllLive(int id);

    /**
     * Gets by id.
     *
     * @param id the id
     * @return the by id
     */
    public abstract T getById(int id);

    /**
     * Delete all in valid.
     */
    public abstract void deleteAllInValid();

    /**
     * Gets last updated timestamp.
     *
     * @return the last updated timestamp
     */
    public abstract long getLastUpdatedTimestamp();

    /**
     * Is present boolean.
     *
     * @return the boolean
     */
    public abstract boolean isPresent();

    /**
     * Destroy.
     */
    public abstract void destroy();

    /**
     * Is retry required boolean.
     *
     * @return the boolean
     */
    public boolean isRetryRequired() {
        return retryRequired;
    }

    /**
     * Sets retry required.
     *
     * @param retryRequired the retry required
     */
    public void setRetryRequired(boolean retryRequired) {
        this.retryRequired = retryRequired;
    }
}
