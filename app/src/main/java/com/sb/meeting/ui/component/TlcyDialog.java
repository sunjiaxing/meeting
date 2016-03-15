package com.sb.meeting.ui.component;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sb.meeting.R;
import com.sb.meeting.common.Utils;

public class TlcyDialog extends Dialog implements OnClickListener {
	public interface TlcyDialogListener {
		public void onClick();
	}

	private TextView tvTitle;
	private TextView tvMessage,tvMessageTwo;
	private TlcyDialogListener okListener;
	private TlcyDialogListener cancelLisenter;
	private View layoutOkAndCancel;
	private Button btnOne, btnLeft, btnRight;
	private long l;

	public TlcyDialog(Context context) {
		super(context, R.style.dialog);
		setContentView(R.layout.tlcydialog);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(context.getString(R.string.tip));
		tvMessage = (TextView) findViewById(R.id.tv_message_content);
		tvMessageTwo = (TextView) findViewById(R.id.tv_message_content_two);
		layoutOkAndCancel = findViewById(R.id.layout_ok_and_cancel);

		btnOne = (Button) findViewById(R.id.btn_one);
		btnOne.setOnClickListener(this);

		btnLeft = (Button) findViewById(R.id.btn_left);
		btnLeft.setOnClickListener(this);

		btnRight = (Button) findViewById(R.id.btn_right);
		btnRight.setOnClickListener(this);

	}
	
	public TlcyDialog setMessageTwo(String msg){
		if (!Utils.isEmpty(msg)) {
			tvMessageTwo.setVisibility(View.VISIBLE);
			tvMessageTwo.setText(msg);
		}
		return this;
	}

	@Override
	public void onClick(View v) {
		if (Math.abs(System.currentTimeMillis() - l) > 500) {
			l = System.currentTimeMillis();
			switch (v.getId()) {
			case R.id.btn_one:
				dismiss();
				if (okListener != null) {
					okListener.onClick();
				}
				break;
			case R.id.btn_left:
				dismiss();
				if (cancelLisenter != null) {
					cancelLisenter.onClick();
				}
				break;
			case R.id.btn_right:
				dismiss();
				if (okListener != null) {
					okListener.onClick();
				}
				break;
			}
		}
	}

	/**
	 * 
	 * 设置对话框的标题
	 */
	public TlcyDialog setTitle(String title) {
		this.tvTitle.setText(title);
		return this;
	}

	/**
	 * 设置对话框提示消息
	 */
	public TlcyDialog setMessage(String message) {
		this.tvMessage.setText(message);
		return this;
	}

	/**
	 * 重新设置确定和取消按钮监听器
	 */
	public TlcyDialog setButton(TlcyDialogListener okListener,
			TlcyDialogListener cancelListener) {
		layoutOkAndCancel.setVisibility(View.VISIBLE);
		btnOne.setVisibility(View.GONE);
		this.okListener = okListener;
		this.cancelLisenter = cancelListener;
		return this;
	}

	/**
	 * 设置确定和取消按钮的文本和监听器
	 */
	public TlcyDialog setButton(String oktext, String cancelText,
			TlcyDialogListener okListener, TlcyDialogListener cancelListener) {
		layoutOkAndCancel.setVisibility(View.VISIBLE);
		btnOne.setVisibility(View.GONE);
		if (!Utils.isEmpty(oktext)) {
			btnRight.setText(oktext);
		} else {
			btnRight.setVisibility(View.GONE);
		}
		if (!Utils.isEmpty(cancelText)) {
			btnLeft.setText(cancelText);
		} else {
			btnLeft.setVisibility(View.GONE);
		}
		this.okListener = okListener;
		this.cancelLisenter = cancelListener;
		return this;
	}

	/**
	 * 设置弹出框为只有一个确定按钮
	 */
	public TlcyDialog setOnlyOkPositiveMethod(String oktext) {
		layoutOkAndCancel.setVisibility(View.GONE);
		btnOne.setVisibility(View.VISIBLE);
		if (!Utils.isEmpty(oktext)) {
			btnOne.setText(oktext);
		}
		return this;
	}

	/**
	 * 设置弹出框为只有一个确定按钮的文本和监听器
	 */
	public TlcyDialog setOnlyOkPositiveMethod(String oktext,
			TlcyDialogListener okListener) {
		layoutOkAndCancel.setVisibility(View.GONE);
		btnOne.setVisibility(View.VISIBLE);
		if (!Utils.isEmpty(oktext)) {
			btnOne.setText(oktext);
		}
		this.okListener = okListener;
		return this;
	}

	/**
	 * 设置为无按钮
	 */
	public TlcyDialog setNoButton() {
		layoutOkAndCancel.setVisibility(View.GONE);
		btnOne.setVisibility(View.GONE);
		return this;
	}
}
