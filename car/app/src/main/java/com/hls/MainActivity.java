package com.hls;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.hls.component.HandleJoystickView;
import com.hls.configuration.JoystickDirection;
import com.hls.configuration.PackageConfig;
import com.hls.dialog.SettleDialog;
import com.hls.dialog.chooseDialog;
import com.hls.template.BlueToochConnect;
import com.hls.template.CallBack;
import com.hls.template.Connect;
import com.hls.template.ConnectThread;
import com.hls.template.Speech;
import com.hls.ui.fragment.AutoFragment;
import com.hls.ui.fragment.LogFragment;
import com.hls.ui.fragment.HomeFragment;
import com.hls.ui.fragment.OperationFragment;
import com.hls.ui.viewModel.ActivityViewModel;
import com.hls.ui.viewModel.AutoViewModel;

import java.io.IOException;

import kotlinx.coroutines.Delay;

public class MainActivity extends AppCompatActivity {


    public static SettleDialog settleDialog;
    public static TextView debugText;
    public static TextView connectStatus;

    private HandleJoystickView mHandleJoystickView;
    private TextView mTvJoystickDirection;
    public static Button voice;
    public static ScrollView p;
    public static Speech speech;
    public static final String TAG = "car";


    private ActivityViewModel activityViewModel;
    private com.hls.databinding.ActivityMainBinding binding;
    private FragmentManager fragmentManager;

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolBar);
        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(binding.fragment.getId(), new HomeFragment())
                .commit();
        binding.menuBtn.setOnClickListener(v -> binding.drawer.openDrawer(GravityCompat.START));

        fragmentManager = getSupportFragmentManager();

        binding.nav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragment.getId(), HomeFragment.class, null)
                        .commit();
            } else if (id == R.id.operation) {
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragment.getId(), OperationFragment.class, null)
                        .commit();
            } else if (id == R.id.auto) {
                fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(binding.fragment.getId(), AutoFragment.class, null)
                        .commit();
            }

            binding.drawer.closeDrawer(GravityCompat.START);
            //todo
            return true;
        });


        binding.connectBtn.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.setting, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.uart) {
                    //todo
                    return true;
                } else if (id == R.id.bt) {
                    BlueToochConnect blueToochConnect = new BlueToochConnect(this,
                            this.getApplication(), new CallBack() {
                        @Override
                        public void read(int count, String data) {
                            Log.i(TAG,data);
                            activityViewModel.getBtRepository().getDate().postValue(data);
                        }
                    });
                    chooseDialog chooseDialog = new chooseDialog(this, blueToochConnect);
                    chooseDialog.show();
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });


//        ImageButton debug = findViewById(R.id.debug);
//        debug.setOnClickListener(this);
//        ImageButton settle = findViewById(R.id.settle);
//        settle.setOnClickListener(this);
//
//        debugText = findViewById(R.id.debugText);
//        connectStatus = findViewById(R.id.connectStatus);
//
//        Button lt = findViewById(R.id.leftTurn);
//        lt.setOnClickListener(this);
//        Button rt = findViewById(R.id.rightTurn);
//        rt.setOnClickListener(this);
//        p = findViewById(R.id.debugView);
//        voice = findViewById(R.id.voice);
//
//        ((SeekBar)findViewById(R.id.rate)).setOnSeekBarChangeListener(this);
//        new Voice(this);
//        speech = new Speech(this.getApplicationContext());
//
//
//        DebugHandle.info("欢迎使用小车控制台");


    }

//    public void handleRate(int val) {
//        String rate = PackageConfig.getRate(val);
//        if (rate == null) {
//            return;
//        }
//        if (settleDialog == null) {
//            return;
//        }
//        Connect connect = settleDialog.get_connect();
//        if (connect == null) {
//            return;
//        }
//        try {
//            connect.write(rate);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


//        if (v.getId() == R.id.debug) {
//            p.setVisibility(p.getVisibility() == ScrollView.GONE ? ScrollView.VISIBLE : ScrollView.GONE);
//        } else if (v.getId() == R.id.settle) {
//            settleDialog = new SettleDialog(this);
//            settleDialog.show();
//        } else if (v.getId() == R.id.leftTurn) {
//            Connect connect = settleDialog.get_connect();
//            if (connect == null) {
//                return;
//            }
//            try {
//                connect.write(PackageConfig.LEFT_TURN);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        } else if (v.getId() == R.id.rightTurn) {
//            Connect connect = settleDialog.get_connect();
//            if (connect == null) {
//                return;
//            }
//            try {
//                connect.write(PackageConfig.RIGHT_TURN);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//        int progress = seekBar.getProgress();
//        ((TextView)findViewById(R.id.rateText)).setText(""+progress);
//        if(progress>=70)
//
//           handleRate(progress);
}