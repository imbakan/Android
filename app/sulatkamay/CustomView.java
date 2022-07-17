package balikbayan.box.sulatkamay;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {

    public CustomView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();

        switch (action){
            case MotionEvent.ACTION_DOWN:         onActionDown(event);      break;
            case MotionEvent.ACTION_MOVE:         onActionMove(event);      break;
            default: return super.onTouchEvent(event);
        }

        return true;
    }

    private void onActionDown(MotionEvent event) {

    }

    private void onActionMove(MotionEvent event) {

    }

}
