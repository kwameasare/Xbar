package genius.Xbar;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ServiceInfo;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



public class barservice extends AccessibilityService {


    Animation slideInRight, slideOutRight, drop;
    TextView batteryTxt;
    String batt;
    RelativeLayout profile;

    private final static int INTERVAL = 1000 * 60 ;
    Handler mHandler = new Handler();
    ProgressBar battbar;

    public barservice() {
    }

    WindowManager mWindowManager;
    int mprogress;
    Drawable thumb;
    View xpanel;
    SeekBar brightness;
    boolean ExRunning;
    RelativeLayout exvvv;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    //   @Override
    //   public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    //      throw new UnsupportedOperationException("Not yet implemented");
    //  }


    public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
       AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
       List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

       for (AccessibilityServiceInfo enabledService : enabledServices) {
           ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
           if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
               return true;
       }

       return false;
   }




    @Override
    public void onCreate() {
        super.onCreate();

        View mBarView = View.inflate(this, R.layout.bar_service_layout, null);

        xpanel=View.inflate(this,R.layout.expand,null);
        RelativeLayout back=mBarView.findViewById(R.id.backBtnLayout);
        RelativeLayout home=mBarView.findViewById(R.id.homeBtnLayout);
        RelativeLayout recents=mBarView.findViewById(R.id.recentsBtnLayout);
        RelativeLayout expand=mBarView.findViewById(R.id.statuspart);
        final CardView mainCard= mBarView.findViewById(R.id.maincard);
        brightness =xpanel.findViewById(R.id.Brightness);
        thumb=ContextCompat.getDrawable(this, R.drawable.ic_brightness_high_black_24dp );
        brightness.setThumb(thumb);
        slideInRight= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        exvvv=xpanel.findViewById(R.id.exvvv);
        slideOutRight= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        batteryTxt=mBarView.findViewById(R.id.batt);
        battbar=mBarView.findViewById(R.id.battbar);
        profile=xpanel.findViewById(R.id.top_panel);
        drop=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.drop);

// get Settings permission if not already granted


     if (!isAccessibilityServiceEnabled (this, barservice.class))
     {
         Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
         startActivity(intent);
     }



        slideInRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                profile.setVisibility(View.VISIBLE);
                profile.startAnimation(drop);

            }

            @Override
            public void onAnimationEnd(Animation animation) {



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        mHandlerTask.run();

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                PixelFormat.TRANSLUCENT);

        lp.gravity= Gravity.BOTTOM|Gravity.END;



        // this will allow the bar to run in an overlay on devices that support this

        lp.flags |= WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;



        mWindowManager.addView(mBarView, lp);

        xpanel.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==MotionEvent.ACTION_OUTSIDE)
                {

                    exvvv.startAnimation(slideOutRight);
//mWindowManager.removeView(xpanel);




                }

                return false;
            }

        });


        slideOutRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mWindowManager.removeView(xpanel);
                profile.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barservice.this.performGlobalAction(1);

                if (ExRunning){exvvv.startAnimation(slideOutRight);}
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barservice.this.performGlobalAction(2);
            }
        });

        recents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barservice.this.performGlobalAction(3);
            }
        });

        xpanel.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

                ExRunning=true;

            }

            @Override
            public void onViewDetachedFromWindow(View v) {

                ExRunning=false;

            }
        });


        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!ExRunning) {

                    extPanel(xpanel);

                }

                else {

                    exvvv.startAnimation(slideOutRight);


                }

            }
        });

        seek();




        Intent i = new Intent();
        i.setClassName("genius.xccount", "genius.xccount.SetupActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(i);



    }



    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            checkBattery();
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    private void checkBattery() {
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        Toast.makeText(this,"Battery Stat Updated",Toast.LENGTH_LONG).show();

    }


    public void seek(){


        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                mprogress=progress;


                if(  mprogress<135)
                {
                    brightness.getProgressDrawable().setColorFilter(ContextCompat.getColor(barservice.this,R.color.light), PorterDuff.Mode.MULTIPLY);
                    thumb.setTint(ContextCompat.getColor(barservice.this,R.color.light));
                }

                else if(mprogress>135 &&  mprogress<205)
                {
                    brightness.getProgressDrawable().setColorFilter(ContextCompat.getColor(barservice.this,R.color.yel), PorterDuff.Mode.MULTIPLY);
                    thumb.setTint(ContextCompat.getColor(barservice.this,R.color.yel));
                }

                else if(mprogress>205)
                {
                    brightness.getProgressDrawable().setColorFilter(ContextCompat.getColor(barservice.this,R.color.hotred), PorterDuff.Mode.MULTIPLY);
                    thumb.setTint(ContextCompat.getColor(barservice.this,R.color.hotred));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }


    public void extPanel(View xv){



        WindowManager.LayoutParams exlp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                 WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                PixelFormat.TRANSLUCENT);

        exlp.gravity= Gravity.END;

        mWindowManager.addView(xv,exlp);
        exvvv.startAnimation(slideInRight);


    }




    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            battbar.setProgress(level);
            batt=String.valueOf(level);

           if(level<100){
               batteryTxt.setVisibility(View.VISIBLE);
               batteryTxt.setText(batt);

           }
           else{
               batteryTxt.setVisibility(View.INVISIBLE);
           }



        }
    };






}
