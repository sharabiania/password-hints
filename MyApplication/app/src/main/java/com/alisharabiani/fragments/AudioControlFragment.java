package com.alisharabiani.fragments;

import android.graphics.Color;
import android.widget.Button;
import com.alisharabiani.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alisharabiani.interfaces.IASEventListener;
import com.alisharabiani.services.ASAudioService;

public class AudioControlFragment extends Fragment {

    private boolean mStartRecording = false;
    private ASAudioService audioService;
    private Button recBtn;
    private Button playBtn;
    private Button removeBtn;
    private int defaultColor;
    private AudioControlEventListener callback;
    private String loadedFilename;

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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        /// Get the elements
        recBtn = (Button) getView().findViewById(R.id.recordButton);
        playBtn = (Button) getView().findViewById(R.id.playButton);
        removeBtn = (Button) getView().findViewById(R.id.removeButton);

        /// UI work
        playBtn.setEnabled(false);
        removeBtn.setVisibility(View.INVISIBLE);
        defaultColor = recBtn.getTextColors().getDefaultColor();

        /// Setup audio service callbacks
        audioService = new ASAudioService(getActivity().getApplicationContext());
        audioService.setOnMaxRecordDurationReached(new IASEventListener() {
            @Override
            public void Invoke() {
                recBtn.setTextColor(Color.BLACK);
                recBtn.setText("Record");
                mStartRecording = false;
                playBtn.setVisibility(View.VISIBLE);
                callback.OnRecordCompleted();
            }
        });
        audioService.setOnPlayCompletion(new IASEventListener() {
            @Override
            public void Invoke() {
                recBtn.setEnabled(true);
                recBtn.setAlpha(1);
                playBtn.setEnabled(true);
                removeBtn.setVisibility(View.VISIBLE);
                callback.OnPlayCompleted();
            }
        });

        audioService.setOnRecordTickCallback(new IASEventListener() {
            @Override
            public void Invoke() {
                recBtn.setText("Stop " + audioService.getCurrentRecordTime());
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
            removeBtn.setVisibility(View.VISIBLE);
        }
        else{
            loadedFilename = null;
            playBtn.setEnabled(false);
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
        loadedFilename = null;
        mStartRecording = !mStartRecording;

        if (mStartRecording) {
            callback.OnRecord();
            recBtn.setText("Stop");
            recBtn.setTextColor(Color.RED);
            playBtn.setVisibility(View.INVISIBLE);
            removeBtn.setVisibility(View.INVISIBLE);
            audioService.startRecording();
        } else {
            recBtn.setText("Record");
            recBtn.setTextColor(defaultColor);
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
        recBtn.setAlpha(0.5f);
        removeBtn.setVisibility(View.INVISIBLE);
        if(loadedFilename == null)
            audioService.playTemp();
        else audioService.playFile(loadedFilename);
    }

    private void removeOnClick(View v){
        removeBtn.setVisibility(View.INVISIBLE);
        playBtn.setEnabled(false);
        if(loadedFilename == null)
            audioService.deleteTempIfExists();
        else
            audioService.deleteIfExists(loadedFilename);

        loadedFilename = null;

    }
    //endregion
}
