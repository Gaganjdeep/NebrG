package gagan.com.communities.utills;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import gagan.com.communities.R;

public class RoundedCornersGaganImg extends ImageView
{

    private boolean mBlockLayout;

    private int RADIUS = 0;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public RoundedCornersGaganImg(Context context)
    {
        super(context);

    }

    public RoundedCornersGaganImg(Context context, AttributeSet attrs)
    {
        super(context, attrs);

    }

    public RoundedCornersGaganImg(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

    }

    @Override
    public void onDraw(Canvas canvas)
    {
        Drawable maiDrawable = getDrawable();

        if (maiDrawable != null && maiDrawable instanceof BitmapDrawable
                && RADIUS > 0)
        {
            Paint       paint        = ((BitmapDrawable) maiDrawable).getPaint();
            final int   color        = 0xff000000;
            Rect        bitmapBounds = maiDrawable.getBounds();
            final RectF rectF        = new RectF(bitmapBounds);
            // Create an off-screen bitmap to the PorterDuff alpha blending to
            // work right
            int saveCount = canvas.saveLayer(
                    rectF, null,
                    Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                            | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                            | Canvas.CLIP_TO_LAYER_SAVE_FLAG
            );
            // Resize the rounded rect we'll clip by this view's current bounds
            // (super.onDraw() will do something similar with the drawable to
            // draw)
            getImageMatrix().mapRect(rectF);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, RADIUS, RADIUS, paint);

            Xfermode oldMode = paint.getXfermode();
            // This is the paint already associated with the BitmapDrawable that
            // super draws
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            super.onDraw(canvas);
            paint.setXfermode(oldMode);
            canvas.restoreToCount(saveCount);
        }
        else
        {
            super.onDraw(canvas);
        }
    }

    public void setRadius(int radius)
    {
        this.RADIUS = radius;
        setDrawableDefault(R.drawable.user_img);
    }


    @Override
    public void requestLayout()
    {

        if (!mBlockLayout)
        {

            super.requestLayout();

        }

    }

    @Override
    public void setImageResource(int resId)
    {

        mBlockLayout = true;

        super.setImageResource(resId);

        mBlockLayout = false;

    }

    @Override
    public void setImageURI(Uri uri)
    {

        mBlockLayout = true;

        super.setImageURI(uri);

        mBlockLayout = false;

    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {

        mBlockLayout = true;

        super.setImageDrawable(drawable);

        mBlockLayout = false;

    }

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        mBlockLayout = true;

        super.setImageBitmap(bm);

        mBlockLayout = false;

    }


    private int drawable = R.drawable.grey_bg;

    public void setDrawableDefault(int drwableG)
    {
        drawable = drwableG;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    public void setImageUrl(Context con, String URL)
    {


        if (!URL.isEmpty())
        {
//            Picasso.with(con).load(URL).placeholder(drawable).error(drawable).into(this);

            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(drawable)
                    .showImageOnFail(drawable)
                    .showImageOnLoading(drawable).build();


            imageLoader.init(ImageLoaderConfiguration.createDefault(((ContextWrapper) con).getBaseContext()));


            // imageLoader.displayImage(URL, this, options);
            imageLoader.displayImage(URL, this, options, new ImageLoadingListener()
            {

                @Override
                public void onLoadingStarted(String s, View view)
                {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason)
                {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
                {

                    AlphaAnimation scale = new AlphaAnimation(0.1f, 1f);
                    scale.setDuration(500);
                    scale.setInterpolator(new OvershootInterpolator());
                    view.startAnimation(scale);

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view)
                {

                }
            });
        }
        else
        {
            this.setImageResource(drawable);
        }
    }


    public void setImageWithCallBack(Context con, String URL, final CallBackG callBack)
    {


        if (!URL.isEmpty())
        {

            options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(true)
                    .showImageForEmptyUri(drawable)
                    .showImageOnFail(drawable)
                    .showImageOnLoading(drawable).build();


            imageLoader.init(ImageLoaderConfiguration.createDefault(((ContextWrapper) con).getBaseContext()));


            // imageLoader.displayImage(URL, this, options);
            imageLoader.displayImage(URL, this, options, new ImageLoadingListener()
            {

                @Override
                public void onLoadingStarted(String s, View view)
                {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason)
                {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
                {

//                    AlphaAnimation scale = new AlphaAnimation(0.1f, 1f);
//                    scale.setDuration(500);
//                    scale.setInterpolator(new OvershootInterpolator());
//                    view.startAnimation(scale);

                    callBack.OnFinishG(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view)
                {

                }
            });
        }
    }


    //compile 'com.github.bumptech.glide:glide:3.7.0'
    // https://futurestud.io/blog/glide-custom-transformation



}
