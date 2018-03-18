package android.facilitatelauncher.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.facilitatelauncher.OnSwipeTouchListener;
import android.facilitatelauncher.R;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.facilitatelauncher.activity.MenuChooserActivity.HANDICAP_TYPE;

public class PhoneCallActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etNumber;
    private Button btnOne, btnTwo, btnThree, btnFour, btnFive,
            btnSix, btnSeven, btnEight, btnNine, btnStar, btnZero, btnNumberSign;
    private Button btnClear, btnAddToContact, btnCall;
    private RelativeLayout rlContent;
    private TextView tvTitle;
    private String phoneNumber;
    private MediaPlayer mp;
    private boolean isNumberConfirmed = false;
    private int userType = -1;
    private TextWatcher textWatcher;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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
        getSupportActionBar().setTitle("โทรศัพท์");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        etNumber = findViewById(R.id.etNumber);
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
        rlContent = findViewById(R.id.rlContent);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void initInstance() {
        phoneNumber = "";
        Intent intent = getIntent();
        userType = intent.getIntExtra("USER_TYPE", -1);

        if (userType == HANDICAP_TYPE){
            playSound(R.raw.tap_talk);
            showKeyboard();
        }
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
        setconfirmNumberListener();

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int count, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll("\\s","");
                if (result.length() >= 10) {
                    rlContent.setVisibility(View.VISIBLE);
                    hideKeyboard();
                    etNumber.removeTextChangedListener(textWatcher);
                    etNumber.setText(result);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            confirmNumber();
                        }
                    },500);

                }
            }
        };

        if (userType == HANDICAP_TYPE) {
            etNumber.addTextChangedListener(textWatcher);
        }
    }

    private void setconfirmNumberListener() {
        rlContent.setOnTouchListener(new OnSwipeTouchListener(PhoneCallActivity.this) {
            @Override
            public void onClick() {
                super.onClick();
                // your on click here
            }

            @Override
            public void onDoubleClick() {
                super.onDoubleClick();
                if (!isNumberConfirmed) {
                    playSound(R.raw.confirm_number_menu);
                    isNumberConfirmed = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playSound(R.raw.confirm_calling);
                            tvTitle.setText("แตะสองครั้งเพื่อโทร \nหรือ สไลด์เพื่อบันทึก");
                        }
                    }, 1400);
                } else {
                    playSound(R.raw.calling);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            makePhoneCall();
                            finish();
                        }
                    }, 700);
                }

            }

            @Override
            public void onLongClick() {
                super.onLongClick();
                // your on onLongClick here
            }

            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                // your swipe up here
            }

            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                // your swipe down here.
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                onSwiped();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                onSwiped();
            }
        });
    }

    private void confirmNumber(){
        String number = etNumber.getText().toString();
        String[] separated = number.split("");
        List<String> numbers = Arrays.asList(separated);

        if (numbers.size() == 11) {
            for (int i = 0; i < numbers.size(); i++) {
                playConfirmNumber(numbers.get(i));
                try {
                    Thread.sleep(600);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (i == numbers.size() - 1) playSound(R.raw.confirm_number);
            }
        }
    }

    private void onSwiped() {
        if (!isNumberConfirmed) {
            rlContent.setVisibility(View.GONE);
            etNumber.setText("");
            etNumber.addTextChangedListener(textWatcher);
            phoneNumber = "";
            playSound(R.raw.menu_edit);
            showKeyboard();
        } else {
            playSound(R.raw.save);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), RecorderActivity.class);
                    intent.putExtra("PHONE", etNumber.getText().toString());
                    intent.putExtra("USER_TYPE", userType);
                    startActivity(intent);
                }
            }, 700);
        }
    }

    private void playConfirmNumber(String number) {
        switch (number) {
            case "0":
                playSound(R.raw.num0);
                break;
            case "1":
                playSound(R.raw.num1);
                break;
            case "2":
                playSound(R.raw.num2);
                break;
            case "3":
                playSound(R.raw.num3);
                break;
            case "4":
                playSound(R.raw.num4);
                break;
            case "5":
                playSound(R.raw.num5);
                break;
            case "6":
                playSound(R.raw.num6);
                break;
            case "7":
                playSound(R.raw.num7);
                break;
            case "8":
                playSound(R.raw.num8);
                break;
            case "9":
                playSound(R.raw.num9);
                break;
            default:
                playSound(R.raw.confirm_number_menu);
                try {
                    Thread.sleep(1200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
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
            if (phoneNumber.trim().equals("")) {
                Toast.makeText(getApplicationContext(), "โปรดกรอกเบอร์โทร!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getApplicationContext(), RecorderActivity.class);
                intent.putExtra("PHONE", etNumber.getText().toString());
                startActivity(intent);
            }
        } else if (v.getId() == R.id.btnCall) {
            makePhoneCall();
        }
    }

    private void playSound(int resId) {
        if (mp == null) {
            mp = new MediaPlayer();
        } else {
            mp.release();
            mp = new MediaPlayer();
        }
        mp = MediaPlayer.create(this, resId);
        mp.start();
    }

    private void addNumber(String num) {
        phoneNumber = new StringBuilder()
                .append(phoneNumber)
                .append(num)
                .toString();
        etNumber.setText(phoneNumber);
    }

    private void removeNumber() {
        if (phoneNumber != null && phoneNumber.length() > 1) {
            phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
        } else if (phoneNumber != null && phoneNumber.length() == 1) {
            phoneNumber = "";
        }
        etNumber.setText(phoneNumber);
    }

    private void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + etNumber.getText()));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) mp.release();
        hideKeyboard();
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}