//==============================================================================================================================
package gagan.com.communities.utills;


//==============================================================================================================================

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;


//------------------------------------------------------------------------------------------------------------------------------
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


//==============================================================================================================================
public class BitmapDecoderG {
    final public static int REQUESTCODE_CAMERA = 11;
    final public static int REQUESTCODE_GALLERY = 22;

    //--------------------------------------------------------------------------------------------------------------------------
    public static Uri getTemporaryUri() {
        return Uri.fromFile(createTemporaryFile());
    }


    //    ========================================================
    public static String getBytesImage(Context context,Uri imageUri) {
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);


        }
        catch (Exception | Error e) {
            e.printStackTrace();
            return "";
        }
    }

//    ========================================================


    //--------------------------------------------------------------------------------------------------------------------------
    public static Uri getFromData(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = null;

        switch (requestCode) {
            case REQUESTCODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    try {
//                        File tempFile = generatedname;
//                        String tempFileName = tempFile.getAbsolutePath();
//                        InputStream inputStream = contentResolver.openInputStream(Uri.fromFile(generatedname));
//                        FileOutputStream outputStream = new FileOutputStream(tempFileName);
//
//                        Utills.copyStream(inputStream, outputStream);
//
//                        outputStream.close();
//                        inputStream.close();
//
//                        bitmap = decodeFile(tempFileName);


                        return Uri.fromFile(generatedname);

//                        if (tempFile.exists())
//                            tempFile.delete();
                    }
                    catch (Exception error) {
                        error.printStackTrace();
                    }
                }

                break;

            case REQUESTCODE_GALLERY:
                try {


//                    File tempFile = createTemporaryFile();
//                    String filePath =  tempFile.getAbsolutePath();
//
//                    bitmap = decodeFile(filePath);

                    return data.getData();
                }
                catch (Exception error) {
                    error.printStackTrace();
                }

                break;
        }

        return null;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static Bitmap decodeFile(String filePath) {
        int scale = getAproptiateScale(filePath);

        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inSampleSize = scale;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, option);

        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
            );
            Matrix matrix = new Matrix();

            final float DEGREE_90 = 90;
            final float DEGREE_180 = 180;
            final float DEGREE_270 = 270;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                matrix.postRotate(DEGREE_90);

            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                matrix.postRotate(DEGREE_180);

            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                matrix.postRotate(DEGREE_270);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        catch (Throwable error) {
            error.printStackTrace();
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        final int JPEG_QUALITY = 100;

        bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, outStream);

        return bitmap;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static int getAproptiateScale(String filePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, option);

        final int REQUIRED_SIZE = 1024;
        int maximalSideSize = Math.max(option.outWidth, option.outHeight);
        int scale = 1;

        if (maximalSideSize > REQUIRED_SIZE)
            scale = (int) Math.pow(2, Math.ceil(Math.log((double) maximalSideSize / REQUIRED_SIZE) / LOG_2));

        return scale;
    }


    private static String randomName() {

        return System.currentTimeMillis() + "";

    }


    //--------------------------------------------------------------------------------------------------------------------------
    private static File createTemporaryFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), randomName());

                file.createNewFile();

                generatedname = file;

                return file;
            }
            catch (IOException error) {
            }
        }

        return null;
    }


    public static void selectImage(final Context context, final Fragment frag) {
        generatedname = null;
        final CharSequence[] items = {
                "Take Photo", "Choose from Library",
                "Cancel"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getTemporaryUri());

                    if (frag != null) {
                        frag.startActivityForResult(cameraIntent, REQUESTCODE_CAMERA);
                    }
                    else {
                        ((Activity) context).startActivityForResult(cameraIntent, REQUESTCODE_CAMERA);
                    }
                }
                else if (items[item].equals("Choose from Library")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK);

                    pickPhoto.setType("image/*");


                    if (frag != null) {
                        frag.startActivityForResult(pickPhoto, REQUESTCODE_GALLERY);
                    }
                    else {
                        ((Activity) context).startActivityForResult(pickPhoto, REQUESTCODE_GALLERY);
                    }


                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static String temporaryFilePath() {
        return Environment.getExternalStorageDirectory() + "/" + TEMP_PHOTO_FILE;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String TEMP_PHOTO_FILE = "temporary_holderGagan.jpg";
    private static final double LOG_2 = Math.log(2);


    static File generatedname;

}
