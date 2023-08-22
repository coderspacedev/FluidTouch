package com.magicfluids.listeners;

import android.util.Log;
import android.view.SurfaceHolder;

import com.magicfluids.EglHelper;
import com.magicfluids.GLES20Renderer;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class GLThread extends Thread {
    public static final int DEBUG_CHECK_GL_ERROR = 1;
    public static final int DEBUG_LOG_GL_CALLS = 2;
    private static final boolean LOG_THREADS = false;
    private EGLConfigChooser mEGLConfigChooser;
    private int mEGLContextClientVersion;
    private EGLContextFactory mEGLContextFactory;
    private EGLWindowSurfaceFactory mEGLWindowSurfaceFactory;
    private EglHelper mEglHelper;
    public GLThread mEglOwner;
    private boolean mEventsWaiting;
    private GLWrapper mGLWrapper;
    private boolean mHasSurface;
    private boolean mHaveEgl;
    public SurfaceHolder mHolder;
    private boolean mPaused;
    private GLES20Renderer mRenderer;
    private boolean mWaitingForSurface;
    public boolean mDone = false;
    private ArrayList<Runnable> mEventQueue = new ArrayList<>();
    private int mHeight = 0;
    public int mRenderMode = 1;
    private boolean mRequestRender = true;
    private boolean mSizeChanged = true;
    private int mWidth = 0;
    private final GLThreadManager sGLThreadManager = new GLThreadManager(this, this, null);

    public class GLThreadManager {
        private GLThreadManager() {
        }

        GLThreadManager(GLThread gLThread, GLThread gLThread2, GLThreadManager gLThreadManager) {
            this();
        }

        public synchronized void threadExiting(GLThread gLThread) {
            gLThread.mDone = true;
            if (mEglOwner == gLThread) {
                mEglOwner = null;
            }
            notifyAll();
        }

        public synchronized boolean tryAcquireEglSurface(GLThread gLThread) {
            boolean z;
            z = mEglOwner == gLThread || mEglOwner == null;
            mEglOwner = gLThread;
            notifyAll();
            return z;
        }

        public synchronized void releaseEglSurface(GLThread gLThread) {
            if (mEglOwner == gLThread) {
                mEglOwner = null;
            }
            notifyAll();
        }
    }

    public GLThread(GLES20Renderer gLES20Renderer, EGLConfigChooser eGLConfigChooser, EGLContextFactory eGLContextFactory, EGLWindowSurfaceFactory eGLWindowSurfaceFactory, GLWrapper gLWrapper, int i) {
        this.mRenderer = gLES20Renderer;
        this.mEGLConfigChooser = eGLConfigChooser;
        this.mEGLContextFactory = eGLContextFactory;
        this.mEGLWindowSurfaceFactory = eGLWindowSurfaceFactory;
        this.mGLWrapper = gLWrapper;
        this.mEGLContextClientVersion = i;
    }

    @Override
    public void run() {
        setName("GLThread " + getId());
        try {
            try {
                guardedRun();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            this.sGLThreadManager.threadExiting(this);
        }
    }

    private void stopEglLocked() {
        if (this.mHaveEgl) {
            this.mHaveEgl = false;
            this.mEglHelper.destroySurface();
            this.sGLThreadManager.releaseEglSurface(this);
        }
    }

    private void guardedRun() throws InterruptedException {
        mEglHelper = new EglHelper(mEGLConfigChooser, mEGLContextFactory, mEGLWindowSurfaceFactory, mGLWrapper,mEGLContextClientVersion);
        try {
            GL10 gl = null;
            boolean tellRendererSurfaceCreated = true;
            boolean tellRendererSurfaceChanged = true;


            while (!isDone()) {
                int w = 0;
                int h = 0;
                boolean changed = false;
                boolean needStart = false;
                boolean eventsWaiting = false;

                synchronized (sGLThreadManager) {
                    while (true) {
                        if (mPaused) {
                            stopEglLocked();
                        }
                        if (!mHasSurface) {
                            if (!mWaitingForSurface) {
                                stopEglLocked();
                                mWaitingForSurface = true;
                                sGLThreadManager.notifyAll();
                            }
                        } else {
                            if (!mHaveEgl) {
                                if (sGLThreadManager.tryAcquireEglSurface(this)) {
                                    mHaveEgl = true;
                                    mEglHelper.start();
                                    mRequestRender = true;
                                    needStart = true;
                                }
                            }
                        }


                        if (mDone) {
                            return;
                        }

                        if (mEventsWaiting) {
                            eventsWaiting = true;
                            mEventsWaiting = false;
                            break;
                        }

                        if ((!mPaused) && mHasSurface && mHaveEgl && (mWidth > 0) && (mHeight > 0) && (mRequestRender || (mRenderMode == 1))) {
                            changed = mSizeChanged;
                            w = mWidth;
                            h = mHeight;
                            mSizeChanged = false;
                            mRequestRender = false;
                            if (mHasSurface && mWaitingForSurface) {
                                changed = true;
                                mWaitingForSurface = false;
                                sGLThreadManager.notifyAll();
                            }
                            break;
                        }

                        if (LOG_THREADS) {
                            Log.i("GLThread", "waiting tid=" + getId());
                        }
                        sGLThreadManager.wait();
                    }
                }

                if (eventsWaiting) {
                    Runnable r;
                    while ((r = getEvent()) != null) {
                        r.run();
                        if (isDone()) {
                            return;
                        }
                    }
                    // Go back and see if we need to wait to render.
                    continue;
                }

                if (needStart) {
                    tellRendererSurfaceCreated = true;
                    changed = true;
                }
                if (changed) {
                    gl = (GL10) mEglHelper.createSurface(mHolder);
                    tellRendererSurfaceChanged = true;
                }
                if (tellRendererSurfaceCreated) {
                    mRenderer.onSurfaceCreated(gl, mEglHelper.eglConfig);
                    tellRendererSurfaceCreated = false;
                }
                if (tellRendererSurfaceChanged) {
                    mRenderer.onSurfaceChanged(gl, w, h);
                    tellRendererSurfaceChanged = false;
                }
                if ((w > 0) && (h > 0)) {
                    /* draw a frame here */
                    mRenderer.onDrawFrame(gl);

                    /*
                     * Once we're done with GL, we need to call swapBuffers() to instruct the system to display the
                     * rendered frame
                     */
                    mEglHelper.swap();
                    Thread.sleep(10);
                }
            }
        } finally {
            /*
             * clean-up everything...
             */
            synchronized (sGLThreadManager) {
                stopEglLocked();
                mEglHelper.finish();
            }
        }
    }

    private boolean isDone() {
        boolean z;
        synchronized (this.sGLThreadManager) {
            z = this.mDone;
        }
        return z;
    }

    public void setRenderMode(int i) {
        if (i < 0 || i > 1) {
            throw new IllegalArgumentException("renderMode");
        }
        synchronized (this.sGLThreadManager) {
            this.mRenderMode = i;
            if (i == 1) {
                this.sGLThreadManager.notifyAll();
            }
        }
    }

    public int getRenderMode() {
        int i;
        synchronized (this.sGLThreadManager) {
            i = this.mRenderMode;
        }
        return i;
    }

    public void requestRender() {
        synchronized (this.sGLThreadManager) {
            this.mRequestRender = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHolder = surfaceHolder;
        synchronized (this.sGLThreadManager) {
            this.mHasSurface = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void surfaceDestroyed() {
        synchronized (this.sGLThreadManager) {
            this.mHasSurface = false;
            this.sGLThreadManager.notifyAll();
            while (!this.mWaitingForSurface && isAlive() && !this.mDone) {
                try {
                    this.sGLThreadManager.wait();
                } catch (InterruptedException unused) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void onPause() {
        synchronized (this.sGLThreadManager) {
            this.mPaused = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void onResume() {
        synchronized (this.sGLThreadManager) {
            this.mPaused = false;
            this.mRequestRender = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void onWindowResize(int i, int i2) {
        synchronized (this.sGLThreadManager) {
            this.mWidth = i;
            this.mHeight = i2;
            this.mSizeChanged = true;
            this.sGLThreadManager.notifyAll();
        }
    }

    public void requestExitAndWait() {
        synchronized (this.sGLThreadManager) {
            this.mDone = true;
            this.sGLThreadManager.notifyAll();
        }
        try {
            join();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
    }

    public void queueEvent(Runnable runnable) {
        synchronized (this) {
            this.mEventQueue.add(runnable);
            synchronized (this.sGLThreadManager) {
                this.mEventsWaiting = true;
                this.sGLThreadManager.notifyAll();
            }
        }
    }

    private Runnable getEvent() {
        synchronized (this) {
            if (this.mEventQueue.size() <= 0) {
                return null;
            }
            return this.mEventQueue.remove(0);
        }
    }
}
