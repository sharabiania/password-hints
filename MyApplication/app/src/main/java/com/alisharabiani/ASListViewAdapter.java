package com.alisharabiani;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import org.w3c.dom.Text;

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
        super(context, layout, c, from, to, flags);
    }

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

        switch (serviceName){
            case "Facebook":
                icon.setImageResource(R.drawable.facebook_50x50);
                break;
            case "GoDaddy":
                icon.setImageResource(R.drawable.godaddy_50x50);
                break;
            default:
                icon.setImageResource(R.drawable.ic_lock_lock_alpha);
                break;
        }
    }
}
