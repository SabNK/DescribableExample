package ru.polescanner.describableexample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.polescanner.describableexample.R;
import ru.polescanner.describableexample.domain.base.BaseDescription;
import ru.polescanner.describableexample.domain.base.DescriptionFileImpl;
import ru.polescanner.describableexample.domain.base.DescriptionIO;
import ru.polescanner.describableexample.domain.base.DescriptionUtility;
import ru.polescanner.describableexample.domain.base.DevConstants;
import ru.polescanner.describableexample.domain.base.WeFacedExternalStorageProblems;
import ru.polescanner.describableexample.domain.base.WeHaveNoFile;

//ToDo discuss async operations with load multimedia from Server
public class DescriptionAdapter extends RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder> {
    private static final String TAG = "DescriptionAdapter";
    private static final Map<Integer, Integer> VIEW_TYPE__LAYOUT = initViewTypeLayoutMap();
    private static final Map<String, String> ITEM__VIEW_HOLDER = initItemViewHolderMap();
    private static final Map<Integer, String> VIEW_TYPE__VIEW_HOLDER = initViewTypeViewHolderMap();

    private static Map<Integer, String> initViewTypeViewHolderMap() {
        Map<Integer, String> map = new HashMap<>();
        for (Map.Entry<String, Integer> item : DevConstants.ITEM__VIEW_TYPE.entrySet()) {
            map.put(item.getValue(), ITEM__VIEW_HOLDER.get(item.getKey()));
        }
        return Collections.unmodifiableMap(map);
    }

