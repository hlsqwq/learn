package com.hls.ui.viewModel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.RequiresPermission;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.hls.MainActivity;
import com.hls.R;
import com.hls.configuration.BConfig;
import com.hls.databinding.ActivityMainBinding;
import com.hls.dialog.chooseDialog;
import com.hls.template.BlueToochConnect;
import com.hls.template.CallBack;
import com.hls.template.ConnectThread;
import com.hls.ui.fragment.AutoFragment;
import com.hls.ui.fragment.HomeFragment;
import com.hls.ui.fragment.OperationFragment;
import com.hls.ui.repository.BtRepository;

import java.io.IOException;

import lombok.Getter;

@Getter
public class ActivityViewModel extends ViewModel {

    private final BtRepository btRepository=BtRepository.getInstance();








}
