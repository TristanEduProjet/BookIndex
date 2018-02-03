package fr.esgi.bookindex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import java9.util.stream.Collectors;
import java9.util.stream.IntStream;

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

        this.cameraView = this.findViewById(R.id.surfaceView);
        this.cameraScanRes = this.findViewById(R.id.surfaceText);

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
                    } catch(final IOException ex) {
                        Log.e("CameraSource", "problem with holder", ex);
                    }
                }
                @Override
                public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
                }
                @Override
                public void surfaceDestroyed(final SurfaceHolder holder) {
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
                        ScanActivity.this.cameraScanRes.post(() -> ScanActivity.this.cameraScanRes.setText( // ✓ \u2713  ✔ \u2714  ✘ \u2718  ✗ \u2717  ► \u25ba  ▷ \u25b7
                                    IntStream.range(0, barcodes.size()).mapToObj(barcodes::valueAt)
                                    .map(bc -> (bc.valueFormat == Barcode.ISBN ? '\u25ba' : '\u25b7')+' '+bc.displayValue)
                                    .collect(Collectors.joining("\n")))
                        );
                        IntStream.range(0, barcodes.size()).mapToObj(barcodes::valueAt).filter(bc -> bc.valueFormat == Barcode.ISBN).findFirst()
                                .ifPresent(bc -> {
                                    ScanActivity.this.setResult(ScanActivity.RES_OK,  new Intent().putExtra(FIELD_BCODE, bc.rawValue));
                                    ScanActivity.this.finish();
                                });
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
