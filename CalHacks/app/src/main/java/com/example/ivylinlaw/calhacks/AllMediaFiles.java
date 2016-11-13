package com.example.ivylinlaw.calhacks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ivylinlaw.calhacks.helper.ImageHelper;
import com.google.gson.Gson;
import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.FaceRectangle;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AllMediaFiles extends Activity {
	private GridView gvAllImages;
	private HashMap<String, String> userInfo;
	private ArrayList<String> imageThumbList = new ArrayList<String>();
	private Context context;
	private int WHAT_FINALIZE = 0;
	private static int WHAT_ERROR = 1;
	private ProgressDialog pd;
	public static final String TAG_DATA = "data";
	public static final String TAG_IMAGES = "images";
	public static final String TAG_THUMBNAIL = "thumbnail";
	public static final String TAG_URL = "url";
	private Uri mImageUri;
	private ArrayList<List<RecognizeResult>> recognizedResults;
	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (pd != null && pd.isShowing())
				pd.dismiss();
			if (msg.what == WHAT_FINALIZE) {
				setImageGridAdapter();
			} else {
				Toast.makeText(context, "Check your network.",
						Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_media_list_files);
		gvAllImages = (GridView) findViewById(R.id.gvAllImages);
		userInfo = (HashMap<String, String>) getIntent().getSerializableExtra(
				"userInfo");

		context = AllMediaFiles.this;
		getAllMediaImages();
	}

	private void setImageGridAdapter() {
		gvAllImages.setAdapter(new MyGridListAdapter(context, imageThumbList));
	}

	//		gvAllImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//				ImageView imageView = (ImageView)v;
//				imageView.buildDrawingCache();
//				Bitmap image= imageView.getDrawingCache();
//
//				Bundle extras = new Bundle();
//				extras.putParcelable("imagebitmap", image);
//				Intent intent = new Intent();
//				intent.putExtras(extras);
//				MediaFileAnalyzeActivity MFAA = new MediaFileAnalyzeActivity();
//				MFAA.startActivity(intent);
//
////				String faceSubscriptionKey = getString(R.string.faceSubscription_key);
////				if (faceSubscriptionKey.equalsIgnoreCase("Please_add_the_face_subscription_key_here")) {
////					Log.d("no face subscription key", "There is no face subscription key in res/values/strings.xml. Skip the sample for detecting emotions using face rectangles");
////				} else {
////					// Do emotion detection using face rectangles provided by Face API.
////					try {
////						if(recognizedResults==null){
////							System.out.println("what the fuck");
////						}
////					} catch (Exception e) {
////						Log.d("exception", "Error encountered. Exception is: " + e.toString());
////					}
////				}
//////				for(int i = 0 ; i<recognizedResults.size();i++) {
////				List<RecognizeResult> resultss = recognizedResults.get(position);
////				System.out.println(resultss.size());
////				Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
////				paint.setStyle(Paint.Style.STROKE);
////				paint.setStrokeWidth(5);
////				paint.setColor(Color.RED);
////				// set up draw for emoji(s)
////				ImageView imageView = (ImageView) v;
////				BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
////				Bitmap bitmap = drawable.getBitmap();
////				Canvas canvas = new Canvas(bitmap);
////				for (RecognizeResult r : resultss) {
////					System.out.println("YES!!!"+resultss.size());
//////					mEditText.append(String.format("\nFace #%1$d \n", count));
//////					mEditText.append(String.format("\t anger: %1$.5f\n", r.scores.anger));
//////					mEditText.append(String.format("\t contempt: %1$.5f\n", r.scores.contempt));
//////					mEditText.append(String.format("\t disgust: %1$.5f\n", r.scores.disgust));
//////					mEditText.append(String.format("\t fear: %1$.5f\n", r.scores.fear));
//////					mEditText.append(String.format("\t happiness: %1$.5f\n", r.scores.happiness));
//////					mEditText.append(String.format("\t neutral: %1$.5f\n", r.scores.neutral));
//////					mEditText.append(String.format("\t sadness: %1$.5f\n", r.scores.sadness));
//////					mEditText.append(String.format("\t surprise: %1$.5f\n", r.scores.surprise));
//////					mEditText.append(String.format("\t face rectangle: %d, %d, %d, %d", r.faceRectangle.left, r.faceRectangle.top, r.faceRectangle.width, r.faceRectangle.height));
//////					faceCanvas.drawRect(r.faceRectangle.left,
//////							r.faceRectangle.top,
//////							r.faceRectangle.left + r.faceRectangle.width,
//////							r.faceRectangle.top + r.faceRectangle.height,
//////							paint);
////						// draw emoji(s) on imageView
////					Drawable myDrawable = ContextCompat.getDrawable(getBaseContext(), R.drawable.profile);
//////                        Drawable myDrawable = getResources().getDrawable(R.drawable.profile);
////					Bitmap poopBitmap = ((BitmapDrawable) myDrawable).getBitmap();
////					poopBitmap = Bitmap.createScaledBitmap(poopBitmap, r.faceRectangle.width, r.faceRectangle.height, true);
////					canvas.drawBitmap(poopBitmap, r.faceRectangle.left, r.faceRectangle.top, paint);
////					imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
////				}
//			}
//		});
//	}
//
	private void getAllMediaImages() {

		pd = ProgressDialog.show(context, "", "Loading images...");
		new Thread(new Runnable() {

			@Override
			public void run() {

				int what = WHAT_FINALIZE;
				try {
					// URL url = new URL(mTokenUrl + "&code=" + code);
//                    Log.d("fetch images", "GG");

					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = jsonParser
							.getJSONFromUrlByGet("https://api.instagram.com/v1/users/"
									+ userInfo.get(InstagramApp.TAG_ID)
									+ "/media/recent/?access_token="
									+ ApplicationData.CLIENT_ID
									+ "&access_token="
									+ userInfo.get("ACCESS_TOKEN"));


					JSONArray data = jsonObject.getJSONArray(TAG_DATA);
					for (int data_i = 0; data_i < data.length(); data_i++) {
						JSONObject data_obj = data.getJSONObject(data_i);

						JSONObject images_obj = data_obj
								.getJSONObject(TAG_IMAGES);

						JSONObject thumbnail_obj = images_obj
								.getJSONObject(TAG_THUMBNAIL);

						// String str_height =
						// thumbnail_obj.getString(TAG_HEIGHT);
						//
						// String str_width =
						// thumbnail_obj.getString(TAG_WIDTH);

						String str_url = thumbnail_obj.getString(TAG_URL);
						imageThumbList.add(str_url);

					}

					System.out.println("jsonObject::" + jsonObject);
				} catch (Exception exception) {
					exception.printStackTrace();
					what = WHAT_ERROR;
				}
				// pd.dismiss();
				handler.sendEmptyMessage(what);
			}
		}).start();
	}
}


