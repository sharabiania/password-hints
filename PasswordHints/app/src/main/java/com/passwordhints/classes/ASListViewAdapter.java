package com.passwordhints.classes;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.*;
import com.passwordhints.R;
import com.passwordhints.services.ASAudioService;


/**
 * Created by Ali Sharabiani on 2016-09-19.
 */
public class ASListViewAdapter extends SimpleCursorAdapter {

    private ASAudioService audioService;
    private ImageButton prevClicked;

    /**
     * Default constructor.
     * @param context application context.
     * @param layout
     * @param c
     * @param from
     * @param to
     * @param flags
     */
    public ASListViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, ASAudioService audioService) {
        super(context, layout, c, from, to, flags);
        this.audioService = audioService;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        long id = cursor.getLong(cursor.getColumnIndex(PasswordHintContract.HintEntry._ID));
        int serviceNameIndex = cursor.getColumnIndex(PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT);
        int accountNameIndex = cursor.getColumnIndex(PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME);
        String serviceName = cursor.getString(serviceNameIndex);
        String accountName = cursor.getString(accountNameIndex);

        TextView serviceNameTextView = (TextView) view.findViewById(R.id.service_textview);
        TextView accountTextView = (TextView) view.findViewById(R.id.account_textview);

        serviceNameTextView.setText(serviceName);
        accountTextView.setText(accountName);

        ImageButton btn = (ImageButton)view.findViewById(R.id.listPlayButton);

        final String stringID = Long.toString(id);
        btn.setVisibility(View.INVISIBLE);
        if(audioService.hasAudio(stringID)) {
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // reset previously clicked
                    if(prevClicked != null) prevClicked.setImageResource(android.R.drawable.ic_media_play);
                    prevClicked = (ImageButton) v;
                    if(audioService.isPlaying())  {
                        audioService.stopPlaying();
                        return;
                    }

                    audioService.setOnPlayCompletion(new ASCallback(v){
                        @Override
                        public void run() {
                            ImageButton v = (ImageButton) param;
                            v.setImageResource(android.R.drawable.ic_media_play);
                        }
                    });
                    audioService.playFile(stringID);
                    ((ImageButton)v).setImageResource(android.R.drawable.ic_media_pause);
                }
            });
        }

        ImageView icon = (ImageView)view.findViewById(R.id.image_view);
        icon.setImageResource(RecordModel.getIcon(serviceName));
    }
}
