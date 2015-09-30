package com.patcornejo.qear.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.patcornejo.qear.R;
import com.patcornejo.qear.fragments.QuestionFragment;
import com.patcornejo.qear.utils.Globals;

/**
 * Created by patcornejo on 27-09-15.
 */
public class AnswersAdapter extends GridPagerAdapter {
    String[] letters = new String[] {"A", "B", "C", "D"};
    Context context;
    TextView selected;

    public AnswersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return Globals.question.Answers.length;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int i, int i1) {
        View v = View.inflate(context, R.layout.item_answer, null);

        TextView letter = (TextView) v.findViewById(R.id.letter);
        TextView answer = (TextView) v.findViewById(R.id.answer);

        answer.setText(Globals.question.Answers[i1]);
        letter.setText(letters[i1]);
        v.setOnClickListener(clickl);

        viewGroup.addView(v);
        return v;
    }

    private View.OnClickListener clickl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(selected != null)
                selected.getBackground().setTint(context.getResources().getColor(R.color.grey));

            selected = (TextView) v.findViewById(R.id.letter);
            selected.getBackground().setTint(context.getResources().getColor(R.color.green_circle_pressed));
        }
    };

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, int i1, Object o) {
        viewGroup.removeView((View) o);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view == o);
    }
}
