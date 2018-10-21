package com.example.louisferdianto.app1;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.firestore.Query;

public class Filters {
    private String category = null;
    private Query.Direction sortDirection = null;

    public Filters() {}
    public boolean hasCategory() {
        return !(TextUtils.isEmpty(category));
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }


    public String getSearchDescription(Context context) {
        StringBuilder desc = new StringBuilder();


        if (category != null) {
            desc.append("<b>");
            desc.append(category);
            desc.append("</b>");
        }


        return desc.toString();
    }
}