    private static Map<String, String> initItemViewHolderMap() {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, Integer> item : DevConstants.ITEM__VIEW_TYPE.entrySet()) {
            map.put(item.getKey(), item.getKey() + "ViewHolder");
        }
        return Collections.unmodifiableMap(map);
    }

    private static Map<Integer, Integer> initViewTypeLayoutMap() {
        Map<Integer, Integer> map = new HashMap<>();
        for (Map.Entry<String, Integer> item : DevConstants.ITEM__VIEW_TYPE.entrySet()) {
            Integer layout = DevConstants.ITEM__LAYOUT.get(item.getKey());
            map.put(item.getValue(), layout);
        }
        return Collections.unmodifiableMap(map);
    }

    private Context context;
    private List<BaseDescription> descriptions;
    private BaseDescription description;
    private List<GestureDetector> gestureDetectors;

    public DescriptionAdapter(Context context, List<BaseDescription> descriptions) {
        this.context = context;
        this.descriptions = descriptions;
        this.descriptions.add(0, createAddDescriptionStub(context));
        this.gestureDetectors = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return DevConstants.ITEM__VIEW_TYPE.get(descriptions.get(position).getClass().getSimpleName());
    }

    @NonNull
    @Override
    public DescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(VIEW_TYPE__LAYOUT.get(viewType),
                                                             parent,
                                                             false);
        return newInstance(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull final DescriptionViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        BaseDescription currDescription = descriptions.get(position);
        Glide.with(context)
                .asBitmap()
                .load(currDescription.thumbnail())
                .into(holder.ivDescriptionThumbnail);
        holder.tvDescriptionMetadata.setText(currDescription.metadata());
        holder.setDot(currDescription.isStored(context));
        gestureDetectors.add(new GestureDetector(context, new DescriptionGestureListener(holder)));
        holder.cvDescriptionItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int position = holder.getAdapterPosition();
                description = descriptions.get(position);
                GestureDetector gestureDetector = gestureDetectors.get(position);
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    private DescriptionViewHolder newInstance(@NonNull View itemView, int viewType){
        Object o = null;
        String outerClassName = this.getClass().getName();
        try {
            Class<? extends DescriptionViewHolder> c = (Class<? extends DescriptionViewHolder>)
                    Class.forName(outerClassName +"$" + VIEW_TYPE__VIEW_HOLDER.get(viewType));

            Constructor<? extends DescriptionViewHolder> ctor =
                    c.getDeclaredConstructor(new Class<?>[]{this.getClass(), View.class});
            o = ctor.newInstance(new Object[] {this, itemView});
        } catch (ClassNotFoundException e ) {
            Log.d(TAG, "newInstance: " + "ClassNotFoundException");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            Log.d(TAG, "newInstance: " + "NoSuchMethodException");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        DescriptionViewHolder d = (DescriptionViewHolder) o;
        return d;
    }

    private BaseDescription createAddDescriptionStub(Context context){
        Bitmap bm = getBitmap(R.drawable.ic_add_description);
        BaseDescription.Metadata metadata = new BaseDescription.Metadata("ADD NOW!");
        String filename = DescriptionUtility.saveBitmapToFile(bm, context);
        DescriptionIO utility = new DescriptionUtility(context);
        String hash = utility.hash(filename, null);
        DescriptionFileImpl file = new DescriptionFileImpl(filename, hash);
        return new AddBaseDescription(DescriptionUtility.getThumbnail(filename, context),
                                      metadata,
                                      file);
    };

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    abstract class DescriptionViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "DescriptionViewHolder";
        ImageView ivDescriptionThumbnail;
        TextView tvDescriptionMetadata;
        CardView cvDescriptionItem;
        ImageView ivDescriptionIsStored;
        CircularProgressIndicator cpiDescriptionDownload;

        public DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDescriptionThumbnail = itemView.findViewById(R.id.ivDescriptionThumbnail);
            tvDescriptionMetadata = itemView.findViewById(R.id.tvDescriptionMetadata);
            ivDescriptionIsStored = itemView.findViewById(R.id.ivDescriptionIsStored);
            cpiDescriptionDownload = itemView.findViewById(R.id.cpiDescriptionDownload);
        }

        public void setDot(boolean isStored){
            if (isStored)
                this.ivDescriptionIsStored.setImageResource(R.drawable.circle_is_stored);
            else
                this.ivDescriptionIsStored.setImageResource(R.drawable.circle_is_not_stored);
        }
    }

    public class ImagePortraitViewHolder extends DescriptionViewHolder {

        public ImagePortraitViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDescriptionItem = itemView.findViewById(R.id.cvDescriptionImagePortraitItem);
        }
    }

    public class ImageLandscapeViewHolder extends DescriptionViewHolder {

        public ImageLandscapeViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDescriptionItem = itemView.findViewById(R.id.cvDescriptionImageLandscapeItem);
        }
    }

    public class VideoViewHolder extends DescriptionViewHolder {

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDescriptionItem = itemView.findViewById(R.id.cvDescriptionVideoItem);
        }
    }

    public class AudioViewHolder extends DescriptionViewHolder {

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDescriptionItem = itemView.findViewById(R.id.cvDescriptionAudioItem);
        }
    }

    public class NoteViewHolder extends DescriptionViewHolder {

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDescriptionItem = itemView.findViewById(R.id.cvDescriptionNoteItem);
        }
    }

    public class AddDescriptionViewHolder extends DescriptionViewHolder {

        public AddDescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            cvDescriptionItem = itemView.findViewById(R.id.cvDescriptionAddStubItem);
        }

    }

    //ToDo make a res with an icon +add multimedia
    private class AddBaseDescription extends BaseDescription {
        private AddBaseDescription(@NonNull Bitmap thumbnail,
                                   @NonNull Metadata metadata,
                                   @NonNull DescriptionFileImpl file) {
            super(thumbnail, metadata, file);
        }

        @Override
        protected String intentType() {
            return null;
        }
    }

    private class DescriptionGestureListener extends GestureDetector.SimpleOnGestureListener{
        private static final int DESCRIPTION_SWIPE_THRESHOLD = 50;
        private static final int DESCRIPTION_SWIPE_VELOCITY_THRESHOLD = 50;
        DescriptionViewHolder holder;

        public DescriptionGestureListener(DescriptionViewHolder holder) {
            super();
            this.holder = holder;
        }

        //needed to consume events - see
        // https://stackoverflow.com/questions/23122411/the-meaning-of-returning-false-from-ongesturelistener-ondown
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        //ToDo setAvatar
        @Override
        public void onLongPress(MotionEvent e) {
            Toast.makeText(context, description.metadata() + " Long Press", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(context, description.metadata() + " Double Tap", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (description.isStored(context)) {
                Intent intent;
                try {
                    intent = description.explore(holder.itemView.getContext());
                } catch (WeHaveNoFile ex) {
                    Toast.makeText(context, ex.getMessage()
                            , Toast.LENGTH_SHORT).show();
                    return true;
                } catch (WeFacedExternalStorageProblems ex) {
                    Toast.makeText(context, ex.getMessage()
                            , Toast.LENGTH_SHORT).show();
                    return true;
                }
                context.startActivity(intent);
                Log.d(TAG, "onSingleTapConfirmed: ");

            }
            else showDescriptionDownloadDialog();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            switch (swipeDirection(e1, e2, velocityX, velocityY)) {
                case UP:
                    onSwipeUp();
                    break;
                case DOWN:
                    onSwipeDown();
                    break;
                case LEFT:
                    break;
                case RIGHT:
                    break;
                default:
                    return true;
            }
            return true;
        }

        private SwipeDirection swipeDirection(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) < Math.abs(diffY) )
                if (Math.abs(diffY) > DESCRIPTION_SWIPE_THRESHOLD
                        && Math.abs(velocityY) > DESCRIPTION_SWIPE_VELOCITY_THRESHOLD)
                    return diffY > 0 ? SwipeDirection.DOWN : SwipeDirection.UP;
            else
                if (Math.abs(diffX) > DESCRIPTION_SWIPE_THRESHOLD
                        && Math.abs(velocityX) > DESCRIPTION_SWIPE_VELOCITY_THRESHOLD)
                    return diffX > 0 ? SwipeDirection.RIGHT : SwipeDirection.LEFT;
            return SwipeDirection.NOT_A_SWIPE;
        }

        private void onSwipeUp() {
            Toast.makeText(context, description.metadata() + " On Swipe Up", Toast.LENGTH_SHORT).show();
        }

        private void onSwipeDown() {
            Toast.makeText(context, description.metadata() + " On Swipe Down", Toast.LENGTH_SHORT).show();
        }

        private void showDescriptionDownloadDialog() {
            AlertDialog dialog = new MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
                    .setTitle("Description is not available?")
                    .setMessage("Do you want to try to download data from cloud via current network?"
                                        + " it might take a while in a background")
                    .setIcon(R.drawable.cloud_download_outline)
                    .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startAsyncTask();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            dialog.show();
        }

        public void startAsyncTask(){
            DownloadAsyncTask task = new DownloadAsyncTask(context, holder, description);
            task.execute(10);
        }
        private void downloadImmediately(BaseDescription description) {

        }
    }

    private enum SwipeDirection {UP, RIGHT, DOWN, LEFT, NOT_A_SWIPE};

    private static class DownloadAsyncTask extends AsyncTask<Integer, Integer, String> {
        private WeakReference<Context> contextWeakReference;
        private WeakReference<DescriptionViewHolder> dvhWeakReference;
        private WeakReference<BaseDescription> descriptionWeakReference;


        DownloadAsyncTask(Context context,
                          DescriptionViewHolder descriptionViewHolder,
                          BaseDescription description) {
            this.dvhWeakReference = new WeakReference<>(descriptionViewHolder);
            this.contextWeakReference = new WeakReference<>(context);
            this.descriptionWeakReference = new WeakReference<>(description);
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: ");
            super.onPreExecute();

            DescriptionViewHolder dvh = dvhWeakReference.get();
            if (dvh == null ) return;

            dvh.cpiDescriptionDownload.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Log.d(TAG, "doInBackground: ");
            for (int i = 0; i < integers[0]; i++) {
                publishProgress((i* 100)/integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Finished!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: ");
            DescriptionViewHolder dvh = dvhWeakReference.get();
            if (dvh == null ) return;
            dvh.cpiDescriptionDownload.setProgressCompat(values[0], true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: ");
            Context context = contextWeakReference.get();
            DescriptionViewHolder dvh = dvhWeakReference.get();
            BaseDescription description = descriptionWeakReference.get();

            if (context == null || dvh == null ||  description == null) {
                Log.d(TAG, "onPostExecute: RETURN");
                return;
            }
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onPostExecute: TOAST");

            dvh.cpiDescriptionDownload.setProgress(0);
            dvh.cpiDescriptionDownload.setIndeterminate(true);
            dvh.cpiDescriptionDownload.setVisibility(View.INVISIBLE);
            description.isStored(context);
        }
    }
}
