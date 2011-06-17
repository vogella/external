package de.greenrobot.tutorial.maps2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/*
 * Maps/LBS Tutorial.
 * 
 * (c) Copyright Markus Junginger 2010.
 */
public class MarienplatzOverlay extends Overlay {
    private GeoPoint geoPointMarienplatz;
    private GeoPoint geoPointCurrent;
    private Bitmap bitmap;
    private Paint paintLine;

    public MarienplatzOverlay(Activity activity, GeoPoint geoPointMarienplatz) {
        this.geoPointMarienplatz = geoPointMarienplatz;
        bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.munich);
        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(0xff0000aa);
        paintLine.setStrokeWidth(3);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        // TODO 3: Es wird kein Schatten benötigt; in dem Fall einfach return
        Projection projection = mapView.getProjection();
        Point point = new Point();
        // TODO 3: geoPointMarienplatz zu Pixel umrechnen (mit projection in die Variable point)
        // TODO 3: bitmap an den umgerechneten Pixel-Koordinaten zeichnen (canvas.drawBitmap)
        if (geoPointCurrent != null) {
            Point pointCurrent = new Point();
            // TODO 3: geoPointCurrent zu Pixel umrechnen (mit projection in die Variable pointCurrent)
            // TODO 3: Linie zwischen aktueller Position und dem Marienplatz zeichnen (canvas.drawLine)
        }
    }

    public void setGeoPointCurrent(GeoPoint geoPointCurrent) {
        this.geoPointCurrent = geoPointCurrent;
    }

}
