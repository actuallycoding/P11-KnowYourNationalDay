package sg.edu.rp.a15071484.p11_knowyournationalday;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.id.message;

public class MainActivity extends AppCompatActivity {
    EditText etAccessCode;
    RadioGroup rg1, rg2, rg3;
    ListView lv;
    ArrayList<String> al;
    ArrayAdapter<String> aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ListView
        lv = (ListView) findViewById(R.id.lv);
        al = new ArrayList<String>();
        al.add("Singapore National Day is on 9 Aug");
        al.add("Singapore is 52 years old");
        al.add("Theme is '#OneNationTogether'");
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean loggedIn = pref.getBoolean("correct", false);
        if (loggedIn == false) {
            //Dialog for the login
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout accessCode =
                    (LinearLayout) inflater.inflate(R.layout.access, null);
            etAccessCode = (EditText) accessCode
                    .findViewById(R.id.editTextAccessCode);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Login")
                    .setView(accessCode)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String code = etAccessCode.getText().toString();
                            if (code.equalsIgnoreCase("738964")) {
                                pref.edit().putBoolean("correct", true).commit();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getBaseContext(), "Wrong access code!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }
                    });
            builder.setNegativeButton("NO ACCESS CODE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getBaseContext(), "If you are keen to access, please contact ABC Pte Ltd!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Tally against the respective action item clicked
        //  and implement the appropriate action
        if (item.getItemId() == R.id.action_quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quit?")
                    // Set text for the positive button and the corresponding
                    //  OnClickListener when it is clicked
                    .setPositiveButton("QUIT", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            pref.edit().putBoolean("correct", false).commit();
                            finish();
                        }
                    })
                    // Set text for the negative button and the corresponding
                    //  OnClickListener when it is clicked
                    .setNegativeButton("NOT REALLY", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.action_send) {
            String[] list = new String[]{"SMS", "Email"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select the way to enrich your friend")
                    // Set the list of items easily by just supplying an
                    //  array of the items
                    .setItems(list, new DialogInterface.OnClickListener() {
                        // The parameter "which" is the item index
                        // clicked, starting from 0
                        public void onClick(DialogInterface dialog, int which) {
                            String message = "Did you know? ";
                            for (int i = 0; i < al.size(); i++) {
                                message += al.get(i) + "\n";
                            }
                            if (which == 0) {
                                Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
                                smsIntent.setType("vnd.android-dir/mms-sms");
                                smsIntent.putExtra("sms_body", message);
                                startActivity(smsIntent);

                            } else {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                // Put essentials like email address, subject & body text
                                email.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{""});
                                email.putExtra(Intent.EXTRA_SUBJECT,
                                        "");
                                email.putExtra(Intent.EXTRA_TEXT, message);
                                // This MIME type indicates email
                                email.setType("message/rfc822");
                                // createChooser shows user a list of app that can handle
                                // this MIME type, which is, email
                                startActivity(Intent.createChooser(email,
                                        "Choose an Email client :"));
                            }
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (item.getItemId() == R.id.action_quiz) {
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final LinearLayout quiz =
                    (LinearLayout) inflater.inflate(R.layout.quiz, null);
            rg1 = (RadioGroup) quiz.findViewById(R.id.rg1);
            rg2 = (RadioGroup) quiz.findViewById(R.id.rg2);
            rg3 = (RadioGroup) quiz.findViewById(R.id.rg3);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Test yourself!")
                    .setView(quiz)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            int score = 0;
                            int id1 = rg1.getCheckedRadioButtonId();
                            RadioButton rb1 = (RadioButton) quiz.findViewById(id1);
                            int id2 = rg2.getCheckedRadioButtonId();
                            RadioButton rb2 = (RadioButton) quiz.findViewById(id2);
                            int id3 = rg3.getCheckedRadioButtonId();
                            RadioButton rb3 = (RadioButton) quiz.findViewById(id3);
                            if (rb1.getText().toString().equalsIgnoreCase("No")) {
                                score += 1;
                            }
                            if (rb2.getText().toString().equalsIgnoreCase("Yes")) {
                                score += 1;
                            }
                            if (rb3.getText().toString().equalsIgnoreCase("Yes")) {
                                score += 1;
                            }
                            Toast.makeText(getBaseContext(), "Your total score is " + score + "/3!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton("DON'T KNOW LAH", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
