package com.savaskoc.lead;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by savaskoc on 27/03/16.
 */
public class LeadActivity extends AppCompatActivity {
    protected Lead lead;

    @Override
    public void onBackPressed() {
        if (!lead.goBack())
            super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (lead == null)
            throw new RuntimeException("Lead is null");
        lead.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lead = new Lead(this, savedInstanceState);
    }

    public <T extends Serializable> T getCurrentState() {
        return lead.getCurrentState();
    }

    public boolean goBack() {
        return lead.goBack();
    }

    public <T extends Serializable> void goState(T state) {
        lead.goState(state);
    }
}