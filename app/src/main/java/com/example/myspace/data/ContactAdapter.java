package com.example.myspace.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myspace.R;
import com.example.myspace.data.entity.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private Activity activity;
    private List<Contact> contacts;
    private static LayoutInflater inflater = null;

//    public static int selectedItemPosition = 100;

    public ContactAdapter(Activity activity, int textViewResourceId, List<Contact> contacts) {
        super(activity, textViewResourceId, contacts);
        try {
            this.activity = activity;
            this.contacts = contacts;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

//    public int getCount() {
//        return contacts.size();
//    }

    public Contact getItem(Contact contact) {
        return contact;
    }

    public long getItemId(int position) {
        return this.contacts.get(position).getId();
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_phone;
        public TextView display_email;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        final ViewHolder holder;

        try {
            if (convertView == null) {
                view = inflater.inflate(R.layout.contact_list_item, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) view.findViewById(R.id.name);
                holder.display_phone = (TextView) view.findViewById(R.id.phone);
                holder.display_email = (TextView) view.findViewById(R.id.email);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

//            view.setId(contacts.get(position).getId());
            holder.display_name.setText(contacts.get(position).getName());
            holder.display_phone.setText(contacts.get(position).getPhone());
            holder.display_email.setText(contacts.get(position).getEmail());

        } catch (Exception e) {

        }

        return view;
    }

}
