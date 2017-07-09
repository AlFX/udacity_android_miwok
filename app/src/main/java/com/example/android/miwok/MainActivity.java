/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    // Boilerplate = tells what happens when the activity is initialized
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Within XML, find the ViewPager ID that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an Custom Adapter that associates a fragment to a screen position (see CategoryAdapter.java)
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Link the Custom Adapter to the the ViewPager we created before
        viewPager.setAdapter(adapter);

        /*Within XML, find the TabLayout ID that will allow the user to pick a specific fragment
        * this basically looks like a toolbar*/
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        /*Associate the TabLayout to the ViewPager in order to enable the links to the fragments*/
        tabLayout.setupWithViewPager(viewPager);

    }
}
