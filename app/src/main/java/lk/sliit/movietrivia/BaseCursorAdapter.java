package lk.sliit.movietrivia;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class BaseCursorAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIDColumn;

    public abstract void onBindViewHolder(V holder, Cursor cursor);

    public BaseCursorAdapter(Cursor cursor) {
        setHasStableIds(true);
        swapCursor(cursor);
    }



    @Override
    public void onBindViewHolder(@NonNull V holder, int position) {
        if (!mDataValid){
            throw new IllegalStateException("Cannot bind view holder when cursor us in invalid state");
        }
        if (!mCursor.moveToPosition(position)){
            throw new IllegalStateException("Cannot move cursor to position " + position);
        }

        onBindViewHolder(holder,mCursor);
    }

    @Override
    public int getItemCount() {
        if(mDataValid){
            return mCursor.getCount();
        } else {
            return 0;
        }

    }

    @Override
    public long getItemId(int position) {

        if (!mDataValid){
            throw new IllegalStateException("Cannot bind view holder when cursor us in invalid state");
        }
        if (!mCursor.moveToPosition(position)){
            throw new IllegalStateException("Cannot move cursor to position " + position);
        }

        return mCursor.getLong(mRowIDColumn);
    }

    public Cursor getItem(int position){
        if (!mDataValid){
            throw new IllegalStateException("Cannot bind view holder when cursor us in invalid state");
        }
        if (!mCursor.moveToPosition(position)){
            throw new IllegalStateException("Cannot move cursor to position " + position);
        }
        return mCursor;
    }

    public void swapCursor(Cursor cursor) {
        if(cursor == mCursor){
            return;
        }
        if(cursor != null){
            mCursor = cursor;
            mDataValid = true;
            //notify observer
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0,getItemCount());
            mCursor = null;
            mRowIDColumn = -1;
            mDataValid = false;
        }
    }
}
