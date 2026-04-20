package com.hls.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hls.R;
import com.hls.component.HandleJoystickView;
import com.hls.configuration.JoystickDirection;
import com.hls.configuration.PackageConfig;
import com.hls.template.ConnectThread;
import com.hls.ui.viewModel.OperationViewModel;


public class OperationFragment extends Fragment {

    private OperationViewModel mViewModel;
    private com.hls.databinding.FragmentOperationBinding binding;

    public static OperationFragment newInstance() {
        return new OperationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_operation, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .add(binding.fragment.getId(), new LogFragment())
                .commit();
        

        // 设置摇杆方向监听
        binding.handleJoystickView.setOnJoystickDirectionChangeListener(new HandleJoystickView.OnJoystickDirectionChangeListener() {
            @Override
            public void onDirectionChange(JoystickDirection direction, float intensity) {
                String directionText = "当前方向：" + getDirectionName(direction);
                binding.tvJoystickDirection.setText(directionText);
                handleJoystickBusiness(direction);
            }
        });


    }


    /**
     * 获取方向中文名称
     */
    private String getDirectionName(JoystickDirection direction) {
        switch (direction) {
            case STOP:
                return "停止";
            case UP:
                return "上";
            case DOWN:
                return "下";
            case LEFT:
                return "左";
            case RIGHT:
                return "右";
            case LEFT_UP:
                return "左上";
            case LEFT_DOWN:
                return "左下";
            case RIGHT_UP:
                return "右上";
            case RIGHT_DOWN:
                return "右下";
            default:
                return "未知";
        }
    }

    JoystickDirection previous;

    /**
     * 处理摇杆方向对应的业务逻辑
     */
    private void handleJoystickBusiness(JoystickDirection direction) {
        try {
            if (previous == direction) {
                return;
            }
            previous = direction;
            switch (direction) {
                case UP:
                    ConnectThread.write(PackageConfig.FORWARD);
                    // 向上业务逻辑（如：角色上移）
                    break;
                case DOWN:
                    // 向下业务逻辑（如：角色下移）
                    ConnectThread.write(PackageConfig.BACK);
                    break;
                case LEFT:
                    // 向左业务逻辑（如：角色左移）
                    ConnectThread.write(PackageConfig.LEFT);
                    break;
                case RIGHT:
                    // 向右业务逻辑（如：角色右移）
                    ConnectThread.write(PackageConfig.RIGHT);
                    break;
                case LEFT_UP:
                    // 左上业务逻辑（如：角色左上移）
                    ConnectThread.write(PackageConfig.LEFT_FORWARD);
                    break;
                case LEFT_DOWN:
                    // 左下业务逻辑（如：角色左下移）
                    ConnectThread.write(PackageConfig.LEFT_BACK);
                    break;
                case RIGHT_UP:
                    // 右上业务逻辑（如：角色右上移）
                    ConnectThread.write(PackageConfig.RIGHT_FORWARD);
                    break;
                case RIGHT_DOWN:
                    // 右下业务逻辑（如：角色右下移）
                    ConnectThread.write(PackageConfig.RIGHT_BACK);
                    break;
                case STOP:
                    // 停止业务逻辑（如：角色停止移动）
                    ConnectThread.write(PackageConfig.STOP);
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}