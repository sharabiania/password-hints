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
    private int defaultColor;
    private AudioControlEventListener callback;

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

        recBtn = (Button) getView().findViewById(R.id.recordButton);
        playBtn = (Button) getView().findViewById(R.id.playButton);
        // TODO addBtn = (Button) getView().findViewById(R.id.addButton);

        defaultColor = recBtn.getTextColors().getDefaultColor();

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
                callback.OnPlayCompleted();
            }
        });

        audioService.setOnRecordTickCallback(new IASEventListener() {
            @Override
            public void Invoke() {
                recBtn.setText("Stop " + audioService.getCurrentRecordTime());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_control, container, false);

        // set button click events
        view.findViewById(R.id.recordButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordOnClick(v);
            }
        });

        view.findViewById(R.id.playButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOnClick(v);
            }
        });

        return view;
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
    public void saveAs(String filename){
        audioService.saveAs(filename);
    }

    public void recordOnClick(View view) {
        // TODO check for permissions.

        mStartRecording = !mStartRecording;

        if (mStartRecording) {
            callback.OnRecord();
            recBtn.setText("Stop");
            recBtn.setTextColor(Color.RED);
            playBtn.setVisibility(View.INVISIBLE);
            audioService.startRecording();
        } else {
            recBtn.setText("Record");
            recBtn.setTextColor(defaultColor);
            audioService.stopRecording();
            playBtn.setVisibility(View.VISIBLE);
            callback.OnRecordCompleted();
        }
    }

    public void playOnClick(View v) {
        callback.OnPlay();
        playBtn.setEnabled(false);
        recBtn.setEnabled(false);
        recBtn.setAlpha(0.5f);
        audioService.startPlaying();
    }
    //endregion
}
