package com.passwordhints.classes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.passwordhints.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Sharabiani on 2018-03-14.
 */
public class ASAutoCompleteAdapter extends ArrayAdapter {
    private final List<String> accountNames;
    public List<String> filteredServiceNames = new ArrayList<>();

    public ASAutoCompleteAdapter(Context context, List<String> accountNamesList) {

        super(context, 0, accountNamesList);
        this.accountNames = accountNamesList;
    }

    @Override
    public int getCount() {
        return filteredServiceNames.size();
    }

    @Override
    public Filter getFilter() {
        return new ASServicesFilter(this, accountNames);
    }

    @Override
    public String getItem(int position){
        return this.filteredServiceNames.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        String service = filteredServiceNames.get(position);

        // Inflate your custom row layout as usual.
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.row_autocomplete, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.autocomplete_textView);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.autocomplete_imageview);
        tvName.setText(service);
        ivIcon.setImageResource(RecordModel.getIcon(service));

        return convertView;
    }
}