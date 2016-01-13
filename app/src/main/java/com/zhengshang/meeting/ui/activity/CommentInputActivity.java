package com.zhengshang.meeting.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.zhengshang.meeting.R;
import com.zhengshang.meeting.remote.IParam;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by sun on 2016/1/13.
 */
@EActivity(R.layout.layout_comment_input)
public class CommentInputActivity extends BaseActivity {
    @ViewById(R.id.edit_content)
    EditText editContent;
    @ViewById(R.id.tv_send)
    TextView tvSend;

    private boolean canSend;

    @AfterViews
    void init() {
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    canSend = true;
                    tvSend.setBackgroundResource(R.drawable.send_comment_focus_shape);
                    tvSend.setTextColor(Color.WHITE);
                } else {
                    canSend = false;
                    tvSend.setBackgroundResource(R.drawable.send_comment_shape);
                    tvSend.setTextColor(getResources().getColor(R.color.news_content_color));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String hint = getIntent().getStringExtra(IParam.HINT);
        editContent.setHint(hint);
    }

    @Click(R.id.tv_send)
    void clickSend() {
        if (canSend) {
            Intent intent = new Intent();
            intent.putExtra(IParam.CONTENT, editContent.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onTaskSuccess(int action, Object data) {

    }
}
