package com.udacity.sandwichclub;

import android.content.Intent;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;
import com.udacity.sandwichclub.utils.NetworkUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private int htmlResponse;

    private Sandwich mSandwich;

    private TextView mOrigin;
    private TextView mAKA;
    private TextView mIngredients;
    private TextView mDescription;

    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        mOrigin = (TextView) findViewById(R.id.origin_tv);
        mAKA = (TextView) findViewById(R.id.also_known_tv);
        mIngredients = (TextView) findViewById(R.id.ingredients_tv);
        mDescription = (TextView) findViewById(R.id.description_tv);
        mImage = (ImageView) findViewById((R.id.image_iv));

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        mSandwich = sandwich;

        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {

        new testImgURL().execute();

        if (mSandwich.getAlsoKnownAs().get(0) == "") {
            mAKA.append("- no other known aliases");
        } else {

            int akaLength = mSandwich.getAlsoKnownAs().size();

            for (int i = 0; i < akaLength; i++) {
                mAKA.append("- " + mSandwich.getAlsoKnownAs().get(i));
                if (i + 1 != akaLength) {
                    mAKA.append("\n");
                }
            }
        }

        if (mSandwich.getIngredients().get(0) == "") {
            mIngredients.append("- ingredients are unknown");
        } else {

            int akaLength = mSandwich.getIngredients().size();

            for (int i = 0; i < akaLength; i++) {
                mIngredients.append("- " + mSandwich.getIngredients().get(i));
                if (i + 1 != akaLength) {
                    mIngredients.append("\n");
                }
            }
        }

        if (mSandwich.getPlaceOfOrigin().isEmpty() || mSandwich.getPlaceOfOrigin().equals("")) {
            mOrigin.append("- place of origin is unknown");
        } else {
            mOrigin.append("- " + mSandwich.getPlaceOfOrigin());
        }

        if (mSandwich.getDescription().isEmpty() || mSandwich.getDescription().equals("")) {
            mDescription.append("- descrption is unvailable.");
        } else {
            mDescription.append("- " + mSandwich.getDescription());
        }
    }

    private class testImgURL extends AsyncTask<String, Void, String> {

        int htmlCode;

        @Override
        protected String doInBackground(String... String) {
            URL imageURL = NetworkUtils.buildUrl(mSandwich.getImage());
            try {
                HttpURLConnection test = (HttpURLConnection) imageURL.openConnection();
                test.setRequestMethod("GET");
                test.connect();
                int code = test.getResponseCode();
                htmlCode = code;
                return "" + code;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (htmlCode == 404) {
                mImage.getLayoutParams().height = 0;
                mImage.requestLayout();
            }
            super.onPostExecute(s);
        }
    }

}
