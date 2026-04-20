package com.example.my_uart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {
    // 方向枚举
    public enum Direction {
        UP, DOWN, LEFT, RIGHT, CENTER
    }
    
    // 回调接口
    public interface OnDirectionChangedListener {
        void onDirectionChanged(Direction direction);
        void onStop();
    }
    
    private OnDirectionChangedListener listener;
    
    // 绘制相关变量
    private Paint basePaint;
    private Paint stickPaint;
    private int baseRadius;
    private int stickRadius;
    private int centerX;
    private int centerY;
    private float stickX;
    private float stickY;
    private boolean isDragging = false;
    
    // 方向判断阈值
    private static final float THRESHOLD = 0.3f;

    public JoystickView(Context context) {
        super(context);
        init();
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        // 初始化画笔
        basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        basePaint.setColor(Color.argb(100, 100, 100, 100));
        basePaint.setStyle(Paint.Style.FILL);
        
        stickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stickPaint.setColor(Color.argb(200, 200, 200, 200));
        stickPaint.setStyle(Paint.Style.FILL);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        // 计算中心点和半径
        centerX = w / 2;
        centerY = h / 2;
        baseRadius = Math.min(w, h) / 2;
        stickRadius = baseRadius / 3;
        
        // 初始化摇杆位置
        stickX = centerX;
        stickY = centerY;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // 绘制底座
        canvas.drawCircle(centerX, centerY, baseRadius, basePaint);
        
        // 绘制摇杆
        canvas.drawCircle(stickX, stickY, stickRadius, stickPaint);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isDragging = true;
                updateStickPosition(event.getX(), event.getY());
                calculateDirection();
                return true;
                
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                // 重置摇杆位置
                stickX = centerX;
                stickY = centerY;
                invalidate();
                
                // 触发停止事件
                if (listener != null) {
                    listener.onStop();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }
    
    private void updateStickPosition(float x, float y) {
        // 计算与中心点的距离
        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        // 如果超出底座范围，限制在边缘
        if (distance > baseRadius - stickRadius) {
            float ratio = (baseRadius - stickRadius) / distance;
            stickX = centerX + dx * ratio;
            stickY = centerY + dy * ratio;
        } else {
            stickX = x;
            stickY = y;
        }
        
        invalidate();
    }
    
    private void calculateDirection() {
        if (listener == null) return;
        
        // 计算相对中心的偏移比例
        float dx = stickX - centerX;
        float dy = stickY - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        
        // 如果距离太小，视为中心位置
        if (distance < stickRadius) {
            listener.onDirectionChanged(Direction.CENTER);
            return;
        }
        
        float xRatio = dx / distance;
        float yRatio = dy / distance;
        
        // 判断方向（优先判断上下）
        if (Math.abs(yRatio) > Math.abs(xRatio)) {
            if (yRatio < -THRESHOLD) {
                listener.onDirectionChanged(Direction.UP);
            } else if (yRatio > THRESHOLD) {
                listener.onDirectionChanged(Direction.DOWN);
            } else {
                listener.onDirectionChanged(Direction.CENTER);
            }
        } else {
            if (xRatio < -THRESHOLD) {
                listener.onDirectionChanged(Direction.LEFT);
            } else if (xRatio > THRESHOLD) {
                listener.onDirectionChanged(Direction.RIGHT);
            } else {
                listener.onDirectionChanged(Direction.CENTER);
            }
        }
    }
    
    // 设置监听器
    public void setOnDirectionChangedListener(OnDirectionChangedListener listener) {
        this.listener = listener;
    }
    
    // 可以自定义颜色的方法
    public void setBaseColor(int color) {
        basePaint.setColor(color);
        invalidate();
    }
    
    public void setStickColor(int color) {
        stickPaint.setColor(color);
        invalidate();
    }
}