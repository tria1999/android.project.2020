package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;

public class ShareActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 100;
    private Bitmap shareImage;
    private ImageView imageView;
    private Uri imageUri;
    private SharePhotoContent content;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private boolean imageSelected;
    private CheckBox shareCheckBox;
    private CheckBox tweetCheckBox;
    private CheckBox postCheckBox;
    private EditText quoteEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        //TODO fix twitter image post
        shareCheckBox = findViewById(R.id.shareCheckBox);
        tweetCheckBox = findViewById(R.id.tweetCheckBox);
        postCheckBox  = findViewById(R.id.postCheckBox);

        imageSelected = false;
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        quoteEditText = findViewById(R.id.quoteEditText);
        quoteEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quoteEditText.setText("");
            }
        });
        //select and display selected image
        imageView = findViewById(R.id.shareImageView);
        Button selectImageButton = findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                }

        });
        //clear image
        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = null;
                imageView.setImageResource(R.drawable.ic_launcher_foreground);
                imageSelected = false;
            }
        });

        final TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager()
                .getActiveSession();




        //post on requested social media
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //to post on instagram
                if (postCheckBox.isChecked()) {
                    if (imageSelected) {
                        shareImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        share.putExtra(Intent.EXTRA_STREAM, imageUri);
                        startActivity(Intent.createChooser(share, "Share to"));

                    }
                }//to share on facebook
                else if (shareCheckBox.isChecked()) {
                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {
                            Toast.makeText(ShareActivity.this, "Shared!", Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onCancel() {
                            Toast.makeText(ShareActivity.this, "Cancelled.", Toast.LENGTH_LONG);
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Toast.makeText(ShareActivity.this, "Error...", Toast.LENGTH_LONG);
                        }
                    });
                    if (imageSelected) {
                        shareImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        SharePhoto photo = new SharePhoto.Builder()
                                .setBitmap(shareImage)
                                .build();
                        SharePhotoContent photoContent = new SharePhotoContent.Builder()
                                .addPhoto(photo)
                                .build();
                        shareDialog.show(photoContent);
                    } else {
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse("https://github.com/UomMobileDevelopment/Project_2020-21"))
                                .setQuote(quoteEditText.getText().toString())
                                .build();
                        shareDialog.show(content);


                    }

                }//to tweet
                if(tweetCheckBox.isChecked()) {
                    if (imageSelected) {
                        if (twitterSession != null)
                        {   System.out.println(imageUri.toString());
                            final Intent intent = new ComposerActivity.Builder(ShareActivity.this)
                                    .session(twitterSession)
                                    .image(imageUri)
                                    .text(quoteEditText.getText().toString())
                                    .hashtags("UOMDAIAndroidProject")
                                    .createIntent();
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(ShareActivity.this,"Not logged in on Twitter!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if (twitterSession != null)
                            {
                                final Intent intent = new ComposerActivity.Builder(ShareActivity.this)
                                    .session(twitterSession)
                                    .text(quoteEditText.getText().toString())
                                    .hashtags("UOMDAIAndroidProject")
                                    .createIntent();
                            startActivity(intent);
                            }
                    else
                        Toast.makeText(ShareActivity.this,"Not logged in on Twitter!", Toast.LENGTH_LONG).show();
                    }

                }
            }

        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            imageSelected = true;

        }
    }


}