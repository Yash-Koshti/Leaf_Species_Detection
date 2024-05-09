package com.leaf_lore.leaf_lore_frontend;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.leaf_lore.leaf_lore_frontend.researcher.ImageMapping;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    startActivity(new Intent(MainActivity.this, ImageMapping.class));

    finish();
  }
}