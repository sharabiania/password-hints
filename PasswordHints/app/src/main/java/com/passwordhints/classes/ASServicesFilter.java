package com.passwordhints.classes;

import android.widget.Filter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Sharabiani on 2018-03-14.
 */
public class ASServicesFilter extends Filter {
    ASAutoCompleteAdapter adapter;
    List<String> originalList;
    List<String> filteredList;

    public ASServicesFilter(ASAutoCompleteAdapter adapter, List<String> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint){
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if(constraint == null || constraint.length() == 0){
            filteredList.addAll(originalList);
        }
        else{
            final String filterPattern = constraint.toString().toLowerCase().trim();
            // Your filtering logic goes here
            for(final String str : originalList) {
                if(str.toLowerCase().startsWith(filterPattern)) {
                    filteredList.add(str);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return  results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results){
        adapter.filteredServiceNames.clear();
        adapter.filteredServiceNames.addAll((List) results.values);
        adapter.notifyDataSetChanged();
    }
}
