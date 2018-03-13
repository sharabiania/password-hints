package com.passwordhints.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.passwordhints.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.passwordhints.interfaces.IASEventListener;
import com.passwordhints.services.ASAudioService;
import com.passwordhints.services.ASLogService;

public class AudioControlFragment extends Fragment {

    private static final String LOG_TAG = "AS_AudioControlFrag";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private boolean mStartRecording = false;
    private ASAudioService audioService;
    private Button recBtn;
    private ImageButton playBtn;
    private ImageButton removeBtn;

    private AudioControlEventListener callback;
    private String loadedFilename;
    private ASLogService log;

    public interface AudioControlEventListener{
        void OnRecord();
        void OnRecordCompleted();
        void OnPlay();
        void OnPlayCompleted();
    }

    //region Constructors
    public AudioControlFragment() {
        // Required empty public constructor
    }
    //endregion

    //region Overrides
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log = new ASLogService(LOG_TAG);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        /// Get the elements
        recBtn = (Button) getView().findViewById(R.id.recordButton);
        playBtn = (ImageButton) getView().findViewById(R.id.playButton);
        removeBtn = (ImageButton) getView().findViewById(R.id.removeButton);


        /// UI work
        playBtn.setVisibility(View.INVISIBLE);
        removeBtn.setVisibility(View.INVISIBLE);

        /// Setup audio service callbacks
        audioService = new ASAudioService(getActivity().getApplicationContext());
        audioService.setOnMaxRecordDurationReached(new IASEventListener() {
            @Override
            public void Invoke() {
                recBtn.setText("");
                mStartRecording = false;
                playBtn.setVisibility(View.VISIBLE);
                playBtn.setEnabled(true);
                removeBtn.setVisibility(View.VISIBLE);
                callback.OnRecordCompleted();
            }
        });
        audioService.setOnPlayCompletion(new IASEventListener() {
            @Override
            public void Invoke() {
                recBtn.setEnabled(true);
                playBtn.setEnabled(true);
                removeBtn.setVisibility(View.VISIBLE);
                callback.OnPlayCompleted();
            }
        });

        audioService.setOnRecordTickCallback(new IASEventListener() {
            @Override
            public void Invoke() {
                recBtn.setText(String.valueOf(audioService.getCurrentRecordTime()));
            }
        });


        /// set button click events
        recBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordOnClick(v);
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOnClick(v);
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                removeOnClick(v);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_audio_control, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AudioControlEventListener) {
            callback = (AudioControlEventListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AudioControlEventListener");
        }
    }

    @Override
    public void onDestroy(){
        audioService.destroy();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionts[], int[] grantResults){
        switch(requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                }
                else{
                    // permission denied.
                    // TODO disable the functionality to record audio.

                }
                break;

        }
    }
    //endregion

    //region Methods

    /**
     * Loads file if exist and enable/disable the buttons.
     * @param filename filename without extension.
     */
    public void loadFile(String filename){
        if(audioService.hasAudio(filename)) {
            loadedFilename = filename;
            playBtn.setEnabled(true);
            playBtn.setVisibility(View.VISIBLE);
            removeBtn.setVisibility(View.VISIBLE);
        }
        else{
            loadedFilename = null;
            playBtn.setVisibility(View.INVISIBLE);
            removeBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void saveAs(String filename){
        // TODO check it this is correct.
        if(removeBtn.getVisibility() == View.INVISIBLE)
            return;
        audioService.saveAs(filename);
    }
    //endregion

    //region Helpers
    private void recordOnClick(View view) {
        // TODO check for permissions.
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
            return;
        }
        loadedFilename = null;
        mStartRecording = !mStartRecording;

        if (mStartRecording) {
            callback.OnRecord();
            recBtn.setVisibility(View.VISIBLE);
            playBtn.setVisibility(View.INVISIBLE);
            removeBtn.setVisibility(View.INVISIBLE);
            audioService.startRecording();
        } else {
            recBtn.setText("");
            audioService.stopRecording();
            playBtn.setVisibility(View.VISIBLE);
            removeBtn.setVisibility(View.VISIBLE);
            playBtn.setEnabled(true);
            callback.OnRecordCompleted();
        }
    }

    private void playOnClick(View v) {
        callback.OnPlay();
        playBtn.setEnabled(false);
        recBtn.setEnabled(false);
        removeBtn.setVisibility(View.INVISIBLE);
        if(loadedFilename == null)
            audioService.playTemp();
        else audioService.playFile(loadedFilename);
    }

    private void removeOnClick(View v){
        removeBtn.setVisibility(View.INVISIBLE);
        playBtn.setVisibility(View.INVISIBLE);
        if(loadedFilename == null)
            audioService.deleteTempIfExists();
        else
            audioService.deleteIfExists(loadedFilename);

        loadedFilename = null;

    }
    //endregion
}
