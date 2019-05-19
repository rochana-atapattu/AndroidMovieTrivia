package lk.sliit.movietrivia;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    private int mCurrentPosition;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    protected abstract void clear();

    public void onBind(int position){
        mCurrentPosition = position;
        clear();
    }

    public int getmCurrentPosition(){
        return mCurrentPosition;
    }
}