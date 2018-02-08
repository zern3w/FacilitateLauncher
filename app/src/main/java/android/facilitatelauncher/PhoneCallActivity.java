package android.facilitatelauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PhoneCallActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvNumber;
    private Button btnOne, btnTwo, btnThree, btnFour, btnFive,
            btnSix, btnSeven, btnEight, btnNine, btnStar, btnZero, btnNumberSign;
    private Button btnClear, btnAddToContact, btnCall;
    private String phoneNumber;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);

        initView();
        initInstance();
        initListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        getSupportActionBar().setTitle("สมุดโทรศัพท์");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
        phoneNumber = "";
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
            if (phoneNumber.trim().equals("")){
                Toast.makeText(getApplicationContext(), "โปรดกรอกเบอร์โทร!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), RecorderActivity.class);
                intent.putExtra("PHONE", phoneNumber);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.btnCall) {
            makePhoneCall();
        }
    }

    private void playSound(int resId) {
        mp = MediaPlayer.create(this, resId);
        mp.start();
    }

    private void addNumber(String num) {
        phoneNumber = new StringBuilder()
                .append(phoneNumber)
                .append(num)
                .toString();
        tvNumber.setText(phoneNumber);
    }

    private void removeNumber() {
        if (phoneNumber != null && phoneNumber.length() > 1) {
            phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
        } else if (phoneNumber != null && phoneNumber.length() == 1) {
            phoneNumber = "";
        }
        tvNumber.setText(phoneNumber);
    }

    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
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
}