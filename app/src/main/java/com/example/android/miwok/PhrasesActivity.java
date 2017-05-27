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

import android.content.Context;
import android.media.AudioManager;
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

    // handles audio focus when playing a sound file
    private AudioManager mAudioManager;

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

    // this listener get triggered whenever the audio focus changes
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // AUDIOFOCUS_TRANSIENT = lost audiofocus for short time
                // AUDIOFOCUS_TRANSIENT_CAN_DUCK = lost audiofocus for short time but app can play with reduced volume in background
                // these cases are treated the same way because Miwok app plays only short files

                // pause + reset = play from beginning when resuming
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // AUDIOFOCUS_GAIN = regained focus + can resume playback
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // AUDIOFOCUS_LOSS = lost audio focus + stop playback + clean up resources
                releaseMediaPlayer();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        // create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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


                // request audio focus in order to play the audio file (for a short time)
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // we have audiofocus now
                    // create and setup the {@link MediaPlayer} for the audio resource associated
                    // with the current word
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, word.getAudioResourceId());

                    // start the audio file
                    mMediaPlayer.start();

                    // setup a listener on the media player, so that we can stop and release the
                    // media player once the sound ha finished playing
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }

    /* this works either when overriding onStop or onPause methods
     * overriding onPause thought brings a somewhat faster interruption of the audio */
    @Override
    protected void onStop() {
        super.onStop();
        /* when the activity is stopped, release the media player resources because we won't
        be playing any more sounds */
        // aka when you press home button while playing audio, sound should stop
        releaseMediaPlayer();
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

            /* regardless of whether or not we were granted audio focus, abandon it. this also unregisters
            the AudioFocusChangeListener so we don't get anymore callbacks */
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}