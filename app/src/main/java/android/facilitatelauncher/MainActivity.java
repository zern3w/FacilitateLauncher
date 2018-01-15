package android.facilitatelauncher;

import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.android.gms.actions.NoteIntents;

import java.util.Calendar;

import static android.content.Intent.CATEGORY_APP_MUSIC;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button btnAlarmClock, btnCamera;
    private Button btnCalculator, btnCalendar;
    private Button btnContactBook, btnGallery;
    private Button btnMusicPlayer, btnNote;
    private Button btnPhoneCall, btnRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        intiListener();
    }

    private void initView() {
        btnAlarmClock = findViewById(R.id.btnAlarmClock);
        btnCamera = findViewById(R.id.btnCamera);
        btnCalculator = findViewById(R.id.btnCalculator);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnContactBook = findViewById(R.id.btnContactBook);
        btnGallery = findViewById(R.id.btnGallery);
        btnMusicPlayer = findViewById(R.id.btnMusicPlayer);
        btnNote = findViewById(R.id.btnNote);
        btnPhoneCall = findViewById(R.id.btnPhoneCall);
        btnRecorder = findViewById(R.id.btnRecorder);
    }

    private void intiListener() {
        btnAlarmClock.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnCalculator.setOnClickListener(this);
        btnCalendar.setOnClickListener(this);
        btnContactBook.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnMusicPlayer.setOnClickListener(this);
        btnNote.setOnClickListener(this);
        btnPhoneCall.setOnClickListener(this);
        btnRecorder.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAlarmClock:
                btnAlarmClockClicked();
                break;
            case R.id.btnCamera:
                btnCameraClicked();
                break;
            case R.id.btnCalculator:
                btnCalculatorClicked();
                break;
            case R.id.btnCalendar:
                btnCalendarClicked();
                break;
            case R.id.btnContactBook:
                btnContactBookClicked();
                break;
            case R.id.btnGallery:
                btnGalleryClicked();
                break;
            case R.id.btnMusicPlayer:
                btnMusicPlayerClicked();
                break;
            case R.id.btnNote:
                btnNoteClicked();
                break;
            case R.id.btnPhoneCall:
                btnPhoneCallClicked();
                break;
            case R.id.btnRecorder:
                btnRecorderClicked();
                break;
            default:
                break;
        }
    }

    private void btnAlarmClockClicked() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, R.style.HoloDialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, final int selectedHour, final int selectedMinute) {
                String selectedTime = Helper.intToString(selectedHour, 2) + ":" + Helper.intToString(selectedMinute, 2);
                String dialogMessage = getString(R.string.alarm_confirm_dialog_message, selectedTime);

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.alarm_confirm_dialog_title)
                        .setMessage(dialogMessage)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                createAlarm(selectedHour, selectedMinute);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void btnCameraClicked() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnCalculatorClicked() {
        Intent intent = new Intent();
        intent.setClassName("com.android.calculator2", "com.android.calculator2.Calculator");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void btnCalendarClicked() {
        Intent intent = new Intent();
        ComponentName cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
        intent.setComponent(cn);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnContactBookClicked() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnGalleryClicked() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnMusicPlayerClicked() {
        Intent intent = new Intent(CATEGORY_APP_MUSIC);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnNoteClicked() {
        Intent intent = new Intent(NoteIntents.ACTION_CREATE_NOTE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnPhoneCallClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnRecorderClicked() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void createAlarm(int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
