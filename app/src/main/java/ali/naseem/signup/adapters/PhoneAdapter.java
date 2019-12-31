package ali.naseem.signup.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ali.naseem.signup.R;
import ali.naseem.signup.models.ContactItem;

public class PhoneAdapter extends ArrayAdapter<ContactItem> {

    private Activity activity;
    private ArrayList<ContactItem> contacts;
    ContactItem contact = null;
    LayoutInflater inflater;

    public PhoneAdapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList<ContactItem> objects
    ) {
        super(activitySpinner, textViewResourceId, objects);
        activity = activitySpinner;
        contacts = objects;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.contact_list_item, parent, false);
//        if (position == 0) {
//            row = inflater.inflate(R.layout.contact_list_prompt, parent, false);
//        }
        contact = null;
        contact = contacts.get(position);
        TextView phone = row.findViewById(R.id.phone);

        if (position == 0) {
            // Default selected Spinner item (prompt)
            phone.setText("Phone");
            phone.setTextColor(Color.parseColor("#C4C4C4"));
        } else {
            phone.setText(contact.getPhone());
        }

        return row;
    }
}
