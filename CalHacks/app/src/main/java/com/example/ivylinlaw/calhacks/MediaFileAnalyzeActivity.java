package com.example.ivylinlaw.calhacks;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.example.ivylinlaw.calhacks.helper.EmojiParser;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;

import java.util.ArrayList;
import java.util.List;

public class MediaFileAnalyzeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_file_analyze);
    }



    // From RecognizeActivity
    private class doRequest extends AsyncTask<String, String, List<RecognizeResult>> {
        // Store error message
        private Exception e = null;
        private ArrayList<RecognizeResult> mResult;
//        private boolean useFaceRectangles = false;

        public doRequest(List<RecognizeResult> inResult) {
//            this.useFaceRectangles = useFaceRectangles;
            mResult = new ArrayList<RecognizeResult>(inResult);
        }

        @Override
        protected List<RecognizeResult> doInBackground(String... args) {
//            if (this.useFaceRectangles == false) {
//                try {
//                    return processWithAutoFaceDetection();
//                } catch (Exception e) {
//                    this.e = e;    // Store error
//                }
//            } else {
//                try {
//                    return processWithFaceRectangles();
//                } catch (Exception e) {
//                    this.e = e;    // Store error
//                }
//            }
            return null;
        }

        @Override
        protected void onPostExecute(List<RecognizeResult> result) {
            super.onPostExecute(result);
            // Display based on error existence

            if (this.useFaceRectangles == false) {
                mEditText.append("\n\nRecognizing emotions with auto-detected face rectangles...\n");
            } else {
                mEditText.append("\n\nRecognizing emotions with existing face rectangles from Face API...\n");
            }
            if (e != null) {
                mEditText.setText("Error: " + e.getMessage());
                this.e = null;
            } else {
                if (result.size() == 0) {
                    mEditText.append("No emotion detected :(");
                } else {
                    Integer count = 0;
                    // Covert bitmap to a mutable bitmap by copying it
                    Bitmap bitmapCopy = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas faceCanvas = new Canvas(bitmapCopy);
                    faceCanvas.drawBitmap(mBitmap, 0, 0, null);
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(5);
                    paint.setColor(Color.RED);

                    // set up draw for emoji(s)
                    ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    Canvas canvas = new Canvas(bitmap);

                    for (RecognizeResult r : result) {
                        mEditText.append(String.format("\nFace #%1$d \n", count));
                        mEditText.append(String.format("\t anger: %1$.5f\n", r.scores.anger));
                        mEditText.append(String.format("\t contempt: %1$.5f\n", r.scores.contempt));
                        mEditText.append(String.format("\t disgust: %1$.5f\n", r.scores.disgust));
                        mEditText.append(String.format("\t fear: %1$.5f\n", r.scores.fear));
                        mEditText.append(String.format("\t happiness: %1$.5f\n", r.scores.happiness));
                        mEditText.append(String.format("\t neutral: %1$.5f\n", r.scores.neutral));
                        mEditText.append(String.format("\t sadness: %1$.5f\n", r.scores.sadness));
                        mEditText.append(String.format("\t surprise: %1$.5f\n", r.scores.surprise));
                        mEditText.append(String.format("\t face rectangle: %d, %d, %d, %d", r.faceRectangle.left, r.faceRectangle.top, r.faceRectangle.width, r.faceRectangle.height));
                        faceCanvas.drawRect(r.faceRectangle.left,
                                r.faceRectangle.top,
                                r.faceRectangle.left + r.faceRectangle.width,
                                r.faceRectangle.top + r.faceRectangle.height,
                                paint);

                        // draw emoji(s) on imageView
                        int getEmoji = EmojiParser.getEmojibyScore(r.scores);

                        Drawable myDrawable = ContextCompat.getDrawable(getBaseContext(), getEmoji);
//                        Drawable myDrawable = getResources().getDrawable(R.drawable.profile);
                        Bitmap poopBitmap = ((BitmapDrawable) myDrawable).getBitmap();
                        poopBitmap = Bitmap.createScaledBitmap(poopBitmap, r.faceRectangle.width, r.faceRectangle.height, true);
                        canvas.drawBitmap(poopBitmap, r.faceRectangle.left, r.faceRectangle.top, paint);

                        count++;
                    }
//                    ImageView imageView = (ImageView) findViewById(R.id.selectedImage);
                    imageView.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                }
                mEditText.setSelection(0);
            }

            mButtonSelectImage.setEnabled(true);
        }
    }
}
