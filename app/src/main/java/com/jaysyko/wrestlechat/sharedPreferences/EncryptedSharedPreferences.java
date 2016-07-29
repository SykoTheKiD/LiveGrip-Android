package com.jaysyko.wrestlechat.sharedPreferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.jaysyko.wrestlechat.auth.Krit;

import java.util.Map;
import java.util.Set;

/**
 * @author Jay Syko on 2016-07-28.
 */
public class EncryptedSharedPreferences implements SharedPreferences {
    // Don't use anything you wouldn't want to
    // get out there if someone decompiled
    // your app.


    protected SharedPreferences delegate;
    protected Context context;

    public EncryptedSharedPreferences(Context context, SharedPreferences delegate) {
        this.delegate = delegate;
        this.context = context;
    }

    public class Editor implements SharedPreferences.Editor {
        protected SharedPreferences.Editor delegate;

        @SuppressLint("CommitPrefEdits")
        public Editor() {
            this.delegate = EncryptedSharedPreferences.this.delegate.edit();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            delegate.putBoolean(key, value);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            delegate.putFloat(key, value);
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            delegate.putInt(key, value);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            delegate.putLong(key, value);
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            delegate.putString(key, Krit.encrypt(context, value));
            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            return null;
        }

        @Override
        public void apply() {
            delegate.apply();
        }

        @Override
        public Editor clear() {
            delegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return delegate.commit();
        }

        @Override
        public Editor remove(String s) {
            delegate.remove(s);
            return this;
        }
    }

    public Editor edit() {
        return new Editor();
    }


    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException(); // left as an exercise to the reader
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return delegate.getBoolean(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return delegate.getFloat(key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        return delegate.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return delegate.getLong(key, defValue);
    }

    @Override
    public String getString(String key, String defValue) {
        final String v = delegate.getString(key, null);
        return v != null ? Krit.decrypt(context, v) : defValue;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, Set<String> defValues) {
        return null;
    }

    @Override
    public boolean contains(String s) {
        return delegate.contains(s);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }


}