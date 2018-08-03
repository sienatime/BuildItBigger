package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import java.io.IOException;
import net.emojiparty.android.jokeactivity.JokeActivity;

public class JokeEndpointAsyncTask extends AsyncTask<Context, Void, Pair<Context, String>> {
  private static MyApi myApiService = null;

  @Override
  protected Pair<Context, String> doInBackground(Context... params) {
    if(myApiService == null) {  // Only do this once
      MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
          new AndroidJsonFactory(), null)
          // options for running against local devappserver
          // - 10.0.2.2 is localhost's IP address in Android emulator
          // - turn off compression when running against local devappserver
          .setRootUrl("http://10.0.2.2:8080/_ah/api/")
          .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws
                IOException {
              abstractGoogleClientRequest.setDisableGZipContent(true);
            }
          });
      // end options for devappserver

      myApiService = builder.build();
    }

    try {
      return new Pair<>(params[0], myApiService.tellJoke().execute().getData());
    } catch (IOException e) {
      return new Pair<>(params[0], e.getMessage());
    }
  }

  @Override
  protected void onPostExecute(Pair<Context, String> result) {
    Context context = result.first;
    Intent jokeIntent = new Intent(context, JokeActivity.class);
    jokeIntent.putExtra(JokeActivity.INTENT_JOKE_KEY, result.second);
    context.startActivity(jokeIntent);
  }
}
