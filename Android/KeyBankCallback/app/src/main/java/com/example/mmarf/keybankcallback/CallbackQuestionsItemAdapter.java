package com.example.mmarf.keybankcallback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CallbackQuestionsItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] ListOfQuestions;
    String[] ListOfDepartments;

    public CallbackQuestionsItemAdapter(Context c, String[] listOfQuestions, String[] listOfDepartments){
        this.ListOfDepartments = listOfDepartments;
        this.ListOfQuestions = listOfQuestions;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ListOfDepartments.length;
    }

    @Override
    public Object getItem(int position) {
        return ListOfQuestions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.callback_questions_detail, null);
        TextView questionTextView = view.findViewById(R.id.QuestionTextView);
        TextView departmentTextView = view.findViewById(R.id.DepartmentTextView);

        String question = this.ListOfQuestions[position];
        String department = this.ListOfDepartments[position];

        questionTextView.setText(question);
        departmentTextView.setText(department);

        return view;
    }
}
