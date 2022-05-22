package com.example.practice_1_hrachov;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondaryActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "extra.REPLY";
    private EditText mReply;

    private TextView mTextView1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        mTextView1 = findViewById(R.id.var1);

        Intent intent = getIntent();

        String message1 = intent.getStringExtra(MainActivity.EXTRA_MESSAGE1);
        mTextView1.setText(message1);



        mReply = findViewById(R.id.valueToMain);
    }


    public void returnToMainActivity(View view) {
        String reply = mReply.getText().toString();
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, reply);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

}
