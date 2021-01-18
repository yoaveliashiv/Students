package com.code.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.code.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityFeedbackChat extends AppCompatActivity {
    private String type = "";
    private Boolean flag;
    private TextView textViewWarn;
    private EditText editText;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_chat);
        getSupportActionBar().setTitle("משוב");
        radioGroup = findViewById(R.id.radios);
        Button button = findViewById(R.id.button_send_feed);
        editText = findViewById(R.id.editText_feed_chat);
        flag = getIntent().getExtras().getBoolean("flag");
        if(flag){
            RadioButton radioButton=findViewById(R.id.radioButton_report);
            radioButton.setChecked(true);
        }
        textViewWarn = findViewById(R.id.textView_warn_feed);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int radioId = radioGroup.getCheckedRadioButtonId();
                if (radioId < 0) {
                    editText.setError("אנא סמן למעלה סוג משוב");
                    editText.requestFocus();
                    return;
                }
                RadioButton radioButtonNow = findViewById(radioId);
                type = radioButtonNow.getText().toString();

                String feedback = editText.getText().toString();
                if (feedback.isEmpty() || feedback.length() < 3) {
                    editText.setError("אנא הכנס משוב");
                    editText.requestFocus();
                    return;
                }
                datebase(feedback);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton_takla) {
                    textViewWarn.setVisibility(View.GONE);

                } else if (i == R.id.radioButton_report) {
                    if (!flag) {
                        textViewWarn.setVisibility(View.VISIBLE);
                        radioGroup.clearCheck();

                        return;
                    }
                } else if (i == R.id.radioButton_all) {
                    textViewWarn.setVisibility(View.GONE);
                }
            }
        });

    }

    private void datebase(String feedback) {
        FeedbackChat feedbackChat = new FeedbackChat();
        if (flag) {
            Message message = (Message) getIntent().getSerializableExtra("message");
            feedbackChat.setMessage(message);
            feedbackChat.setUidReportingOn(message.getUid());
            feedbackChat.setPhoneReportingOn(message.getPhone());
        }
        feedbackChat.setType(type);
        feedbackChat.setFeedback(feedback);
        feedbackChat.setUidReportingSend(FirebaseAuth.getInstance().getCurrentUser().getUid());
        feedbackChat.setPhoneReportingSend(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FeedbackChat")
                .child(type).push();
        feedbackChat.setKey(databaseReference.getKey());
        databaseReference.setValue(feedbackChat);


        Toast.makeText(ActivityFeedbackChat.this, "המשוב נשלח בהצלחה", Toast.LENGTH_SHORT).show();
       Intent intent = new Intent(ActivityFeedbackChat.this, MainActivity3.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_chat, menu);
        return true;

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.refresh);
        item.setTitle("לדף הראשי");

        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent2;
        switch (item.getItemId()) {

            case R.id.mainIconMenu:
                intent2 = new Intent(ActivityFeedbackChat.this, MainActivity3.class);
                startActivity(intent2);
                return true;

            case R.id.settingsMenu:
                intent2 = new Intent(ActivityFeedbackChat.this, ActivitySettings.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                return true;
            case R.id.feedbackMenu:
                intent2 = new Intent(ActivityFeedbackChat.this, ActivityFeedbackChat.class);
                intent2.putExtra("flag", false);
                startActivity(intent2);
                return true;
            case R.id.refresh:
                intent2 = new Intent(ActivityFeedbackChat.this, MainActivity3.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}