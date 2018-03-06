package android.facilitatelauncher.activity;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.facilitatelauncher.R;
import android.facilitatelauncher.util.Helper;
import android.facilitatelauncher.util.MenuConstant;
import android.facilitatelauncher.view.ClickableViewPager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import me.crosswall.lib.coverflow.core.CoverTransformer;
import me.crosswall.lib.coverflow.core.PagerContainer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.facilitatelauncher.activity.MenuChooserActivity.ELDER_TYPE;
import static android.facilitatelauncher.util.Helper.hasPermissions;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int userType = -1;
    private int positionClicked;
    private TextView tvTitle, tvClicked;
    private int menuCount = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intiActionBar();
        setContentView(R.layout.activity_main);

        initInstance();
        initView();
        initListener();
        initViewPager();

        requestPermission();
    }

    private void intiActionBar() {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void initInstance() {
        Intent intent = getIntent();
        userType = intent.getIntExtra("USER_TYPE", -1);
        positionClicked = 0;
    }

    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvClicked = findViewById(R.id.tvClicked);
        tvTitle.setText("โทรฉุกเฉิน");
    }

    private void initListener() {
        tvClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClicked(positionClicked);
            }
        });

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClicked(positionClicked);
            }
        });
    }

    private void initViewPager() {
        final ViewPager bindingPager = findViewById(R.id.pager);
        final PagerContainer mContainer = findViewById(R.id.pagerContainer);
        final ClickableViewPager pager = (ClickableViewPager) mContainer.getViewPager();

        PagerAdapter adapter = new MyPagerAdapter();
        menuCount = adapter.getCount();
        bindingPager.setAdapter(adapter);
        bindingPager.setOffscreenPageLimit(adapter.getCount());

        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setPageTransformer(false, new CoverTransformer(0.3f, -60f, 0f, 0f));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int index = 0;

            @Override
            public void onPageSelected(int position) {
                index = position;
                positionClicked = position;
                tvTitle.setText(getTitle(position));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int width = bindingPager.getWidth();
                bindingPager.scrollTo((int) (width * position + width * positionOffset), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    bindingPager.setCurrentItem(index);
                }

            }
        });
    }

    private void requestPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private String getTitle(int position) {
        String title = "";
        if (position == menuCount-1) {
            title = "การตั้งค่า";
            return title;
        }
        if (position == MenuConstant.EMERGENCY) {
            title = "โทรฉุกเฉิน";
        } else if (position == MenuConstant.PHONE_CALL) {
            title = "โทรศัพท์";
        } else if (position == MenuConstant.ADDRESS_BOOK) {
            title = "รายชื่อผู้ติดต่อ";
        } else if (position == MenuConstant.ALARM_CLOCK) {
            title = "นาฬิกาปลุก";
        } else if (position == MenuConstant.CALENDAR) {
            title = "ปฏิทิน";
        } else if (position == MenuConstant.CALCULATOR) {
            title = "เครื่องคิดเลข";
        } else if (position == MenuConstant.CAMERA) {
            title = "กล้อง";
        } else if (position == MenuConstant.GALLERY) {
            title = "คลังภาพ";
        } else if (position == MenuConstant.MUSIC_PLAYER) {
            title = "เครื่องเล่นเพลง";
        } else if (position == MenuConstant.RECORDER) {
            title = "เครื่องอัดเสียง";
        }
        return title;
    }

    private void performClicked(int positionClicked) {
        if (positionClicked == menuCount-1) {
            btnSettingCLicked();
            return;
        }
        if (positionClicked == MenuConstant.EMERGENCY) {
            btnEmergencyClicked();
        } else if (positionClicked == MenuConstant.PHONE_CALL) {
            btnPhoneCallClicked();
        } else if (positionClicked == MenuConstant.ADDRESS_BOOK) {
            btnContactBookClicked();
        } else if (positionClicked == MenuConstant.ALARM_CLOCK) {
            btnAlarmClockClicked();
        } else if (positionClicked == MenuConstant.CALENDAR) {
            btnCalendarClicked();
        } else if (positionClicked == MenuConstant.CALCULATOR) {
            btnCalculatorClicked();
        } else if (positionClicked == MenuConstant.CAMERA) {
            btnCameraClicked();
        } else if (positionClicked == MenuConstant.GALLERY) {
            btnGalleryClicked();
        } else if (positionClicked == MenuConstant.MUSIC_PLAYER) {
            btnMusicPlayerClicked();
        } else if (positionClicked == MenuConstant.RECORDER) {
            btnRecorderClicked();
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
        }, hour, minute, true);
        mTimePicker.setTitle("เลือกเวลา");
        mTimePicker.show();
    }

    private void btnCameraClicked() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showSnackBar("ไม่มีแอพกล้อง");
        }
    }

    private void btnCalculatorClicked() {
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        final PackageManager pm = getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs) {
            if (pi.packageName.toString().toLowerCase().contains("calcul")) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("appName", pi.applicationInfo.loadLabel(pm));
                map.put("packageName", pi.packageName);
                items.add(map);
            }
        }
        if (items.size() >= 1) {
            String packageName = (String) items.get(0).get("packageName");
            Intent i = pm.getLaunchIntentForPackage(packageName);
            if (i != null)
                startActivity(i);
        } else {
            showSnackBar("ไม่มีแอพเครื่องคิดเลข");
        }
    }


    private void btnCalendarClicked() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showSnackBar("ไม่มีแอพปฏิทิน");
        }
    }

    private void btnContactBookClicked() {
        Intent intent = new Intent(getApplicationContext(), AddressBookActivity.class);
        startActivity(intent);
    }

    private void btnEmergencyClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยืนยันการโทร");
        builder.setMessage("คุณต้องการที่จะโทรฉุกเฉิน?");
        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                emergencyCall();
            }
        });
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void btnGalleryClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showSnackBar("ไม่มีแอพคลังภาพ");
        }
    }

    private void btnMusicPlayerClicked() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showSnackBar("ไม่มีแอพเครื่องเล่นเพลง");
        }
    }

    private void btnPhoneCallClicked() {
        Intent intent = new Intent(this, PhoneCallActivity.class);
        startActivity(intent);
    }

    private void btnRecorderClicked() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showSnackBar("ไม่มีแอพเครื่องบันทึกเสียง");
        }
    }

    private void btnSettingCLicked() {
        Intent intent = new Intent(this, MenuChooserActivity.class);
        startActivity(intent);
    }

    private void emergencyCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + getString(R.string.emergency_number)));
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }

    private void createAlarm(int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showSnackBar(String msg){
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(MainActivity.this);
            if (position == MenuConstant.EMERGENCY) {
                view.setBackgroundResource(R.drawable.ic_emergency);
            } else if (position == MenuConstant.ALARM_CLOCK) {
                view.setBackgroundResource(R.drawable.ic_alarm_clock);
            } else if (position == MenuConstant.CAMERA) {
                view.setBackgroundResource(R.drawable.ic_camera);
            } else if (position == MenuConstant.CALCULATOR) {
                view.setBackgroundResource(R.drawable.ic_calculator);
            } else if (position == MenuConstant.CALENDAR) {
                view.setBackgroundResource(R.drawable.ic_calendar);
            } else if (position == MenuConstant.ADDRESS_BOOK) {
                view.setBackgroundResource(R.drawable.ic_address_book);
            } else if (position == MenuConstant.GALLERY) {
                view.setBackgroundResource(R.drawable.ic_gallery);
            } else if (position == MenuConstant.MUSIC_PLAYER) {
                view.setBackgroundResource(R.drawable.ic_music_player);
            } else if (position == MenuConstant.PHONE_CALL) {
                view.setBackgroundResource(R.drawable.ic_phone);
            } else if (position == MenuConstant.RECORDER) {
                view.setBackgroundResource(R.drawable.ic_recorder);
            }
            if (position == getCount()-1) {
                view.setBackgroundResource(R.drawable.ic_settings);
            }

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if (userType == ELDER_TYPE) return 11;
            else return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
