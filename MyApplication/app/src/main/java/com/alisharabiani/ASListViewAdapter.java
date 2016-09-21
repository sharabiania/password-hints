package com.alisharabiani;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Ali Sharabiani on 2016-09-19.
 */
public class ASListViewAdapter extends SimpleCursorAdapter {

    /**
     * Default constructor.
     * @param context application context.
     * @param layout
     * @param c
     * @param from
     * @param to
     * @param flags
     */
    public ASListViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);}

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        int serviceNameIndex = cursor.getColumnIndex(PasswordHintContract.HintEntry.COLUMN_NAME_ACCOUNT);
        int accountNameIndex = cursor.getColumnIndex(PasswordHintContract.HintEntry.COLUMN_NAME_USERNAME);
        String serviceName = cursor.getString(serviceNameIndex);
        String accountName = cursor.getString(accountNameIndex);

        TextView serviceNameTextView = (TextView) view.findViewById(R.id.service_textview);
        TextView accountTextView = (TextView) view.findViewById(R.id.account_textview);

        serviceNameTextView.setText(serviceName);
        accountTextView.setText(accountName);


        ImageView icon = (ImageView)view.findViewById(R.id.image_view);

        switch (serviceName.toLowerCase()){
            case "amazon":
                icon.setImageResource(R.drawable.amazon);
                break;
            case "bitbucket":
                icon.setImageResource(R.drawable.bitbucket);
                break;
            case "ebay":
                icon.setImageResource(R.drawable.ebay);
                break;
            case "facebook":
                icon.setImageResource(R.drawable.facebook);
                break;
            case "github":
                icon.setImageResource(R.drawable.github);
                break;
            case "gmail":
                icon.setImageResource(R.drawable.gmail);
                break;
            case "godaddy":
                icon.setImageResource(R.drawable.godaddy);
                break;
            case "google":
                icon.setImageResource(R.drawable.google);
                break;
            case "instagram":
                icon.setImageResource(R.drawable.instagram);
                break;
            case "msn":
                icon.setImageResource(R.drawable.msn);
                break;
            case "scotiabank":
                icon.setImageResource(R.drawable.scotiabank);
                break;
            case "stackoverflow":
                icon.setImageResource(R.drawable.stackoverflow);
                break;
            case "td bank":
                icon.setImageResource(R.drawable.tdbank);
                break;
            default:
                icon.setImageResource(R.drawable.ic_lock_lock_alpha);
                break;
        }
    }
}
