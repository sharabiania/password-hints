package services;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;

/**
 * Created by Ali Sharabiani on 2018-03-03.
 */
public class AudioService {

    private static final String FileExtension = ".3gp";
    private static final String TempFileName = "tempfile";
    private static final String TempFile = TempFileName + FileExtension;
    private static final String LOG_TAG = "AudioRecord";

    private Context c;
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;

    public AudioService(Context context) {
        c=context;
        mRecorder = new MediaRecorder();

        //  delete the temp audio file if exists
        File f = new File(c.getCacheDir(), TempFile);
        if(f.exists()) f.delete();
    }

    public boolean hasAudio(String id){
        File f = new File(c.getFilesDir(), id + FileExtension);
        return f.exists();
    }


    public void startPlaying(){
        startPlaying(null);
    }

    public void startPlaying(String filename)  {
        if(mPlayer == null)
            mPlayer = new MediaPlayer();
        File mFile = null;
        if(filename==null) {
            mFile = new File(c.getCacheDir(), TempFile);
        }
        else {
            mFile = new File(c.getFilesDir(), filename + FileExtension);
        }

        try{
            mPlayer.setDataSource(mFile.getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();
        }
        catch(IOException ex){
             Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public void stopPlaying(){
        if(mPlayer == null) return;
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    public void startRecording(){

        File mFile = new File(c.getCacheDir(), TempFile);
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFile.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try{
            mRecorder.prepare();
        }
        catch(IOException ex){
            Log.e(LOG_TAG, "prepare failed");
        }

        mRecorder.start();
    }

    public void stopRecording(){
        if(mRecorder == null) return;
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }



    public void setOnCompletion(){
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }



    public void saveAs(String filename){
        // TODO Rename the audio file if exists
        File from = new File(c.getCacheDir(), TempFile);
        if(from.exists()){
            File to = new File(c.getFilesDir(), filename + FileExtension);
            if(!from.renameTo(to)){
                Log.e(LOG_TAG, "Failed to move the file to the Files DIR");
            }
        }
    }


}
