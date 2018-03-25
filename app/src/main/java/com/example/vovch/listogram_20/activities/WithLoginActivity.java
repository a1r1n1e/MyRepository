package com.example.vovch.listogram_20.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.vovch.listogram_20.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WithLoginActivity extends AppCompatActivity {  //todo: make abstract and useful (VARL)
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
