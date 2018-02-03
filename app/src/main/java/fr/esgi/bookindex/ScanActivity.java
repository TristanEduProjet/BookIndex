package fr.esgi.bookindex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    public void onStartProcess(final View v) {
        final TextView txtView = (TextView) this.findViewById(R.id.txtContent);
        final ImageView myImageView = (ImageView) findViewById(R.id.imgview);
        Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext()
                //.getResources(), R.raw.puppy);
                .getResources(), R.raw.code_isbn_sans);
        if(myBitmap == null || myBitmap.getWidth()<=0) throw new RuntimeException("Error mybitmap");
        myImageView.setImageBitmap(myBitmap);

        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                        //.setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13)
                        .build();
        if(!detector.isOperational()) {
            txtView.setText("Could not set up the detector!");
            return;
        } else {
            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            txtView.setText(detector.detect(frame).valueAt(0).rawValue);
        }
        //this.finish();
    }
}
