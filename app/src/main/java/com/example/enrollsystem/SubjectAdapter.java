package com.example.enrollsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SubjectAdapter extends ArrayAdapter<Subject> {

    public SubjectAdapter(Context context, List<Subject> subjects) {
        super(context, 0, subjects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_subject_adapter, parent, false);
        }

        Subject subject = getItem(position);

        TextView subjectName = convertView.findViewById(R.id.subject_name);
        TextView subjectCredits = convertView.findViewById(R.id.subject_credits);

        subjectName.setText(subject.getName());
        subjectCredits.setText("Credits: " + subject.getCredits());

        return convertView;
    }
}
