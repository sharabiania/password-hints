package com.alisharabiani.services;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import com.alisharabiani.classes.ASCallback;
import com.alisharabiani.classes.ASCountDownTimer;
import com.alisharabiani.interfaces.IASEventListener;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ali Sharabiani on 2018-03-03.
 */
public class ASAudioService {

    //region Constants
    private static final int MaxDuration = 4000;
    private static final String FileExtension = ".3gp";
    private static final String TempFileName = "tempfile";
    private static final String TempFile = TempFileName + FileExtension;
    private static final String LOG_TAG = "AS_AudioRecord";
    //endregion

    //region Fields
    private Context c;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private ASLogService log;
    private ASCountDownTimer timer;
    private File mFile;
    //endregion

    //region Constructors
    public ASAudioService(Context context) {
        c = context;
        log = new ASLogService(LOG_TAG);
        mRecorder = new MediaRecorder();
        mRecorder.setMaxDuration(MaxDuration);
        mPlayer = new MediaPlayer();
        timer = new ASCountDownTimer((long) MaxDuration);
        timer.setOnFinishCallBack(new IASEventListener() {
            @Override
            public void Invoke() {
                // Stop the recording?
            }
        });

        //  delete the temp audio file if exists
        File f = new File(c.getCacheDir(), TempFile);
        if(f.exists()) f.delete();

    }
    //endregion

    //region Getter, Setter
    public void setOnMaxRecordDurationReached(final IASEventListener e){
        mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                    mRecorder.stop();
                    if(e != null)
                        e.Invoke();
                }
            }
        });
        timer.cancel();
    }


    public void setOnPlayCompletion(){
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.reset();
            }
        });
    }


    public void setOnPlayCompletion(final ASCallback callback){
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.reset();
                if(callback != null)
                    callback.run();
            }
        });
    }

    public void setOnPlayCompletion(final IASEventListener e){
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.reset();
                if(e != null)
                    e.Invoke();
            }
        });
    }

    public void setOnRecordTickCallback(IASEventListener e){
        timer.setOnTickCallBack(e);
    }
    public int getCurrentRecordTime(){
        return timer.getCurrSecond();
    }
    //endregion

    //region Methods
    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }

    public void stopPlaying(){
        mPlayer.stop();
        mPlayer.reset();
    }

    public boolean hasAudio(String id){
        File f = new File(c.getFilesDir(), id + FileExtension);
        return f.exists();
    }

    public boolean deleteIfExists(String filename){
        // stop if an audio is playing.
        mPlayer.stop();
        mPlayer.reset();
        File f = new File(c.getFilesDir(), filename + FileExtension);
        if(f.exists()){
            return f.delete();
        }
        log.w("Delete File: " + filename + ": does not exist");
        return true;
    }

    public boolean deleteTempIfExists(){
        mPlayer.stop();
        mPlayer.reset();
        File f = new File(c.getCacheDir(), TempFile);
        if(f.exists()) return f.delete();
        return true;
    }

    public void loadTemp(){
        loadFile(null);
    }

    public void playTemp(){
        loadTemp();
        mPlayer.start();
    }

    public void playFile(String fileName){
        loadFile(fileName);
        mPlayer.start();
    }

    public void loadFile(String filename){
        if(mPlayer.isPlaying()){
            mPlayer.stop();
            mPlayer.reset();
        }

        if(filename==null) {
            mFile = new File(c.getCacheDir(), TempFile);
        }
        else {
            mFile = new File(c.getFilesDir(), filename + FileExtension);
        }

        if(!mFile.exists())
            log.e("File doesn't exists: " + mFile.getName());

        try{
            mPlayer.setDataSource(mFile.getAbsolutePath());
            mPlayer.prepare();
        }
        catch(Exception ex){
            log.e("Cannot load audio file", ex);
        }
    }

    public void startRecording(){

        timer.reset();
        mFile = new File(c.getCacheDir(), TempFile);
        if(mFile.exists()) mFile.delete();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFile.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try{
            mRecorder.prepare();
        }
        catch(IOException ex){
            log.e("prepare failed", ex);
        }

        timer.start();
        mRecorder.start();
    }

    public void stopRecording(){
        if(mRecorder == null) return;
        mRecorder.stop();
        mRecorder.reset();
        timer.reset();
    }

    /**
     * Rename and move the temp audio file from the cache folder, to the files folder if exists.
     * @param filename destination file name.
     */
    public void saveAs(String filename){

        File from = new File(c.getCacheDir(), TempFile);
        if(from.exists()){
            File to = new File(c.getFilesDir(), filename + FileExtension);
            if(!from.renameTo(to)){
                log.e("Failed to move the file to the Files DIR");
            }
        }
    }

    public void destroy(){
        log.i("Releasing resources");
        log.d("Release player");
        mPlayer.release();
        mPlayer = null;
        log.d("Release recorder");
        mRecorder.release();
        mRecorder = null;
        timer = null;
    }
    //endregion
}
