package com.example.android213;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OnSwipeListener implements View.OnTouchListener {
    private final GestureDetector gestureDetector;

    public OnSwipeListener( Context context ) {
        gestureDetector = new GestureDetector( context, new GestureListener()) ;
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override public boolean onTouch( View view, MotionEvent motionEvent ) {
        return gestureDetector.onTouchEvent( motionEvent );
    }
    public void onSwipeBottom() {}
    public void onSwipeLeft() {}
    public void onSwipeRight() {}
    public void onSwipeTop() {}
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private final static int minSwipeDistance = 100;
        private final static int minSwipeVelocity = 100;

        @Override public boolean onDown(@NonNull MotionEvent e) { return true; }
        @Override public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
            boolean isServed = false;
            if(e1 != null) {
                float deltaX = e2.getX() - e1.getX(), deltaY = e2.getY() - e1.getY(), absX = Math.abs( deltaX ), absY = Math.abs( deltaY );
                if (absX > 2 * absY) {
                    if (absX >= minSwipeDistance && velocityX >= minSwipeVelocity) {
                        if (deltaX > 0) onSwipeRight();
                        else onSwipeLeft();
                        isServed = true;
                    }
                } else if (absY > 2 * absX) {
                    if (absY >= minSwipeDistance && velocityY >= minSwipeVelocity) {
                        if (deltaY > 0) onSwipeBottom();
                        else onSwipeTop();
                        isServed = true;
                    }
                }
            }
            return isServed;
        }
    }
}