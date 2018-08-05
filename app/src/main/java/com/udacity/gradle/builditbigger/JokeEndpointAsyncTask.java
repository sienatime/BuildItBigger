package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import java.io.IOException;

public class JokeEndpointAsyncTask extends AsyncTask<Void, Void, String> {
  private static MyApi myApi = null;
  public interface OnJokeLoaded {
    void success(String result);
  }
  private OnJokeLoaded callback;

  JokeEndpointAsyncTask(OnJokeLoaded callback) {
    this.callback = callback;
  }

  @Override
  protected String doInBackground(Void... params) {
    if(myApi == null) {
      myApi = initializeApi();
    }

    try {
      return myApi.tellJoke().execute().getData();
    } catch (IOException e) {
      return e.getMessage();
    }
  }

  @Override
  protected void onPostExecute(String result) {
    callback.success(result);
  }

  private MyApi initializeApi() {
    String emulatorLocalHost = "http://10.0.2.2:8080/";
    MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
        new AndroidJsonFactory(), null)
        .setRootUrl(emulatorLocalHost + "_ah/api/")
        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
          @Override
          public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws
              IOException {
            abstractGoogleClientRequest.setDisableGZipContent(true);
          }
        });

    return builder.build();
  }
}
