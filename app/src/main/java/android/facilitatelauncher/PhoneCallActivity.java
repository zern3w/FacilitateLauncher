package android.facilitatelauncher;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class PhoneCallActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvNumber;
    private Button btnOne, btnTwo, btnThree, btnFour, btnFive,
            btnSix, btnSeven, btnEight, btnNine, btnStar, btnZero, btnNumberSign;
    private Button btnClear, btnAddToContact, btnCall;
    private String result, contactName;
    private MediaPlayer mp;
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);

        initView();
        initInstance();
        initListener();
    }

    private void initView() {
        tvNumber = findViewById(R.id.tvNumber);
        btnOne = findViewById(R.id.btnOne);
        btnTwo = findViewById(R.id.btnTwo);
        btnThree = findViewById(R.id.btnThree);
        btnFour = findViewById(R.id.btnFour);
        btnFive = findViewById(R.id.btnFive);
        btnSix = findViewById(R.id.btnSix);
        btnSeven = findViewById(R.id.btnSeven);
        btnEight = findViewById(R.id.btnEight);
        btnNine = findViewById(R.id.btnNine);
        btnStar = findViewById(R.id.btnStar);
        btnZero = findViewById(R.id.btnZero);
        btnNumberSign = findViewById(R.id.btnNumberSign);
        btnClear = findViewById(R.id.btnClear);
        btnAddToContact = findViewById(R.id.btnAddToContact);
        btnCall = findViewById(R.id.btnCall);
    }

    private void initInstance() {
        result = "";
        contactName = "";
    }

    private void initListener() {
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnNumberSign.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnAddToContact.setOnClickListener(this);
        btnCall.setOnClickListener(this);

//        stop.setEnabled(false);
//        play.setEnabled(false);

//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
//
//        myAudioRecorder = new MediaRecorder();
//        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        myAudioRecorder.setOutputFile(outputFile);
//
//        btnStar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    myAudioRecorder.prepare();
//                    myAudioRecorder.start();
//                } catch (IllegalStateException ise) {
//                    // make something ...
//                } catch (IOException ioe) {
//                    // make something
//                }
//
////                record.setEnabled(false);
////                stop.setEnabled(true);
//
//                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        btnZero.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myAudioRecorder.stop();
//                myAudioRecorder.release();
//                myAudioRecorder = null;
////                record.setEnabled(true);
////                stop.setEnabled(false);
////                play.setEnabled(true);
//                Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        btnNumberSign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MediaPlayer mediaPlayer = new MediaPlayer();
//
//                try {
//                    mediaPlayer.setDataSource(outputFile);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//
//                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
//
//                } catch (Exception e) {
//                    // make something
//                }
//
//            }
//        });

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOne) {
            playSound(R.raw.num1);
            addNumber("1");
        } else if (v.getId() == R.id.btnTwo) {
            playSound(R.raw.num2);
            addNumber("2");
        } else if (v.getId() == R.id.btnThree) {
            playSound(R.raw.num3);
            addNumber("3");
        } else if (v.getId() == R.id.btnFour) {
            playSound(R.raw.num4);
            addNumber("4");
        } else if (v.getId() == R.id.btnFive) {
            playSound(R.raw.num5);
            addNumber("5");
        } else if (v.getId() == R.id.btnSix) {
            playSound(R.raw.num6);
            addNumber("6");
        } else if (v.getId() == R.id.btnSeven) {
            playSound(R.raw.num7);
            addNumber("7");
        } else if (v.getId() == R.id.btnEight) {
            playSound(R.raw.num8);
            addNumber("8");
        } else if (v.getId() == R.id.btnNine) {
            playSound(R.raw.num9);
            addNumber("9");
        } else if (v.getId() == R.id.btnZero) {
            playSound(R.raw.num0);
            addNumber("0");
        } else if (v.getId() == R.id.btnStar) {
            playSound(R.raw.star);
            addNumber("*");
        } else if (v.getId() == R.id.btnNumberSign) {
            playSound(R.raw.numbersign);
            addNumber("#");
        } else if (v.getId() == R.id.btnClear) {
            removeNumber();
        } else if (v.getId() == R.id.btnAddToContact) {
            if (result != null && result.length() == 0) {
                Toast.makeText(getApplicationContext(), "โปรดกรอกเบอร์โทรศัพท์", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("กรอกชื่อ");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    contactName = input.getText().toString();
                    addToContact(getApplicationContext().getContentResolver(), getApplicationContext());
                }
            });
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else if (v.getId() == R.id.btnCall) {
            makePhoneCall();
        }
    }

    private void playSound(int resId) {
        mp = MediaPlayer.create(this, resId);
        mp.start();
    }

    private void addNumber(String num) {
        result = new StringBuilder()
                .append(result)
                .append(num)
                .toString();
        tvNumber.setText(result);
    }

    private void removeNumber() {
        if (result != null && result.length() > 1) {
            result = result.substring(0, result.length() - 1);
        } else if (result != null && result.length() == 1) {
            result = "";
        }
        tvNumber.setText(result);
    }

    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + result));
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        }
    }

    private void addToContact(ContentResolver resolver, Context context) {
        String DisplayName = contactName;
        String MobileNumber = result;
//        String HomeNumber = "1111";
//        String WorkNumber = "2222";
//        String emailID = "email@nomail.com";
//        String company = "bad";
//        String jobTitle = "abcd";

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(context, "เพิ่มชื่อเข้ารายชื่อติดต่อเรียบร้อย", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
