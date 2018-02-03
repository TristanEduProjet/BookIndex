package fr.esgi.bookindex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {
    final static public int REQ_SCAN = 1;
    final static public int RES_ERRINIT = -1;
    final static public int RES_CANCEL = 0;
    final static public int RES_OK = 1;
    final static public int RES_ERR = -2;
    final static public String FIELD_BCODE = "BARECODE";
    final static public String FIELD_ERRDETAIL = "ERROR_DETAIL";

    private SurfaceView cameraView;
    private TextView cameraScanRes;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        this.cameraView = (SurfaceView) this.findViewById(R.id.surfaceView);
        this.cameraScanRes = (TextView) this.findViewById(R.id.surfaceText);

        final BarcodeDetector detector = this.barcodeDetector = new BarcodeDetector.Builder(this.getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                //.setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13)
                .build();
        if(!detector.isOperational()) {
            this.setResult(RES_ERRINIT); //Could not set up the detector!
            this.finish();
        } else {
            final CameraSource cameraSource = this.cameraSource = new CameraSource.Builder(this, detector)
                    .setRequestedPreviewSize(1600, 1024)
                    .setAutoFocusEnabled(true) //you should add this feature
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(final SurfaceHolder holder) {
                    try {
                        cameraSource.start(cameraView.getHolder()); //noinspection MissingPermission
                    } catch (IOException ex) {
                        Log.e("CameraSource", "problem with holder", ex);
                    }
                }
                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            detector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }
                @Override
                public void receiveDetections(final Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0) {
                        ScanActivity.this.cameraScanRes.post(new Runnable() {
                            @Override
                            public void run() { // → &#x2192;  ✓ \u2713  ✔ \u2714  ✘ \u2718  ✗ \u2717  ► \u25ba  ▷ \u25b7
                                final StringBuilder builder = new StringBuilder();
                                int i;
                                for(i=0 ; i < barcodes.size() ; i++) {
                                    final Barcode barcode = barcodes.valueAt(i);
                                    builder.append(barcode.valueFormat == Barcode.ISBN ? '\u25ba' : '\u25b7').append(' ')
                                            .append(barcode.displayValue).append('\n');
                                }
                                ScanActivity.this.cameraScanRes.setText(builder.deleteCharAt(builder.length()-1).toString());
                            }
                        });
                        int i;
                        for(i=0 ; i < barcodes.size() ; i++) {
                            final Barcode barcode = barcodes.valueAt(i);
                            if(barcode.valueFormat == Barcode.ISBN) {
                                ScanActivity.this.setResult(ScanActivity.RES_OK,  new Intent().putExtra(FIELD_BCODE, barcode.rawValue));
                                ScanActivity.this.finish();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        this.setResult(RES_CANCEL);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(this.cameraSource != null)
            this.cameraSource.release();
        if(this.barcodeDetector != null)
            this.barcodeDetector.release();
        super.onDestroy();
    }
}
