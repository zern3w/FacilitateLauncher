package android.facilitatelauncher.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.facilitatelauncher.model.Contact;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by puttipongtadang on 1/31/18.
 */

public class Manager {

    public List<Contact> getAddressBookContact(ContentResolver resolver) {

        final String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        };

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        int count = 0;
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, "HAS_PHONE_NUMBER > 0", null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            Contact contact = new Contact();
                            contact.setContactId(cursor.getString(cursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                            contact.setPhoneId(cursor.getString(cursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)));
                            contact.setName(cursor.getString(cursor
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));

//                            String numberFormatted = number.replaceAll("\\D", "");
//                            if (number.startsWith("+")) {
//                                numberFormatted = "+" + numberFormatted;
//                            }
//
//                            if (!numberFormatted.isEmpty()) {
                            contact.setNumber(number);
                            contacts.add(contact);
//                            }
                            count++;
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    Log.e("Manager", e.getLocalizedMessage());
                } finally {
                    cursor.close();
                }
            }
        }
        return contacts;
    }

}
