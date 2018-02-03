package fr.esgi.bookindex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScanActivity extends AppCompatActivity {
    final static public int REQ_SCAN = 1;
    final static public int RES_ERRINIT = -1;
    final static public int RES_CANCEL = 0;
    final static public int RES_OK = 1;
    final static public int RES_ERR = -2;
    final static public String FIELD_BCODE = "BARECODE";
    final static public String FIELD_ERRDETAIL = "ERROR_DETAIL";

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

        BarcodeDetector detector = new BarcodeDetector.Builder(this.getApplicationContext())
                        //.setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13)
                        .build();
        if(!detector.isOperational()) {
            this.setResult(RES_ERRINIT); //Could not set up the detector!
        } else {
            Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
            SparseArray<Barcode> barcodes = detector.detect(frame);
            if(barcodes.size() > 0)
                this.setResult(RES_OK, new Intent().putExtra(FIELD_BCODE, barcodes.valueAt(0).rawValue));
            else
                this.setResult(RES_ERR, new Intent().putExtra(FIELD_ERRDETAIL, "No codes detected"));
        }
        this.finish();
    }

    @Override
    public void onBackPressed() {
        this.setResult(RES_CANCEL);
        super.onBackPressed();
    }
}
