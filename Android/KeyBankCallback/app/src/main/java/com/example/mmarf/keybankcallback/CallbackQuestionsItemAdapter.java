package com.example.mmarf.keybankcallback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CallbackQuestionsItemAdapter extends BaseAdapter {

    final String mContactPrefix = "Contact ";
    LayoutInflater mInflater;
    String[] mListOfQuestions;
    String[] mListOfDepartments;

    public CallbackQuestionsItemAdapter(Context c, String[] listOfQuestions, String[] listOfDepartments){
        this.mListOfDepartments = listOfDepartments;
        this.mListOfQuestions = listOfQuestions;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListOfDepartments.length;
    }

    @Override
    public Object getItem(int position) {
        return mListOfQuestions[position];
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

        String question = this.mListOfQuestions[position];
        String department = this.mListOfDepartments[position];

        questionTextView.setText(question);
        departmentTextView.setText(mContactPrefix + department);

        return view;
    }
}
