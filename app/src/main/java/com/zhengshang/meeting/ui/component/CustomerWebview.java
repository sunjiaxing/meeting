package com.zhengshang.meeting.ui.component;

import java.util.LinkedHashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 自定义webview 功能包括图片点击和电话点击
 * 
 * @author admin 下午1:31:49
 * 
 */
@SuppressWarnings("deprecation")
public class CustomerWebview extends WebView {
	private LinkedHashMap<String, String> imgs;

	public LinkedHashMap<String, String> getImgs() {
		return imgs;
	}

	public void setImgs(LinkedHashMap<String, String> imgs) {
		this.imgs = imgs;
	}

	public CustomerWebview(Context context) {
		super(context);
		init();
	}

	public CustomerWebview(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomerWebview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		getSettings().setJavaScriptEnabled(true);// js可用
		getSettings().setLoadsImagesAutomatically(true);// 设置自动加载图片
		getSettings().setDomStorageEnabled(true);
		getSettings().setDefaultTextEncodingName("UTF-8");
		getSettings().setTextSize(TextSize.NORMAL);
		// getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.indexOf(".apk") != -1) {
					getContext().startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				} else if (url.indexOf(".3gp") != -1
						|| url.indexOf(".mp4") != -1
						|| url.indexOf(".flv") != -1
						|| url.indexOf(".wmv") != -1
						|| url.indexOf(".mov") != -1) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String strend = "";
					if (url.toLowerCase().endsWith(".mp4")) {
						strend = "mp4";
					} else if (url.toLowerCase().endsWith(".3gp")) {
						strend = "3gp";
					} else if (url.toLowerCase().endsWith(".mov")) {
						strend = "mov";
					} else if (url.toLowerCase().endsWith(".flv")) {
						strend = "flv";
					} else if (url.toLowerCase().endsWith(".wmv")) {
						strend = "wmv";
					}

					intent.setDataAndType(Uri.parse(url), "video/" + strend);
					getContext().startActivity(intent);
				} else if (url.indexOf(".apk") != -1) {
					getContext().startActivity(
							new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				} else if (url.startsWith("mailto:") || url.startsWith("tel:")) {
					Intent intent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(url));
					getContext().startActivity(intent);
				} else {
					view.loadUrl(url);
				}
				return true;
			}
		});
//		addJavascriptInterface(new Object() {
//			@JavascriptInterface
//			public void onClick(String key) {
//				LinkedHashMap<String, String> imgs = getImgs();
//				if (imgs != null && imgs.size() > 0) {
//					int index = 0;
//					for (String tmpKey : imgs.keySet()) {
//						if (key.equals(tmpKey)) {
//							break;
//						} else {
//							index++;
//						}
//					}
//					// 封装数据
//					ArrayList<ImageZoomModel> datas = new ArrayList<ImageZoomModel>();
//					ImageZoomModel img = null;
//					try {
//						if (Utils.isWifi(getContext())) {
//							// wifi 网使用大图
//							for (String tmpKey : imgs.keySet()) {
//								String jsonStr = imgs.get(tmpKey);
//								JSONObject json = new JSONObject(jsonStr);
//								img = new ImageZoomModel();
//								img.imgUrl = json.getString(IParam.IMG_BIG_URL);
//								datas.add(img);
//							}
//						} else {
//							// 其他网络条件使用中图
//							for (String tmpKey : imgs.keySet()) {
//								String jsonStr = imgs.get(tmpKey);
//								JSONObject json = new JSONObject(jsonStr);
//								img = new ImageZoomModel();
//								img.imgUrl = json.getString(IParam.IMG_MID_URL);
//								if (Utils.isEmpty(img.imgUrl)) {
//									img.imgUrl = json.getString(IParam.IMG_BIG_URL);
//								}
//								datas.add(img);
//							}
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					// 启动显示图片的组件
//					Intent intent = new Intent(getContext(),
//							ImageActivity.class);
//					intent.putExtra(BonConstants.NAME_IMG_INFO, datas);
//					intent.putExtra(BonConstants.NAME_INDEX, index);
//					getContext().startActivity(intent);
//					((Activity) getContext()).overridePendingTransition(
//							R.anim.push_right_in, R.anim.push_right_out);
//				}
//			}
//		}, "image");
//		addJavascriptInterface(new Object() {
//			@JavascriptInterface
//			public void onClick(String phone) {
//				if (!Utils.isEmpty(phone)) {
//					Utils.call(getContext(), phone);
//				}
//			}
//		}, "phone");
//		addJavascriptInterface(new Object() {
//			@JavascriptInterface
//			public void onClick(String adUrl, String adTitle) {
//				if (!Utils.isEmpty(adUrl)) {
//					Intent intent = new Intent(getContext(),
//							ShowUrlActivity.class);
//					intent.putExtra("url", adUrl);
//					intent.putExtra("title", adTitle);
//					getContext().startActivity(intent);
//				}
//			}
//		}, "ad");
	}

}
