package android.facilitatelauncher.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.facilitatelauncher.OnSwipeTouchListener;
import android.facilitatelauncher.R;
import android.facilitatelauncher.database.DatabaseHandler;
import android.facilitatelauncher.model.Contact;
import android.facilitatelauncher.util.MenuConstant;
import android.facilitatelauncher.view.ClickableViewPager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.crosswall.lib.coverflow.core.CoverTransformer;
import me.crosswall.lib.coverflow.core.PagerContainer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.facilitatelauncher.activity.MenuChooserActivity.HANDICAP_TYPE;

public class AddressBookActivity extends AppCompatActivity {
    private int positionSelected;
    private DatabaseHandler db;
    private ImageView imgCall;
    private PagerAdapter adapter;
    private ClickableViewPager pager;
    private MediaPlayer mediaPlayer;
    private TextView tvNoContact;
    private RelativeLayout rlContent;
    private List<Contact> contatactList = new ArrayList<>();
    private int userType = -1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);

        initInstance();
        getContactList();
        initView();
        initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.menu_trash) {
            onTrashClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onTrashClicked() {
        if (contatactList.size() == 0) return;

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("ยืนยันการลบรายชื่อ")
                .content("คุณต้องการที่จะลบรายชื่อผู้ติดต่อนี้?")
                .cancelable(false)
                .positiveText("ยืนยัน")
                .negativeText("ยกเลิก")
                .typeface("RSU_Regular.ttf", "RSU_light.ttf");

        MaterialDialog dialog = builder.build();
        dialog.getTitleView().setTextSize(40);
        dialog.getContentView().setTextSize(25);
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(25);
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(25);
        dialog.show();

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Contact contact = contatactList.get(positionSelected);
                db.deleteContact(contact);
                contatactList.remove(contact);
                if (contatactList.size() == 0) {
                    tvNoContact.setVisibility(View.VISIBLE);
                }
                adapter = new MyPagerAdapter(contatactList);
                pager.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        getSupportActionBar().setTitle("รายชื่อผู้ติดต่อ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tvNoContact = findViewById(R.id.tvNoContact);
        imgCall = findViewById(R.id.imgCall);
        rlContent = findViewById(R.id.rlContent);

        if (contatactList.size() < 1) {
            tvNoContact.setVisibility(View.VISIBLE);
        }

        if (contatactList.size() > 0) {
            playSound(0);
            PagerContainer mContainer = findViewById(R.id.pagerContainer);
            pager = (ClickableViewPager) mContainer.getViewPager();
            adapter = new MyPagerAdapter(contatactList);
            pager.setAdapter(adapter);

            pager.setOffscreenPageLimit(adapter.getCount());

            final ViewPager bindingPager = findViewById(R.id.pager);
            bindingPager.setAdapter(adapter);
            bindingPager.setOffscreenPageLimit(adapter.getCount());

            pager.setPageTransformer(false, new CoverTransformer(0.3f, -60f, 0f, 0f));
            Log.d("###", "pager1 width:" + 150 * getResources().getDisplayMetrics().density);

            bindingPager.setOnTouchListener(new OnSwipeTouchListener(AddressBookActivity.this) {
                @Override
                public void onClick() {
                    super.onClick();
                    // your on click here
                }

                @Override
                public void onDoubleClick() {
                    super.onDoubleClick();
                    playSounds(R.raw.calling);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            makePhoneCall(contatactList.get(positionSelected).getPhoneNumber());
                        }
                    }, 1000);

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
                    rlContent.setVisibility(View.VISIBLE);
                    playSounds(R.raw.tap_delete);
                    rlContent.setOnTouchListener(new OnSwipeTouchListener(AddressBookActivity.this) {
                        @Override
                        public void onClick() {
                            super.onClick();
                            // your on click here
                        }

                        @Override
                        public void onDoubleClick() {
                            super.onDoubleClick();
                            playSounds(R.raw.delete_contact);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rlContent.setVisibility(View.GONE);
                                    Contact contact = contatactList.get(positionSelected);
                                    db.deleteContact(contact);
                                    contatactList.remove(contact);
                                    if (contatactList.size() == 0) {
                                        tvNoContact.setVisibility(View.VISIBLE);
                                    }
                                    adapter = new MyPagerAdapter(contatactList);
                                    pager.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            },1000);

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
                            rlContent.setVisibility(View.GONE);
                            playSounds(R.raw.cancel);
                        }

                        @Override
                        public void onSwipeRight() {
                            super.onSwipeRight();
                            rlContent.setVisibility(View.GONE);
                            playSounds(R.raw.cancel);
                        }
                    });
                }

                @Override
                public void onSwipeDown() {
                    super.onSwipeDown();
                    // your swipe down here.
                }

                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                }

                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
                }
            });

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    positionSelected = position;
                }

                @Override
                public void onPageSelected(int position) {
                    Log.i("test", "onPageSelected: " + position);
                    playSound(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void playSounds(int resId) {
        if (userType == HANDICAP_TYPE) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            } else {
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer = MediaPlayer.create(this, resId);
            mediaPlayer.start();
        }
    }

    private void playSound(int position) {
        String path = contatactList.get(position).getSource();

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    private void initListener() {
        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallClicked();
            }
        });
    }

    private void onCallClicked() {
        if (contatactList.size() == 0) return;

        final String phoneNumber = contatactList.get(positionSelected).getPhoneNumber();

        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title("ยืนยันการโทร")
                .content("คุณต้องการที่จะโทร " + phoneNumber + " ใช่ไหม?")
                .cancelable(false)
                .positiveText("ยืนยัน")
                .negativeText("ยกเลิก")
                .typeface("RSU_Regular.ttf", "RSU_light.ttf");

        MaterialDialog dialog = builder.build();
        dialog.getTitleView().setTextSize(40);
        dialog.getContentView().setTextSize(25);
        dialog.getActionButton(DialogAction.POSITIVE).setTextSize(25);
        dialog.getActionButton(DialogAction.NEGATIVE).setTextSize(25);
        dialog.show();

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                makePhoneCall(phoneNumber);
            }
        });
    }

    private void initInstance() {
        db = new DatabaseHandler(this);
        positionSelected = 0;
        Log.i("test", "initInstance: ");
        Intent intent = getIntent();
        userType = intent.getIntExtra("USER_TYPE", -1);

        if (userType == HANDICAP_TYPE) {
            playSounds(R.raw.tap_call);
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getContactList() {
        contatactList = db.getAllContacts();
    }

    private void makePhoneCall(String phoneNumber) {
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

    private class MyPagerAdapter extends PagerAdapter {
        List<Contact> contactList = new ArrayList<>();

        public MyPagerAdapter(List<Contact> contactList) {
            this.contactList = contactList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Contact contact = contactList.get(position);
            TextView view = new TextView(AddressBookActivity.this);
            view.setText(Html.fromHtml("<h4>" + contact.getName() + "</h4><p>" + contact.getPhoneNumber() + "</p>"));
            view.setGravity(Gravity.CENTER);
            view.setTextSize(50);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        private void setData(List<Contact> contacts) {
            contactList = contacts;
        }
    }


}
