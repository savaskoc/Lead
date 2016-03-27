package com.savaskoc.lead;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Created by savaskoc on 27/03/16.
 */
public class Lead {
    private static final String HISTORY_KEY = "lead_history";
    private static final String LOOKUP_KEY = "lead_lookup";

    private ArrayDeque<History> history;

    private Activity activity;
    private ViewLookup lookup;

    public Lead(Activity activity, Bundle savedInstanceState) {
        this(activity, (ViewLookup) savedInstanceState.getSerializable(LOOKUP_KEY), (ArrayDeque<History>) savedInstanceState.getSerializable(HISTORY_KEY));
        setView();
    }

    public <T extends Serializable> Lead(Activity activity, ViewLookup lookup, T state) {
        this(activity, lookup, new ArrayDeque<History>());
        goState(state);
    }

    private Lead(Activity activity, ViewLookup lookup, ArrayDeque<History> history) {
        if (lookup == null || lookup.isEmpty())
            throw new RuntimeException("Lookup table is null or empty");

        this.activity = activity;
        this.lookup = lookup;
        this.history = history;
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(LOOKUP_KEY, lookup);
        outState.putSerializable(HISTORY_KEY, history);
    }

    public <T extends Serializable> T getCurrentState() {
        return (T) history.peekFirst().state;
    }

    public <T extends Serializable> void goState(T state) {
        Class<? extends Serializable> stateClass = state.getClass();
        Class<? extends View> viewClass = lookup.get(stateClass);
        if (viewClass == null)
            throw new RuntimeException("View for state " + stateClass.getSimpleName() + " is not found");
        history.addFirst(new History(state, viewClass));
        setView();
    }

    public boolean goBack() {
        if (history.size() == 1)
            return false;
        history.removeFirst();
        setView();
        return true;
    }

    private void setView() {
        activity.setContentView(createView(history.peekFirst().viewClass));
    }

    private View createView(Class<? extends View> view) {
        try {
            return view.getConstructor(Context.class).newInstance(activity);
        } catch (Exception ex) {
            Log.i("Lead", "View error", ex);
            return null;
        }
    }

    static class History implements Serializable {
        Serializable state;
        Class<? extends View> viewClass;

        History(Serializable state, Class<? extends View> viewClass) {
            this.state = state;
            this.viewClass = viewClass;
        }
    }

    static class ViewLookup extends HashMap<Class<? extends Serializable>, Class<? extends View>> {
        public static class Builder {
            ViewLookup lookup = new ViewLookup();

            public ViewLookup.Builder addLookup(Class<? extends Serializable> keyClass, Class<? extends View> viewClass) {
                lookup.put(keyClass, viewClass);
                return this;
            }

            public ViewLookup build() {
                return lookup;
            }
        }
    }
}