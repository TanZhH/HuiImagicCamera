package com.seu.magicfilter.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.present.Present;
import com.seu.magicfilter.utils.MagicParams;

public class SavePictureTask extends AsyncTask<Bitmap, Integer, String>{
	
	private OnPictureSaveListener onPictureSaveListener;
	private File file;

	public SavePictureTask(File file, OnPictureSaveListener listener){
		this.onPictureSaveListener = listener;
		this.file = file;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(final String result) {
		if(result != null) {
			MediaScannerConnection.scanFile(MagicParams.context,
					new String[]{result}, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						@Override
						public void onScanCompleted(final String path, final Uri uri) {
							if (onPictureSaveListener != null) {
								onPictureSaveListener.onSaved(result);
							}
						}
					});
			onPictureSaveListener.setphoto(file);
		}
	}

	@Override
	protected String doInBackground(Bitmap... params) {
		if(file == null) {
			return null;
		}
		return saveBitmap(params[0]);
	}
	
	private String saveBitmap(Bitmap bitmap) {
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			int degrees = Present.degree();
			Bitmap resultBitmap = rotateBitmap(bitmap , degrees);
			resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			bitmap.recycle();
			resultBitmap.recycle();
			return file.toString();
		} catch (FileNotFoundException e) {
		   e.printStackTrace();
		} catch (IOException e) {
		   e.printStackTrace();
		}
		return null;
	}
	
	public interface OnPictureSaveListener{
		void onSaved(String result);
		void setphoto(File file);
	}

	/**
	 * 旋转图片，使图片保持正确的方向。
	 *
	 * @param bitmap 原始图片
	 * @param degrees 原始图片的角度
	 * @return Bitmap 旋转后的图片
	 */
	private Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
		if (degrees == 0 || null == bitmap) {
			return bitmap;
		}
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		return bmp;
	}
}
