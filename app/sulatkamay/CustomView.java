package balikbayan.box.sulatkamay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CustomView extends View {

    private float size;
    private Paint paint;
    private ArrayList<PointF> points;
    private ArrayList<ArrayList<PointF>> lines;

    public CustomView(Context context) {
        super(context);

        paint = new Paint();
        lines = new ArrayList<ArrayList<PointF>>();
        points = new ArrayList<PointF>();

        size = 7.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int i, j;
        float x=0, y=0;
        ArrayList<PointF> points;
        PointF point;

        canvas.save();

        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(size);

        for(i=0;i<lines.size();i++){

            points = lines.get(i);

            if(points.size() > 0){
                point = points.get(0);
                x = point.x;
                y = point.y;
            }

            for(j=1;j<points.size();j++){

                point = points.get(j);

                canvas.drawLine(x, y, point.x, point.y, paint);

                x = point.x;
                y = point.y;
            }
        }

        canvas.restore();
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
        int index;
        float x, y;

        index = event.getActionIndex();
        x = event.getX(index);
        y = event.getY(index);

        points = new ArrayList<PointF>();
        points.add(new PointF(x, y));

        lines.add(points);

        invalidate();
    }

    private void onActionMove(MotionEvent event) {
        int index;
        float x, y;

        index = event.getActionIndex();
        x = event.getX(index);
        y = event.getY(index);

        points.add(new PointF(x, y));

        invalidate();
    }

    public void delete() {
        int i;
        ArrayList<PointF> points;

        i = lines.size() - 1;
        points = lines.get(i);
        points.clear();
    }

    public void clear() {

        int i;
        ArrayList<PointF> points;

        for(i=0;i<lines.size();i++){

            points = lines.get(i);
            points.clear();

        }

        lines.clear();

        invalidate();
    }

    public void setPenSize(float size) {
        this.size = size;
        invalidate();
    }

    public void open() {

    }

    public void save() {
        int i, j, m, n;
        ArrayList<PointF> points;
        PointF point;

        n = lines.size();

        for (i = 0; i < n; i++) {

            points = lines.get(i);

            m = points.size();

            for (j = 0; j < m; j++) {

                point = points.get(j);

                Log.d("KLGYN", String.format("%10.2f%10.2f", point.x, point.y));
            }
        }
    }

}
