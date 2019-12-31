package ali.naseem.signup;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ali.naseem.signup.adapters.PhoneAdapter;
import ali.naseem.signup.models.ContactItem;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 79;
    private ArrayList<ContactItem> contacts = new ArrayList<>();
    private ArrayList<ContactItem> list = new ArrayList<>();
    private PhoneAdapter adapter;
    private EditText fname;
    private EditText lname;
    private EditText email;
    private EditText pass;
    private EditText cpass;
    private Spinner phoneNumbers;
    private String vphone;
    private String vfname;
    private String vlname;
    private View submitButton, phoneLabel;
    private TextView emailLabel;
    private ProgressBar strength;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        list.add(0, new ContactItem("Phone", "Phone", "Phone"));
        init();
        checkPermission();
        attachListeners();
    }

    private void init() {
        fname = findViewById(R.id.fnameEditText);
        lname = findViewById(R.id.lnameEditText);
        email = findViewById(R.id.emailEditText);
        pass = findViewById(R.id.passwordEditText);
        cpass = findViewById(R.id.cpasswordEditText);
        phoneNumbers = findViewById(R.id.phoneNumbers);
        submitButton = findViewById(R.id.submitButton);
        strength = findViewById(R.id.strength);
        phoneLabel = findViewById(R.id.phoneLabel);
        emailLabel = findViewById(R.id.emailLabel);
        adapter = new PhoneAdapter(this, android.R.layout.simple_spinner_dropdown_item, list);
        phoneNumbers.setAdapter(adapter);
        pass.setTransformationMethod(new PasswordTransformationMethod());
        cpass.setTransformationMethod(new PasswordTransformationMethod());
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            contacts.addAll(Utils.getInstance().getAllContacts(this));
            if (adapter != null)
                adapter.notifyDataSetChanged();
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contacts.addAll(Utils.getInstance().getAllContacts(this));
                    adapter.notifyDataSetChanged();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void attachListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    Toast.makeText(MainActivity.this, "All Valid. You can now proceed to main screen", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please verify that you have entered correct details.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pass.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    strength.setVisibility(View.VISIBLE);
                    int rank = Utils.getInstance().getRank(s.toString()) * 20;
                    DrawableCompat.setTint(strength.getProgressDrawable(), Color.parseColor(Utils.getInstance().toColor(rank)));
                    strength.setProgress(rank);
                } else {
                    strength.setVisibility(View.GONE);
                    DrawableCompat.setTint(strength.getProgressDrawable(), Color.parseColor("#e3e3e3"));
                    strength.setProgress(0);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        phoneNumbers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                ContactItem selectItem = (ContactItem) parent.getSelectedItem();
                if (selectItem != null) {
                    vphone = selectItem.getPhone();
                    if (vphone.equals("Phone")) phoneLabel.setVisibility(View.GONE);
                    else phoneLabel.setVisibility(View.VISIBLE);
                } else {
                    vphone = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                vphone = null;
            }

        });
        fname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    vfname = s.toString();
                    list.clear();
                    list.addAll(Utils.getInstance().filtered(contacts, vfname, vlname == null ? vfname : vlname));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        lname.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    vlname = s.toString();
                    list.clear();
                    list.addAll(Utils.getInstance().filtered(contacts, vfname == null ? vlname : vfname, vlname));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        final String[] domains = getResources().getStringArray(R.array.domains);
        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    emailLabel.setVisibility(View.VISIBLE);
                    String vemail = email.getText().toString().trim();
                    boolean matches = false;
                    vemail = vemail.substring(vemail.indexOf("@") + 1);
                    for (String domain : domains) {
                        if (domain.matches(vemail)) {
                            matches = true;
                            break;
                        }
                    }
                    if (matches) emailLabel.setText("Personal");
                    else {
                        if (Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches())
                            emailLabel.setText("Business");
                        else emailLabel.setVisibility(View.GONE);
                    }
                } else {
                    emailLabel.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
    }

    private boolean isValid() {
        String vemail = email.getText().toString();
        String vpass = pass.getText().toString();
        String vcpass = cpass.getText().toString();
        String allowed = "^[A-Za-z]+$";
        boolean valid = true;
        if (!Patterns.EMAIL_ADDRESS.matcher(vemail).matches() || TextUtils.isEmpty(vemail)) {
            email.setError("Invalid email");
            valid = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(vpass)) {
            pass.setError("Invalid password");
            valid = false;
        } else {
            pass.setError(null);
        }

        if (!TextUtils.equals(vcpass, vpass)) {
            cpass.setError("Passwords don't match");
            valid = false;
        } else {
            cpass.setError(null);
        }
        if (TextUtils.isEmpty(vfname) || !Utils.getInstance().isProfanityFree(vfname) || !vfname.matches(allowed)) {
            fname.setError("Should be profanity free, have no digits, no special symbols");
            valid = false;
        } else {
            fname.setError(null);
        }
        if (TextUtils.isEmpty(vlname) || !Utils.getInstance().isProfanityFree(vlname) || !vlname.matches(allowed)) {
            lname.setError("Should be profanity free, have no digits, no special symbols");
            valid = false;
        } else {
            lname.setError(null);
        }

        if (vphone == null || TextUtils.isEmpty(vphone) || TextUtils.equals(vphone, "Phone")) {
            valid = false;
        }

        return valid;
    }
}
