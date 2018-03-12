package com.passwordhints.classes;

import android.os.CountDownTimer;
import com.passwordhints.interfaces.IASEventListener;
import com.passwordhints.services.ASLogService;

/**
 * Created by Ali Sharabiani on 2018-03-04.
 */
public class ASCountDownTimer extends CountDownTimer {

    private static final String LOG_TAG = "AS_CountDownTimer";
    private IASEventListener tickEvent;
    private IASEventListener finishEvent;
    private ASLogService log;
    private int currSecond;
    private int maxSecond;

    public ASCountDownTimer(long millisInFuture){
        super(millisInFuture, 1000);
        maxSecond = (int)(millisInFuture / 1000);
        currSecond = maxSecond;
        log = new ASLogService(LOG_TAG);
    }

    public void setOnTickCallBack(IASEventListener e){
        tickEvent = e;
    }

    public void setOnFinishCallBack(IASEventListener e){
        finishEvent = e;
    }

    public int getCurrSecond(){return currSecond;}

    public void onTick(long millisUntilFinished) {
        currSecond--;
        if(tickEvent != null)
            tickEvent.Invoke();
    }

    /**
     * Invoked when time is up.
     */
    public void onFinish() {
        log.d("Timer on finish");
        currSecond = maxSecond;
        if(finishEvent != null)
            finishEvent.Invoke();
    }

    public void reset(){
        cancel();
        currSecond = maxSecond;
    }
}
