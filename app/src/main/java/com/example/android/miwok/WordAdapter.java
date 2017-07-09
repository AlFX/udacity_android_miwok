package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*subclass extends superclass
* notice the Custom Adapter is customize to accept a Custom Object as data source*/
public class WordAdapter extends ArrayAdapter<Word> {

    /*unknown*/
    private static final String LOG_TAG = WordAdapter.class.getSimpleName();
    private int mColorResourceId;

    /*Constructor Method*/
    public WordAdapter(Activity context, ArrayList<Word> words, int colorResourceId) {
        /*superclass constructor that requires
        * context, resource id, list of custom objects (defined inside the fragment)
        * resource id = 0 because we don't rely on a superclass array adapter inflating / creating
        * a list item view. Instead we do that manually with the getView method below*/
        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }

    /*getView provides a view (ListView, GridView...) for an AdapterView
    * position: the AdapterView position that is requesting a view
    * convertView: the recycled view to populate
    * parent: the parent viewGroup used for inflation*/
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        /*convertView is the existing view we want to reuse
        it can be null when first opening the activity
        if null, inflate the list_item.xml file so that there actually IS something to recycle*/
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        /*first step: get the position for a Word Custom Object
        * in fact we specified that the array adapter should expect a list of Word objects*/
        Word currentWord = getItem(position);

        /*find miwok translation view ID + set the updated value*/
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentWord.getMiwokTranslation());

        /*find english translation view ID + set the updated value*/
        TextView defaultTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaultTextView.setText(currentWord.getDefaultTranslation());

        /*find ImageView ID*/
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        if (currentWord.hasImage()) {
            imageView.setImageResource(currentWord.getImageResourceId());
            imageView.setVisibility(View.VISIBLE);
        } else {
            /*if, for some reason, there is no image in the considered Word object
            * make the imageView disappear aka remove any whitespace*/
            imageView.setVisibility(View.GONE);
        }

        /*gets TextContainer id + sets some background color*/
        View textContainer = listItemView.findViewById(R.id.text_container);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);

        /*returns a single TextView updated with new content*/
        return listItemView;
    }
}

    /*this mess is explained here
    theory           https://www.youtube.com/watch?v=ec9YBW3OTpY
    Miwok solution   https://youtu.be/C4zSpiZPyXU*/
