package net.emojiparty.android.jokeactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {
  public static final String INTENT_JOKE_KEY = "JOKE";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_joke);
    setTextViewWithJoke();
  }

  private void setTextViewWithJoke() {
    Intent intent = getIntent();
    String joke = intent.getStringExtra(INTENT_JOKE_KEY);
    if (joke != null) {
      TextView textView = findViewById(R.id.text_view_joke);
      textView.setText(joke);
    }
  }
}