//	private class yijianfenxi extends AsyncTask<String, Void, ArrayList<List<RecognizeResult>>> {
//		// Store error message
//		private Exception e = null;
//		private boolean useFaceRectangles = false;
//		private EmotionServiceClient client;
//		private ArrayList<String> uriList;
//
//
//		public yijianfenxi(ArrayList<String> uriList) {
//
////			this.useFaceRectangles = useFaceRectangles;
//			this.uriList = uriList;
//			Log.d("DEBUG", "2"+String.valueOf(uriList.size()));
//
//			if (client == null) {
//				client = new EmotionServiceRestClient(getString(R.string.subscription_key));
//			}
//		}
//
//
//		private List<RecognizeResult> processWithAutoFaceDetection (String uri_String) throws EmotionServiceException, IOException, ClientException {
//			Log.d("DEBUG", "1 "+uri_String);
////			Uri imageUri = Uri.parse("file:" + uri_String);
////			File file = new File(uri_String);
////			Uri uri1 = Uri.fromFile(file);
//
//			Bitmap mBitmap = null;
//			try {
//				URL url = new URL(uri_String);
//				mBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//			} catch(IOException e) {
//				System.out.println(e);
//			}
//
////			Bitmap mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
//			if(mBitmap==null){
//				Log.d("DEBUG5:", "NOBITMAP");
//			}
//			Log.d("emotion", "Start emotion detection with auto-face detection");
//			Gson gson = new Gson();
//			// Put the image into an input stream for detection.
//			ByteArrayOutputStream output = new ByteArrayOutputStream();
//			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
//			ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
//
//			long timeMark = System.currentTimeMillis();
//			Log.d("emotion", "Start face detection using Face API");
//			FaceRectangle[] faceRectangles = null;
//			String faceSubscriptionKey = getString(R.string.faceSubscription_key);
//			FaceServiceRestClient faceClient = new FaceServiceRestClient(faceSubscriptionKey);
//			Face faces[] = faceClient.detect(inputStream, false, false, null);
//			Log.d("emotion", String.format("Face detection is done. Elapsed time: %d ms", (System.currentTimeMillis() - timeMark)));
//
//			if (faces != null) {
//				faceRectangles = new FaceRectangle[faces.length];
//
//				for (int i = 0; i < faceRectangles.length; i++) {
//					// Face API and Emotion API have different FaceRectangle definition. Do the conversion.
//					com.microsoft.projectoxford.face.contract.FaceRectangle rect = faces[i].faceRectangle;
//					faceRectangles[i] = new com.microsoft.projectoxford.emotion.contract.FaceRectangle(rect.left, rect.top, rect.width, rect.height);
//				}
//			}
//
//			List<RecognizeResult> result = null;
//			if (faceRectangles != null) {
//				inputStream.reset();
//
//				timeMark = System.currentTimeMillis();
//				Log.d("emotion", "Start emotion detection using Emotion API");
//				// -----------------------------------------------------------------------
//				// KEY SAMPLE CODE STARTS HERE
//				// -----------------------------------------------------------------------
//				result = this.client.recognizeImage(inputStream, faceRectangles);
//
//				String json = gson.toJson(result);
//				Log.d("result", json);
//				// -----------------------------------------------------------------------
//				// KEY SAMPLE CODE ENDS HERE
//				// -----------------------------------------------------------------------
//				Log.d("emotion", String.format("Emotion detection is done. Elapsed time: %d ms", (System.currentTimeMillis() - timeMark)));
//			}
//			return result;
//		}
//
//
//
//
//		@Override
//		protected ArrayList<List<RecognizeResult>> doInBackground(String... args) {
//
//			ArrayList<List<RecognizeResult>> result = new ArrayList<>();
//			for(int i = 0; i<uriList.size();i++){
//				try {
//					List<RecognizeResult> resultList = processWithAutoFaceDetection(uriList.get(i));
//					result.add(resultList);
//					System.out.println("RESULTSIZE "+result.size());
//				} catch (EmotionServiceException e1) {
//					e1.printStackTrace();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				} catch (ClientException e1) {
//					e1.printStackTrace();
//				}
//			}
//			return result;
//		}
//
//
//
//	@Override
//		protected void onPostExecute(ArrayList<List<RecognizeResult>> result) {
//			recognizedResults = result;
//		}
//	}
