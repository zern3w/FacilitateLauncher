package android.facilitatelauncher.activity;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.facilitatelauncher.R;
import android.facilitatelauncher.database.DatabaseHandler;
import android.facilitatelauncher.model.Contact;
import android.facilitatelauncher.util.Manager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecorderActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RequestPermissionCode = 1;
    private Button buttonStart, buttonStop, buttonPlayLastRecordAudio, btnSave;
    private EditText etName;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private String AudioSavePathInDevice = null;
    private MediaRecorder mediaRecorder;
    private Random random;
    private String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    private MediaPlayer mediaPlayer;
    private String phoneNumber;
    private boolean isPlayingRecord;
    private Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        initView();
        initListener();
        initInstance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        getSupportActionBar().setTitle("เพิ่มเข้าสมุดโทรศัพท์");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        etName = findViewById(R.id.etName);
        buttonStart = findViewById(R.id.btnStart);
        buttonStop = findViewById(R.id.btnStop);
        buttonPlayLastRecordAudio = findViewById(R.id.btnPlay);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setEnabled(false);
        buttonStop.setEnabled(false);
    }

    private void initListener() {
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonPlayLastRecordAudio.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initInstance() {
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("PHONE");
        random = new Random();
        isPlayingRecord = false;
        manager = new Manager();
    }

    private void addToContact(ContentResolver resolver, Context context) {
        String displayName = etName.getText().toString().trim();
        String mobileNumber = phoneNumber;
        Uri recorded = null;
        File record;
        if (AudioSavePathInDevice != null) record = new File(AudioSavePathInDevice);

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (displayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            displayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (mobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, ops);
            updateContactRingtone(mobileNumber);
            Contact contact = new Contact(manager.getContactIDFromNumber(getApplicationContext(), mobileNumber),
                    displayName,
                    AudioSavePathInDevice,
                    mobileNumber);
            addContactToDatabase(contact);
            Toast.makeText(context, "เพิ่มชื่อเข้ารายชื่อติดต่อเรียบร้อย", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "เกิดข้อผิดพลาด: ", Toast.LENGTH_SHORT).show();
        }
    }

    private void addContactToDatabase(Contact contact) {
        DatabaseHandler db = new DatabaseHandler(this);

        db.addContact(contact);

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            String log = "Id: " + cn.getContactId() +
                    " ,Name: " + cn.getName() + " " +
                    ",Phone: " + cn.getPhoneNumber() +
                    ",Source: " + cn.getSource();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }

    private void stopRecord() {
        mediaRecorder.stop();
        buttonStop.setEnabled(false);
        buttonPlayLastRecordAudio.setEnabled(true);
        buttonStart.setEnabled(true);
        if (!etName.getText().toString().trim().equals("")) btnSave.setEnabled(true);

        Toast.makeText(getApplicationContext(), "การบันทึกเสียงเสร็จสิ้น",
                Toast.LENGTH_LONG).show();
    }

    private void startRecord() {
        hideKeyboard();
        if (checkPermission()) {
            String name = etName.getText().toString().trim();
            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            name + "_" + CreateRandomAudioFileName(5) + ".3gp";

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);

            Toast.makeText(getApplicationContext(), "เริ่มการบันทึกเสียง",
                    Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
    }

    private void playLastRecord() {
        if (isPlayingRecord) {
            buttonPlayLastRecordAudio.setText("เล่นเสียงที่อัด");
            buttonStop.setEnabled(false);
            buttonStart.setEnabled(true);
            buttonPlayLastRecordAudio.setEnabled(true);

            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                MediaRecorderReady();
            }
            isPlayingRecord = false;
            return;
        }

        buttonPlayLastRecordAudio.setText("หยุดการเล่น");
        isPlayingRecord = true;
        buttonStop.setEnabled(false);
        buttonStart.setEnabled(false);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AudioSavePathInDevice);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
        Toast.makeText(getApplicationContext(), "กำลังเล่น ...",
                Toast.LENGTH_LONG).show();

    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    public void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateContactRingtone(String num) {
        // The Uri used to look up a contact by phone number
        final Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, num);
        // The columns used for `Contacts.getLookupUri`
        final String[] projection = new String[]{
                ContactsContract.Contacts._ID, ContactsContract.Contacts.LOOKUP_KEY
        };
        // Build your Cursor
        final Cursor data = getContentResolver().query(lookupUri, projection, null, null, null);
        data.moveToFirst();
        try {
            // Get the contact lookup Uri
            final long contactId = data.getLong(0);
            final String lookupKey = data.getString(1);
            final Uri contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
            if (contactUri == null) {
                // Invalid arguments
                return;
            }

            // Get the path of ringtone you'd like to use
            // final String storage = Environment.getExternalStorageDirectory().getPath();
            final File file = new File(AudioSavePathInDevice);
            final String value = Uri.fromFile(file).toString();

            // Apply the custom ringtone
            final ContentValues values = new ContentValues(1);
            values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, value);
            getContentResolver().update(contactUri, values, null, null);
        } finally {
            // Don't forget to close your Cursor
            data.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStart) {
            startRecord();
        } else if (v.getId() == R.id.btnStop) {
            stopRecord();
        } else if (v.getId() == R.id.btnPlay) {
            playLastRecord();
        } else if (v.getId() == R.id.btnSave) {
            addToContact(this.getContentResolver(), getApplicationContext());
        }
    }
}
