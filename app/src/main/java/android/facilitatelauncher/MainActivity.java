package android.facilitatelauncher;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.facilitatelauncher.util.Helper;
import android.facilitatelauncher.util.MenuConstant;
import android.facilitatelauncher.view.ClickableViewPager;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.actions.NoteIntents;

import java.util.Calendar;

import me.crosswall.lib.coverflow.core.CoverTransformer;
import me.crosswall.lib.coverflow.core.PagerContainer;

import static android.facilitatelauncher.util.Helper.hasPermissions;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView tvTitle, tvClicked;
    private int positionClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PagerContainer mContainer = findViewById(R.id.pagerContainer);
        tvTitle = findViewById(R.id.tvTitle);
        tvClicked = findViewById(R.id.tvClicked);
        tvTitle.setText("โทรฉุกเฉิน");
        positionClicked = 0;

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS
        , };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        final ClickableViewPager pager = (ClickableViewPager) mContainer.getViewPager();

        PagerAdapter adapter = new MyPagerAdapter();
        pager.setAdapter(adapter);

        pager.setOffscreenPageLimit(adapter.getCount());

        final ViewPager bindingPager = findViewById(R.id.pager);
        bindingPager.setAdapter(adapter);
        bindingPager.setOffscreenPageLimit(adapter.getCount());

        pager.setPageTransformer(false, new CoverTransformer(0.3f, -60f, 0f, 0f));
        Log.d("###", "pager1 width:" + 150 * getResources().getDisplayMetrics().density);

        bindingPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pager.onTouchEvent(motionEvent);
                return false;
            }
        });

        tvClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClicked(positionClicked);
            }
        });

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "clicked:" + tvTitle.getText(), Toast.LENGTH_SHORT).show();
               performClicked(positionClicked);
            }
        });

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

    private String getTitle(int position) {
        String title = "";
        if (position == MenuConstant.EMERGENCY) {
            title = "โทรฉุกเฉิน";
        } else if (position == MenuConstant.ALARM_CLOCK) {
            title = "นาฬิกาปลุก";
        } else if (position == MenuConstant.CAMERA) {
            title = "กล้อง";
        } else if (position == MenuConstant.CALCULATOR) {
            title = "เครื่องคิดเลข";
        } else if (position == MenuConstant.CALENDAR) {
            title = "ปฏิทิน";
        } else if (position == MenuConstant.ADDRESS_BOOK) {
            title = "รายชื่อผู้ติดต่อ";
        } else if (position == MenuConstant.GALLERY) {
            title = "คลังภาพ";
        } else if (position == MenuConstant.MUSIC_PLAYER) {
            title = "เครื่องเล่นเพลง";
        } else if (position == MenuConstant.PHONE_CALL) {
            title = "โทรศัพท์";
        } else if (position == MenuConstant.RECORDER) {
            title = "เครื่องอัดเสียง";
        } else if (position == MenuConstant.SETTING) {
            title = "การตั้งค่า";
        }
        return title;
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
        mTimePicker.setTitle("เลือกเวลา");
        mTimePicker.show();
    }

    private void btnCameraClicked() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
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
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
//        Intent intent = new Intent();
//        ComponentName cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
//        intent.setComponent(cn);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnContactBookClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void btnEmergencyClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยืนยันการโทร");
        builder.setMessage("คุณต้องการที่จะโทรฉุกเฉิน?");

        // Set up the buttons
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
        }
    }

    private void btnMusicPlayerClicked() {
        Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
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
        Intent intent = new Intent(this, PhoneCallActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        }
    }

    private void btnRecorderClicked() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void emergencyCall(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + getString(R.string.emergency_number)));
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


    public void createAlarm(int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void performClicked(int positionClicked){
        if (positionClicked == MenuConstant.EMERGENCY) {
            btnEmergencyClicked();
        } else if (positionClicked == MenuConstant.ALARM_CLOCK) {
            btnAlarmClockClicked();
        } else if (positionClicked == MenuConstant.CAMERA) {
            btnCameraClicked();
        } else if (positionClicked == MenuConstant.CALCULATOR) {
            btnCalculatorClicked();
        } else if (positionClicked == MenuConstant.CALENDAR) {
            btnCalendarClicked();
        } else if (positionClicked == MenuConstant.ADDRESS_BOOK) {
            btnContactBookClicked();
        } else if (positionClicked == MenuConstant.GALLERY) {
            btnGalleryClicked();
        } else if (positionClicked == MenuConstant.MUSIC_PLAYER) {
            btnMusicPlayerClicked();
        } else if (positionClicked == MenuConstant.PHONE_CALL) {
            btnPhoneCallClicked();
        } else if (positionClicked == MenuConstant.RECORDER) {
            btnRecorderClicked();
        } else if (positionClicked == MenuConstant.SETTING) {
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(MainActivity.this);
            if (position == MenuConstant.EMERGENCY) {
                view.setImageResource(R.drawable.ic_emergency);
            } else if (position == MenuConstant.ALARM_CLOCK) {
                view.setImageResource(R.drawable.ic_alarm_clock);
            } else if (position == MenuConstant.CAMERA) {
                view.setImageResource(R.drawable.ic_camera);
            } else if (position == MenuConstant.CALCULATOR) {
                view.setImageResource(R.drawable.ic_calculator);
            } else if (position == MenuConstant.CALENDAR) {
                view.setImageResource(R.drawable.ic_calendar);
            } else if (position == MenuConstant.ADDRESS_BOOK) {
                view.setImageResource(R.drawable.ic_address_book);
            } else if (position == MenuConstant.GALLERY) {
                view.setImageResource(R.drawable.ic_gallery);
            } else if (position == MenuConstant.MUSIC_PLAYER) {
                view.setImageResource(R.drawable.ic_music_player);
            } else if (position == MenuConstant.PHONE_CALL) {
                view.setImageResource(R.drawable.ic_phone);
            } else if (position == MenuConstant.RECORDER) {
                view.setImageResource(R.drawable.ic_recorder);
            }else if (position == MenuConstant.SETTING) {
                view.setImageResource(R.drawable.ic_setting);
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
            return 10;
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
