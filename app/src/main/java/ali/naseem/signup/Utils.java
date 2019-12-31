package ali.naseem.signup;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;

import ali.naseem.signup.models.ContactItem;

public class Utils {

    private static final Utils ourInstance = new Utils();
    private String[] words;

    public static Utils getInstance() {
        return ourInstance;
    }

    public void initialize(Context context) {
        words = context.getResources().getStringArray(R.array.bad_words);
    }

    private Utils() {
    }

    public String toColor(int perc) {
        int r, g, b = 0;
        if (perc < 50) {
            r = 255;
            g = Math.round(5.1f * perc);
        } else {
            g = 255;
            r = Math.round(510 - 5.10f * perc);
        }
        int h = r * 0x10000 + g * 0x100 + b * 0x1;
        StringBuilder sb = new StringBuilder();
        String hex = Integer.toHexString(h);
        for (int i = 0; i < 6 - hex.length(); ++i) {
            sb.append(0);
        }
        sb.append(hex);
        return '#' + sb.toString();
    }

    public int getRank(String pass) {
        String upper = "^.*[A-Z].*$"; // For checking if has uppercase letters
        String specialChar = "^.*[!@#$&*].*$"; //For checking special chars
        String digit = "^.*[0-9].*$"; // For checking if has digits
        String lower = "^.*[a-z].*$"; // For checking if has lowercase letters
        String len = "^.{7,20}$"; // For checking if has length betwen {min,max}
        int rank = 0;
        if (pass.matches(upper)) {
            rank++;
        }
        if (pass.matches(lower)) {
            rank++;
        }
        if (pass.matches(len)) {
            rank++;
        }
        if (pass.matches(digit)) {
            rank++;
        }
        if (pass.matches(specialChar)) {
            rank++;
        }
        return rank;
    }

    public ArrayList<ContactItem> filtered(ArrayList<ContactItem> contacts, String fname, String lname) {
        ArrayList<ContactItem> items = new ArrayList<>();
        if (fname == null && lname == null) return items;
        for (ContactItem item : contacts) {
            if (fname != null) {
                if (!TextUtils.isEmpty(fname.trim()) && fname.trim().length() > 2)
                    if (item.getFirstName().toLowerCase().contains(fname.toLowerCase())) {
                        items.add(item);
                    }
            }
            if (lname != null) {
                if (!TextUtils.isEmpty(lname.trim()) && lname.trim().length() > 2)
                    if (item.getLastName().toLowerCase().contains(lname.toLowerCase())) {
                        items.add(item);
                    }
            }
        }
        items.add(0, new ContactItem("Phone", "Phone", "Phone"));
        return items;
    }

    public ArrayList<ContactItem> getAllContacts(Activity context) {
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        ArrayList<ContactItem> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (phone != null)
                contacts.add(new ContactItem(name, name, phone));
        }
        cursor.close();
        return contacts;
    }

    public boolean isProfanityFree(String wd) {
        for (String word : words) {
            if (wd.equalsIgnoreCase(word)) return false;
        }
        return true;
    }
}
