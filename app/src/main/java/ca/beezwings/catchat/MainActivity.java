package ca.beezwings.catchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Gets the user to sign-in
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    1
            );
        } else {


            // Start listing users from the beginning, 1000 at a time.
            ListUsersPage page = FirebaseAuth.getInstance().listUsersAsync(null).get();
            while (page != null) {
                for (ExportedUserRecord user : page.getValues()) {
                    System.out.println("User: " + user.getUid());
                }
                page = page.getNextPage();
            }


            // Iterate through all users. This will still retrieve users in batches,
            // buffering no more than 1000 users in memory at a time.
            page = FirebaseAuth.getInstance().listUsersAsync(null).get();
            for (ExportedUserRecord user : page.iterateAll()) {
                System.out.println("User: " + user.getUid());
            }
        }

        //    // Experimenting with multiple activities. Called when user swips left
//    public void onClick(View view){
//        Intent myIntent = new Intent(this, Main2Activity.class);
//        myIntent.putExtra(EXTRA_MESSAGE, inputMessage);
//        startActivity(myIntent);
//
//    }
    }
}
