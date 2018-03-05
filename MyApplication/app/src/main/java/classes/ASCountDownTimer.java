package classes;

import android.os.CountDownTimer;
import interfaces.IASEventListener;

/**
 * Created by Ali Sharabiani on 2018-03-04.
 */
public class ASCountDownTimer extends CountDownTimer {

    IASEventListener tickEvent;

    public int currSecond;

    public ASCountDownTimer(long millisInFuture){
        super(millisInFuture, 1000);
        currSecond = (int)(millisInFuture / 1000);
    }

    public void setOnTickCallBack(IASEventListener e){
        tickEvent = e;
    }

    public void onTick(long millisUntilFinished) {
        currSecond--;
        if(tickEvent != null)
            tickEvent.Invoke();
    }
    public void onFinish() {
        //btn.setText("");
    }
}
