package com.alpha.android.video2send;

import android.animation.Animator;
import android.animation.TimeAnimator;
import android.graphics.SurfaceTexture;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by Alpha Jin on 9/30/16.
 */

public class SendUI implements Animator.AnimatorListener, View.OnTouchListener,
        TimeAnimator.TimeListener, TextureView.SurfaceTextureListener, android.view.Window.Callback{

    static final String TAG = "SendUi";
    
    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onTimeUpdate(TimeAnimator timeAnimator, long l, long l1) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    @Nullable
    @Override
    public View onCreatePanelView(int i) {
        return null;
    }

    @Override
    public boolean onCreatePanelMenu(int i, Menu menu) {
        return false;
    }

    @Override
    public boolean onPreparePanel(int i, View view, Menu menu) {
        return false;
    }

    @Override
    public boolean onMenuOpened(int i, Menu menu) {
        return false;
    }

    @Override
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return false;
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {

    }

    @Override
    public void onContentChanged() {

    }

    @Override
    public void onWindowFocusChanged(boolean b) {

    }

    @Override
    public void onAttachedToWindow() {

    }

    @Override
    public void onDetachedFromWindow() {

    }

    @Override
    public void onPanelClosed(int i, Menu menu) {

    }

    @Override
    public boolean onSearchRequested() {
        return false;
    }

    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        return false;
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        return null;
    }

    @Override
    public void onActionModeStarted(ActionMode actionMode) {

    }

    @Override
    public void onActionModeFinished(ActionMode actionMode) {

    }
}
