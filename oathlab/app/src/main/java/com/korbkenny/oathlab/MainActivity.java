package com.korbkenny.oathlab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String API_URL = "https://api.twitter.com";
    public static final String GET_BEARER = "https://api.twitter.com/oauth2/token";
    public static final String BEARER_TOKEN_CREDENTIALS =
            "UUo5elJocFZ3REFmZFpkM0hrUTg3YTRTbTowUmZESzQ4bExmMWN0d25RaG55ZEd0UXNzcVhMR2FZT2JSNVJOSmZWY0tSN0FqbXRROQ==";
    public static final String SEARCH_URL =
            "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
    public String mAccessToken;
    EditText mEditText;
    Button mButton;
    RecyclerView mRecyclerView;
    TweetAdapter mAdapter;
    ArrayList<TweetText> mTweetTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            getBearerToken();
        }else{
            Toast.makeText(this, "No network connectivity, yo.", Toast.LENGTH_SHORT).show();
        }

        mEditText = (EditText)findViewById(R.id.serach_edit);
        mButton = (Button)findViewById(R.id.serach_button);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mAdapter = new TweetAdapter(mTweetTexts);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAdapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(networkInfo!=null && networkInfo.isConnected()){
                    String query = mEditText.getText().toString();
                    getQuery(query);
                }else{
                    Toast.makeText(MainActivity.this, "No network connectivity, yo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getQuery(String query){
        OkHttpClient client = new OkHttpClient();
        Headers header = new Headers.Builder()
                .add("Authorization","Bearer "+mAccessToken).build();
        Request request = new Request.Builder()
                .headers(header)
                .url(SEARCH_URL+query)
                .build();

        client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String data = response.body().string();
                    Gson gson = new Gson();
                    if(!response.isSuccessful()){
                        throw new IOException("WHAT JUST HAPPENED? YOU MADE A CALL BUT... IT DIDN'T WORK..."+response);
                    }
                    JSONArray tweetList = null;
                    try {
                        tweetList = new JSONArray(data);
                        for (int i = 0; i < 20; i++) {
                            TweetText tweet = gson.fromJson(tweetList.getJSONObject(i).toString(),TweetText.class);
                            mTweetTexts.add(tweet);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();

                        }
                    });
            }
        });
    }

    private void getBearerToken(){
        OkHttpClient client = new OkHttpClient();
        Headers header = new Headers.Builder()
                .add("Authorization","Basic "+BEARER_TOKEN_CREDENTIALS)
                .add("Content-Type","application/x-www-form-urlencoded;charset=UTF-8")
                .build();

        RequestBody body = new FormBody.Builder()
                .add("grant_type","client_credentials")
                .build();

        Request request = new Request.Builder().url(GET_BEARER).headers(header).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(!response.isSuccessful()){
                    throw new IOException("huh?");
                }
                try {
                    JSONObject jObject = new JSONObject(response.body().string());
                    mAccessToken = jObject.getString("access_token");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
