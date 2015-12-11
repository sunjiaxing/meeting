package com.zhengshang.meeting.common;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhengshang.meeting.R;

/**
 * 用于universal-image-loader
 * 
 * @author sun 2015年1月19日 下午1:34:53
 * 
 */
public class ImageOption {
	private static DisplayImageOptions nomalOption;

	/**
	 * 创建普通的option
	 * 
	 * @author sun 下午1:34:40
	 * @return
	 */
	public static DisplayImageOptions createNomalOption() {
		if (nomalOption == null) {
			nomalOption = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.mipmap.default_item_pic)
					.showImageForEmptyUri(R.mipmap.default_item_pic)
					.showImageOnFail(R.mipmap.default_item_pic)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		}
		return nomalOption;
	}

	/**
	 * 显示圆角图片
	 * @param round
	 * @return
	 */
	public static DisplayImageOptions createRoundOption(int round) {
		return new DisplayImageOptions.Builder()
				.showImageOnLoading(R.mipmap.default_item_pic)
				.showImageForEmptyUri(R.mipmap.default_item_pic)
				.showImageOnFail(R.mipmap.default_item_pic)
				.cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(round)).build();
	}
	/**
	 * 创建指定 默认图的option
	 * 
	 * @author sun 2015年9月17日16:44:26
	 * @param _default
	 * @return
	 */
	public static DisplayImageOptions createNomalOption(Drawable _default) {
		if (_default == null) {
			return createNomalOption();
		}
		return new DisplayImageOptions.Builder().showImageOnLoading(_default)
				.showImageForEmptyUri(_default).showImageOnFail(_default)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	/**
	 * 缩放
	 * 
	 * @param bitmap
	 * @param w
	 *            放大或者缩小后照片的宽
	 * @param h 放大或者缩小后照片的高
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		try {
			Bitmap newbmp = null;
			if (bitmap != null) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				if (w == 0 && h == 0) {
					return zoomBitmap(bitmap);
				} else if (w != 0 && h == 0) {
					h = w * height / width;
				}
				Matrix matrix = new Matrix();
				float scaleWidth = (float) w / width;
				float scaleHeight = (float) h / height;
				float scale = scaleWidth > scaleHeight ? scaleWidth
						: scaleHeight;
				// 按照所需长宽与实际长宽比较大者进行缩放
				if (scale > 1) {
					matrix.postScale(scaleWidth, scaleHeight);
					newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
							matrix, true);
					width = newbmp.getWidth();
					height = newbmp.getHeight();
				} else {
					newbmp = bitmap;
				}
				int startX, startY;
				startX = width > height ? (width - w) / 2 : 0;
				startY = height > width ? (height - h) / 2 : 0;
				// 按照所需长宽进行剪切
				newbmp = Bitmap.createBitmap(newbmp, startX, startY, w, h,
						matrix, true);
			}
			return newbmp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap zoomBitmap(Bitmap bm) {
		if (bm != null) {
			Matrix matrix = new Matrix();
			matrix.postScale(1f, 1f);
			int width = bm.getWidth();
			int height = bm.getHeight();
			int len = width > height ? height : width;
			int startX, startY;
			startX = width > height ? (width - height) / 2 : 0;
			startY = height > width ? (height - width) / 2 : 0;
			Bitmap tmp = Bitmap.createBitmap(bm, startX, startY, len, len,
					matrix, true);
			return tmp;
		}
		return null;
	}
}
