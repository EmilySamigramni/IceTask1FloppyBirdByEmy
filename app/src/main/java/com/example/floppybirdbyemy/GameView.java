package com.example.floppybirdbyemy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "GameView";
    private GameThread gameThread;
    private Bitmap birdBitmap;
    private Bitmap backgroundBitmap;
    private int birdX, birdY;
    private int birdVelocity;
    private int gravity;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), this);
        birdBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird);
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background); // Reference to drawable resource
        birdX = 100;
        birdY = 100;
        birdVelocity = 0;
        gravity = 2;
        Log.d(TAG, "GameView created");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread.setRunning(true);
        gameThread.start();
        Log.d(TAG, "Surface created and game thread started");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
                Log.d(TAG, "Game thread stopped");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the background
        if (backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 0, new Paint());
            Log.d(TAG, "Drawing background");
        } else {
            Log.d(TAG, "backgroundBitmap is null");
        }

        // Draw the bird
        if (birdBitmap != null) {
            canvas.drawBitmap(birdBitmap, birdX, birdY, new Paint());
            Log.d(TAG, "Drawing bird at: " + birdX + ", " + birdY);
        } else {
            Log.d(TAG, "birdBitmap is null");
        }
    }

    public void update() {
        birdY += birdVelocity;
        birdVelocity += gravity;
        if (birdY < 0) {
            birdY = 0;
        }
        if (birdY > getHeight() - birdBitmap.getHeight()) {
            birdY = getHeight() - birdBitmap.getHeight();
        }
        Log.d(TAG, "Updated bird position to: " + birdY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                birdVelocity = -30;
                Log.d(TAG, "Bird flapped");
                break;
        }
        return true;
    }
}
