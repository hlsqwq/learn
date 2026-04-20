package com.hls.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hls.configuration.JoystickDirection;

/**
 * 带滑动程度（0~30）返回的八方向摇杆轮盘View
 */
public class HandleJoystickView extends View {
    // 画笔相关（保持不变）
    private Paint mBgWheelPaint;
    private Paint mBgWheelBorderPaint;
    private Paint mJoystickPaint;
    private Paint mJoystickBorderPaint;

    // 尺寸参数（保持不变）
    private int mViewSize;
    private PointF mCenterPoint;
    private float mBgWheelRadius;
    private float mJoystickRadius;
    private PointF mJoystickPos;

    // 触摸与方向相关
    private JoystickDirection mCurrentDirection = JoystickDirection.STOP;
    private float mSlideIntensity = 0f; // 新增：滑动程度（0~30）
    private OnJoystickDirectionChangeListener mDirectionChangeListener;

    private static final long CHECK_INTERVAL = 200; // 200ms检测一次

    // 构造方法（保持不变）
    public HandleJoystickView(Context context) {
        this(context, null);
    }

    public HandleJoystickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandleJoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initPosition();
    }

    // 初始化画笔（保持不变）
    private void initPaint() {
        mBgWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgWheelPaint.setColor(Color.parseColor("#E5E7EB"));
        mBgWheelPaint.setStyle(Paint.Style.FILL);

        mBgWheelBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgWheelBorderPaint.setColor(Color.parseColor("#9CA3AF"));
        mBgWheelBorderPaint.setStyle(Paint.Style.STROKE);
        mBgWheelBorderPaint.setStrokeWidth(3);

        mJoystickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mJoystickPaint.setColor(Color.parseColor("#2563EB"));
        mJoystickPaint.setStyle(Paint.Style.FILL);

        mJoystickBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mJoystickBorderPaint.setColor(Color.BLACK);
        mJoystickBorderPaint.setStyle(Paint.Style.STROKE);
        mJoystickBorderPaint.setStrokeWidth(2);
    }

    // 初始化位置（保持不变）
    private void initPosition() {
        mCenterPoint = new PointF();
        mJoystickPos = new PointF();
    }

    // 测量尺寸（保持不变）
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(mViewSize, mViewSize);

        mCenterPoint.x = mViewSize / 2f;
        mCenterPoint.y = mViewSize / 2f;
        mBgWheelRadius = mViewSize * 0.45f;
        mJoystickRadius = mViewSize * 0.15f;

        resetJoystick();
    }

    // 绘制（保持不变）
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mBgWheelRadius, mBgWheelPaint);
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mBgWheelRadius, mBgWheelBorderPaint);
        canvas.drawCircle(mJoystickPos.x, mJoystickPos.y, mJoystickRadius, mJoystickPaint);
        canvas.drawCircle(mJoystickPos.x, mJoystickPos.y, mJoystickRadius, mJoystickBorderPaint);
    }


    long lastTime = 0;
    long time=0;
    /**
     * 处理触摸事件（新增滑动程度计算）
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 只要点在 View 范围内就允许操作，不再限制必须点中小球
                updateJoystickPosition(touchX, touchY);
                // DOWN 事件强制刷新，不判断 200ms
                lastTime = System.currentTimeMillis();
                calculateDirection();
                calculateSlideIntensity();
                notifyDirectionChange();
                // 关键：告诉父布局（侧滑菜单等）不要拦截我的触摸事件
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;

            case MotionEvent.ACTION_MOVE:
                updateJoystickPosition(touchX, touchY);
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime >= CHECK_INTERVAL) {
                    lastTime = currentTime;
                    calculateDirection();
                    calculateSlideIntensity();
                    notifyDirectionChange();
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetJoystick();
                mCurrentDirection = JoystickDirection.STOP;
                mSlideIntensity = 0f;
                notifyDirectionChange();
                return true;
        }
        return super.onTouchEvent(event);
    }



    // 判断触摸点是否在摇杆内（保持不变）
    private boolean isInJoystickArea(float x, float y) {
        float distance = (float) Math.sqrt(Math.pow(x - mJoystickPos.x, 2) + Math.pow(y - mJoystickPos.y, 2));
        return distance <= mJoystickRadius;
    }

    // 更新摇杆位置（保持不变）
    private void updateJoystickPosition(float targetX, float targetY) {
        float dx = targetX - mCenterPoint.x;
        float dy = targetY - mCenterPoint.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > mBgWheelRadius - mJoystickRadius) {
            float scale = (mBgWheelRadius - mJoystickRadius) / distance;
            mJoystickPos.x = mCenterPoint.x + dx * scale;
            mJoystickPos.y = mCenterPoint.y + dy * scale;
        } else {
            mJoystickPos.x = targetX;
            mJoystickPos.y = targetY;
        }
        invalidate();
    }

    // 重置摇杆（保持不变）
    private void resetJoystick() {
        mJoystickPos.x = mCenterPoint.x;
        mJoystickPos.y = mCenterPoint.y;
        invalidate();
    }

    // 计算方向（保持不变）
    private void calculateDirection() {
        float dx = mJoystickPos.x - mCenterPoint.x;
        float dy = mJoystickPos.y - mCenterPoint.y;

        if (Math.abs(dx) < 5 && Math.abs(dy) < 5) {
            mCurrentDirection = JoystickDirection.STOP;
            return;
        }

        double radian = Math.atan2(dy, dx);
        double angle = (radian * 180 / Math.PI + 360) % 360;

        if (angle >= 337.5 || angle < 22.5) {
            mCurrentDirection = JoystickDirection.RIGHT;
        } else if (angle >= 22.5 && angle < 67.5) {
            mCurrentDirection = JoystickDirection.RIGHT_DOWN;
        } else if (angle >= 67.5 && angle < 112.5) {
            mCurrentDirection = JoystickDirection.DOWN;
        } else if (angle >= 112.5 && angle < 157.5) {
            mCurrentDirection = JoystickDirection.LEFT_DOWN;
        } else if (angle >= 157.5 && angle < 202.5) {
            mCurrentDirection = JoystickDirection.LEFT;
        } else if (angle >= 202.5 && angle < 247.5) {
            mCurrentDirection = JoystickDirection.LEFT_UP;
        } else if (angle >= 247.5 && angle < 292.5) {
            mCurrentDirection = JoystickDirection.UP;
        } else if (angle >= 292.5 && angle < 337.5) {
            mCurrentDirection = JoystickDirection.RIGHT_UP;
        }
    }

    /**
     * 新增：计算滑动程度（0~30）
     * 逻辑：摇杆到中心的距离 / 最大可滑动距离 * 30
     * 最大可滑动距离 = 背景轮盘半径 - 摇杆半径（避免摇杆超出轮盘）
     */
    private void calculateSlideIntensity() {
        // 计算摇杆到中心的实际距离
        float dx = mJoystickPos.x - mCenterPoint.x;
        float dy = mJoystickPos.y - mCenterPoint.y;
        float actualDistance = (float) Math.sqrt(dx * dx + dy * dy);

        // 最大可滑动距离（摇杆不超出背景轮盘的最大偏移）
        float maxSlideDistance = mBgWheelRadius - mJoystickRadius;

        // 避免除以0（极端情况，maxSlideDistance为0时直接设为0）
        if (maxSlideDistance <= 0) {
            mSlideIntensity = 0f;
            return;
        }

        // 计算比例并映射到0~30
        float ratio = actualDistance / maxSlideDistance;
        mSlideIntensity = ratio * 30;

        // 确保数值不超出0~30范围（边界保护）
        if (mSlideIntensity < 0) {
            mSlideIntensity = 0;
        } else if (mSlideIntensity > 30) {
            mSlideIntensity = 30;
        }
    }



    /**
     * 修改：通知回调时，同时传递方向和滑动程度
     */
    private void notifyDirectionChange() {
        if (mDirectionChangeListener != null) {
            // 传递两个参数：方向 + 滑动程度
            mDirectionChangeListener.onDirectionChange(mCurrentDirection, mSlideIntensity);
        }
    }

    // ---------------------- 对外公开接口（修改回调参数） ----------------------
    public void setOnJoystickDirectionChangeListener(OnJoystickDirectionChangeListener listener) {
        this.mDirectionChangeListener = listener;
    }

    /**
     * 修改：回调接口新增滑动程度参数
     */
    public interface OnJoystickDirectionChangeListener {
        /**
         * 摇杆方向/滑动程度变化时回调
         *
         * @param direction 当前方向
         * @param intensity 滑动程度（0~30，30为最远端）
         */
        void onDirectionChange(JoystickDirection direction, float intensity);
    }
}