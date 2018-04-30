package ca.beezwings.catchat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;


import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class SecondActivity extends AppCompatActivity {

    public FloatingActionButton sendMessage;
    public EditText inputMessage;
    public ListView listMessage;
    public FirebaseListAdapter<Message> adapter;
    public TextView msgView;
    public TextView userName;
    public TextView msgDateStamp;
    public ImageView msgCatImage;
    public Random RandomNumber;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);




        // Text Views and Floating Action Item
        sendMessage = findViewById(R.id.send_message);
        inputMessage = findViewById(R.id.text_input);
        listMessage = findViewById(R.id.chat_messages);


        // Random Cat Images, yay! This was an anticipated feature request by Carolyn
        final int catImages[] = {
                R.drawable.dinah_1,
                R.drawable.dinah_2,
                R.drawable.dinah_3,
                R.drawable.dinah_4,
                R.drawable.dinah_5,
                R.drawable.dinah_6,
                R.drawable.dinah_7,
                R.drawable.dinah_8,
                R.drawable.dinah_9,
                R.drawable.dinah_10,
                R.drawable.dinah_11,
                R.drawable.dinah_12
        };


        // Gets the text that has been sent to display in the ListView
        // This code was found online and is updated based on firebase database 3.0.0 and higher
        Query query = FirebaseDatabase.getInstance().getReference();
        FirebaseListOptions<Message> options =
                new FirebaseListOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .setLayout(R.layout.message)
                        .build();
        adapter = new FirebaseListAdapter<Message>(options){

            protected void populateView(View v, Message model, int position) {

                // This selects the TextView we created in our second layout.
                msgView = v.findViewById(R.id.message_view);
                msgView.setText(model.getMessageText());
                userName = v.findViewById(R.id.msg_user_name);
//                String nameAndDate = model.getProfileName() + " on " + (DateFormat.format("EEE, MMM dd yyyy, HH:mm", model.getDateStamp()));
                String profileNameExt =(model.getProfileName() + ": ");
                userName.setText(profileNameExt);
                msgDateStamp = v.findViewById(R.id.dateStamp);
                msgDateStamp.setText(DateFormat.format("MMM dd, HH:mm", model.getDateStamp()));
                System.out.println("It works!");


                // Makes a random cat appear
                msgCatImage = v.findViewById(R.id.catImage);
                msgCatImage.setImageResource(catImages[model.getRandomGenNumber()]);
            }
        };

        // Gets the user to sign-in
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    1
            );
        }
          else {
//             Show the messages
            showMessages();

            // Hide the soft keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputMessage.getWindowToken(), 0);

            // Scroll to the bottom:
            scrollMyListViewToBottom();
        }


        // onClick listener for when user hits "send" action button
        sendMessage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                // Get text from EditText and save as a string variable
                String inputMessagetoDB = inputMessage.getText().toString();

                // Makes a random number
                RandomNumber = new Random();
                int randomIndex = RandomNumber.nextInt(catImages.length);

                // Send that text to the DB
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new Message(inputMessagetoDB,FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), randomIndex));

//                showMessages();

                // Clears the text from the EditText, ie sets it to an empty string
                inputMessage.setText("");

                // Scroll to the bottom:
                scrollMyListViewToBottom();

                // Hide the soft keyboard after message is sent
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputMessage.getWindowToken(), 0);



            }
        });



    }





    //Code from Eric Lortie March 21, 2018 and edited/updated by JL
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if(reqCode == 1) {

            if(resCode == RESULT_OK) {

                Toast successToast = Toast.makeText(this, "Welcome to Cat Chat!", Toast.LENGTH_LONG);
                successToast.show();
//                showMessages();
            } else {

                Toast failureToast = Toast.makeText(this, "Login failed!", Toast.LENGTH_LONG);
                failureToast.show();
            }
        }
    }

    public void showMessages(){

        listMessage.setAdapter(adapter);
    }

    private void scrollMyListViewToBottom() {
        listMessage.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listMessage.setSelection(listMessage.getCount() - 1);
            }
        });
    }
}
