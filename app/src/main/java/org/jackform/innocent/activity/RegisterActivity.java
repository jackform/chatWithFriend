package org.jackform.innocent.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import org.jackform.innocent.R;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.RegisterTaskRequest;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.widget.AccountEditText;
import org.jackform.innocent.widget.BaseActivity;
import org.jackform.innocent.widget.PasswordEditText;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cropper.CropImageView;

import android.content.CursorLoader;

public class RegisterActivity extends BaseActivity implements DataFetcher.ExecuteListener {
	private static final String TAG = "RegisterActivity";
	private static final int CHOOSE_PICTURE = 1;
	private Button mAlbumChoose, mCaremaChoose;
	private ImageView mHeaderImage;
	private CropImageView cropImg;
	private Bitmap cropedBitmap = null;
	private View vPopWindow = null;
	private PopupWindow popWindow = null;
	private Button mBtnSubmit;
	private AccountEditText mEtAccount;
	private PasswordEditText mEtPassword;
	private PasswordEditText mEtRePassword;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x1:
				if (cropImg != null && cropedBitmap != null) {
					mHeaderImage.setImageBitmap(cropedBitmap);
					popWindow.dismiss();
				}
				break;
			case 0x2:
				break;
			}
		}
	};


	private OnClickListener mOnSubmitClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (null == cropedBitmap) {
				toast("你必须设置头像");
				return;
			}
			String account = mEtAccount.getText().toString();
			String password = mEtPassword.getText().toString();
			String rePassword = mEtRePassword.getText().toString();

			if (TextUtils.isEmpty(account)) {
				toast("必须设置用户名");
				return;
			}
			if (TextUtils.isEmpty(password)) {
				toast("必须设置密码");
				return;
			}
			if (TextUtils.isEmpty(rePassword)) {
				toast("必须再次输入密码");
				return;
			}
			if (!password.equals(rePassword)) {
				toast("两次输入的密码不一致");
				return;
			}
			requestRegister(account,password);
//			SubmitRegisterInfoTask task = new SubmitRegisterInfoTask(account,
//					password);
//			task.start();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);


		mHeaderImage = (ImageView) findViewById(R.id.head_image);
		mAlbumChoose = (Button) findViewById(R.id.album_choose);
		mCaremaChoose = (Button) findViewById(R.id.camera_choose);
		mBtnSubmit = (Button) findViewById(R.id.submit_register_info);
		mBtnSubmit.setOnClickListener(mOnSubmitClickListener);
		mEtAccount = (AccountEditText) findViewById(R.id.et_register_account);
		mEtPassword = (PasswordEditText) findViewById(R.id.et_register_password);
		mEtRePassword = (PasswordEditText) findViewById(R.id.et_regiter_repeat_password);

		final String account = mEtAccount.getText().toString();
		final String password = mEtPassword.getText().toString();

		mAlbumChoose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showCropImagePopWindow(view);
			}
		});
		mCaremaChoose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				requestRegister(account, password);

			}
		});

		mDataFetcher.addExecuteListener(this);
	}


	private void requestRegister(final String account,final String password) {
		new Thread() {
			@Override
			public void run() {
				byte[] bytes = null;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				cropedBitmap
						.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				bytes = baos.toByteArray();
				RegisterTaskRequest request = new RegisterTaskRequest(account,password,bytes);
				mDataFetcher.executeRequest(RegisterActivity.this,RequestConstant.REQUEST_REGISTER,request);
			}
		}.start();
	}
