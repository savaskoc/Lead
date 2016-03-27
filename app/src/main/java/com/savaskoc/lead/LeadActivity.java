package com.savaskoc.lead;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by savaskoc on 27/03/16.
 */
public class LeadActivity extends AppCompatActivity {
    private Lead lead;

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
}