package gr.uom.dai18085_triantafyllos_tsiakiris_project_2020_21;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class ShareActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 100;

    private Button shareButton;
    private Button selectImageButton;
    private String shareText;
    private TextView shareTextView;
    private Bitmap shareImage;
    private ImageView imageView;
    private Uri imageUri;
    private SharePhotoContent content;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private boolean imageSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        imageSelected = false;
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


        imageView = findViewById(R.id.shareImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                }

        });
        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ShareActivity.this,"Shared!", Toast.LENGTH_LONG);

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ShareActivity.this,"Cancelled.", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(ShareActivity.this,"Error...", Toast.LENGTH_LONG);
                    }
                });

                if(imageSelected) {
                    shareImage = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    SharePhoto photo = new SharePhoto.Builder()
                            .setBitmap(shareImage)
                            .build();
                    SharePhotoContent photoContent = new SharePhotoContent.Builder()
                            .addPhoto(photo)
                            .build();
                    shareDialog.show(photoContent);
                }
                else
                {  ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("www.youtube.com"))
                            .setQuote("")
                            .build();
                    shareDialog.show(content);
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