/*
	class SubmitRegisterInfoTask extends Thread {
		private String mAccount;
		private String mPassword;

		public SubmitRegisterInfoTask(String account, String password) {
			mAccount = account;
			mPassword = password;
		}

		@Override
		public void run() {
			
			if (TextUtils.isEmpty(mAccount) || TextUtils.isEmpty(mPassword))
				throw new NullPointerException();
			
			XMPPConnection connection = XmppUtils.getConnection();
			if( null == connection )
			{
				Log.v("connection","con is null");
			}
			Registration reg = new Registration();
			reg.setType(IQ.Type.SET);
			reg.setTo(connection.getServiceName());
			reg.setUsername(mAccount);
			reg.setPassword(mPassword);
			reg.addAttribute("android", "geolo_createUser_android");
			PacketFilter filter = new AndFilter(new PacketIDFilter(
					reg.getPacketID()), new PacketTypeFilter(IQ.class));
			PacketCollector collector = connection
					.createPacketCollector(filter);
			connection.sendPacket(reg);
			IQ result = (IQ) collector.nextResult(SmackConfiguration
					.getPacketReplyTimeout());
			// Stop queuing results
			collector.cancel();// 停止请求results（是否成功的结果）
			if (result == null) {
				toast("服务器没有返回结果");
			} else if (result.getType() == IQ.Type.ERROR) {
				if (result.getError().toString()
						.equalsIgnoreCase("conflict(409)")) {
					toast("这个账号已经存在");
				} else {
					toast("注册失败");
				}
			} else if (result.getType() == IQ.Type.RESULT) {
				
				
				byte[] bytes = null;
				try {

					VCard vcard = new VCard();
					XmppUtils.getConnection().login(mAccount, mPassword);
					vcard.load(XmppUtils.getConnection());

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					cropedBitmap
							.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					bytes = baos.toByteArray();
					String encodedImage = StringUtils.encodeBase64(bytes);
					vcard.setAvatar(bytes, encodedImage);
					vcard.setEncodedImage(encodedImage);
					vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>"
							+ encodedImage + "</BINVAL>", true);
					vcard.save(XmppUtils.getConnection());
					XmppUtils.getConnection().getAccountManager().deleteAccount();
					// XmppUtils.closeConnection();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				toast("恭喜你注册成功");
				finish();
			}
		}
	}
	*/


	protected void showCropImagePopWindow(View parent) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vPopWindow = inflater.inflate(R.layout.layout_crop_image, null, false);
		popWindow = new PopupWindow(vPopWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		ColorDrawable color = new ColorDrawable(Color.TRANSPARENT);
		popWindow.setBackgroundDrawable(color);
		popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		popWindow.setTouchable(true); // //设置PopupWindow可触摸

		cropImg = (CropImageView) vPopWindow.findViewById(R.id.imgView);
		cropImg.setAspectRatio(30, 40);
		cropImg.setFixedAspectRatio(true);

		Button select = (Button) vPopWindow.findViewById(R.id.select);
		Button crop = (Button) vPopWindow.findViewById(R.id.crop);
		Button cancel = (Button) vPopWindow.findViewById(R.id.cancel);

		select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent("android.intent.action.PICK");
				intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, CHOOSE_PICTURE);
			}
		});
		crop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				cropedBitmap = cropImg.getCroppedImage();
				mHandler.sendEmptyMessage(0x1);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				popWindow.dismiss();
			}
		});
		// popWindow.showAsDropDown(parent,-25,10);
		popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	/*
	 * private void cropImageFromAlbum() { Intent intent = new
	 * Intent("android.intent.action.PICK");
	 * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	 * intent.putExtra( "crop", "true"); intent.putExtra( "aspectX", 300);
	 * intent.putExtra( "aspectY", 400); intent.putExtra( "outputX", 300);
	 * intent.putExtra( "outputY", 400); intent.putExtra( "scale", true);
	 * intent.putExtra( "return-data", true);
	 * intent.putExtra("output",Bitmap.CompressFormat.JPEG.toString());
	 * intent.putExtra("noFaceDetection",true);
	 * startActivityForResult(intent,CHOOSE_PICTURE); }
	 */


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, requestCode, data);
		if (RESULT_OK != resultCode)
			return;
		switch (requestCode) {
		case CHOOSE_PICTURE:
			if (data != null) {
				Uri originalUri = data.getData();
				Bitmap photo = null;
				Log.v(TAG, originalUri.toString());
				Log.v(TAG, originalUri.getPath());
				String[] proj = { MediaStore.Images.Media.DATA };
				CursorLoader loader = new CursorLoader(RegisterActivity.this,
						originalUri, proj, null, null, null);
				Cursor cursor = loader.loadInBackground();
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				Log.v(TAG, cursor.getString(column_index));
				ContentResolver resolver = getContentResolver();
				try {
					photo = MediaStore.Images.Media.getBitmap(resolver,
							originalUri);
					if (photo != null) {
						Bitmap newbmp = scaleBitmap(photo);
						// photo.recycle();
						cropImg.setImageBitmap(newbmp);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			break;
		default:
			break;
		}
	}


	/*
	 * private byte[] getFileByte(String fileName) throws IOException {
	 * BufferedInputStream bis = null; byte[] buffer = null; try { File file =
	 * new File(fileName); bis = new BufferedInputStream(new
	 * FileInputStream(file)); int fileLength = (int) file.length(); buffer =
	 * new byte[fileLength]; int readBytes = bis.read(buffer); if (readBytes ==
	 * fileLength) throw new IOException("not read entire file!"); } finally {
	 * if (null != bis) bis.close(); }
	 * 
	 * return buffer; }
	 */

	/*
	 * 缩放原图，使其宽至屏幕的三分之二 缩放比的公式如下： because: picDisplayWidth = 2/3 screenWidth
	 * scale = picDisplayWidth / picOriginalWidth so: scale = 2/3 * screenWidth
	 * / picOriginalWidth
	 */

	private Bitmap scaleBitmap(Bitmap photo) {
		int picOriginalWidth = photo.getWidth();
		int picOriginalHeight = photo.getHeight();
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;

		Matrix matrix = new Matrix();
		float scaleWidth = 2.0f / 3 * screenWidth / picOriginalWidth;
		float scaleHeight = 2.0f / 3 * screenHeight / picOriginalHeight;
		matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(photo, 0, 0, picOriginalWidth,
				picOriginalHeight, matrix, true);
		return newbmp;
	}

	@Override
	public int getCaller() {
		return this.hashCode();
	}

	@Override
	public void onExecuteResult(int responseID, Bundle requestTask) {
		switch(responseID) {
			case ResponseConstant.REGISTER_ID:
				if(requestTask.getString(ResponseConstant.CODE).equals(ResponseConstant.SUCCESS_CODE)){
					toast("注册成功");
					unBindRemoteService();
					finish();
				} else {
					toast("注册失败");
				}
			break;
		}
	}
}
