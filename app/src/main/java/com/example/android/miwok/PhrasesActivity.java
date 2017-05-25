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

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

    // handles playback of all the sound files
    // m-prefix indicates conventionally non-public, non-static variable [stands for member]
    // in other words, m indicates a local variable / function, inside a class
    private MediaPlayer mMediaPlayer;

    // this listener gets triggered when the {@link MediaPlayer} has completed playing the audio file
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {

        // @Override is a Java annotation
        // tells the compiler that the following method overrides a method of its superclass
        // explicitly declares method overriding
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        // create a list of words, made of word objects
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'm feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I am coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I'm coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let's go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        /* Create a {@link WordAdapter}, whose data source is a list of {@link Word}s. The adapter
        * knows how to create list items for each item in the list. */
        WordAdapter adapter = new WordAdapter(this, words, R.color.category_phrases);

        /* Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        There should be a {@link ListView} with the view ID called list, which is declared in the
        word_list.xml layout file.*/
        ListView listView = (ListView) findViewById(R.id.list);

        /* Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        * {@link ListView} will display list items for each {@link Word} in the list.*/
        listView.setAdapter(adapter);

        // set a click listener to play the audio when the list item is clicked on
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /* release the media player if it currently exists because we are about to play a
                different sound file */
                releaseMediaPlayer();

                // get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);

                /* create and setup the {@link MediaPlayer} for the audio resource associated
                with the current word */
                mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());

                // start the audio file
                mMediaPlayer.start();

                /* setup a listener on the media player, so that we can stop and release the media
                player once the sound has finished playing. */
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
            }
        });
    }

    // Clean up the media player by releasing its resources
    private void releaseMediaPlayer() {
        // if media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {

            /* regardless of the current state of the media player, release its resources because
            we no longer need it */
            mMediaPlayer.release();

            /* set the media player back to null. For our code, we've decided that setting the media
            player to null is an easy way to tell that the media player is not configured to play
            an audio file at the moment */
            mMediaPlayer = null;
        }
    }

}