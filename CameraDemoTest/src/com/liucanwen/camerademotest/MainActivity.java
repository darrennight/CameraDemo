package com.liucanwen.camerademotest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * ����demo
 * ���ܣ����ա����á����浽�ֻ���
 * @author ck
 * @since 2014��3��20�� 16:04:57
 */
public class MainActivity extends Activity implements OnClickListener
{
	private ImageView headIv;

	// ����ͷ��
	private static final int IMAGE_REQUEST_CODE = 0; // ������ ����ͼƬ
	private static final int CAMERA_REQUEST_CODE = 1; // ����
	private static final int RESULT_REQUEST_CODE = 2; // �ü�
	private static final String SAVE_AVATORNAME = "head.png";// �����ͼƬ��

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		headIv = (ImageView) findViewById(R.id.image_layout);
		headIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.image_layout:
			showOptionsDialog();
			break;

		default:
			break;
		}
	}

	// ѡ��ͼƬ��Դ
	private void showOptionsDialog()
	{
		String[] items = new String[] { "����", "ѡ�񱾵�ͼƬ" };
		
		DialogInterface.OnClickListener click = new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
				case 0://����
					Intent intentFromCapture = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									SAVE_AVATORNAME)));
					startActivityForResult(intentFromCapture,
							CAMERA_REQUEST_CODE);
					break;
				case 1://ѡ�񱾵�ͼƬ
					Intent intentFromGallery = new Intent();
					intentFromGallery.setType("image/*"); // �����ļ�����
					intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(intentFromGallery,
							IMAGE_REQUEST_CODE);
					break;
				}
			}
		};
		
		new AlertDialog.Builder(this).setItems(items,
				click).show();
	}

	/**
	 * �ص��������
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != RESULT_CANCELED)
		{
			switch (requestCode)
			{
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				startPhotoZoom(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), SAVE_AVATORNAME)));
				break;
			case RESULT_REQUEST_CODE:
				if (data != null)
				{
					getImageToView(data);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * �ü�ͼƬ����ʵ��
	 */
	public void startPhotoZoom(Uri uri)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// ���òü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, RESULT_REQUEST_CODE);
	}

	/**
	 * ����ü�֮���ͼƬ����
	 */
	private void getImageToView(Intent data)
	{
		Bundle extras = data.getExtras();
		if (extras != null)
		{
			Bitmap photo = extras.getParcelable("data");
			saveMyBitmap(photo); // ����ü����ͼƬ��SD
			headIv.setImageBitmap(photo);
		}
	}

	/**
	 * ��ͷ�񱣴���SDcard
	 */
	public void saveMyBitmap(Bitmap bitmap)
	{
		File f = new File(Environment.getExternalStorageDirectory(),
				SAVE_AVATORNAME);
		try
		{
			f.createNewFile();
			FileOutputStream fOut = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

			fOut.flush();
			fOut.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
