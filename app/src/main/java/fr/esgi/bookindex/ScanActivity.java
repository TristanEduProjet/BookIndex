package fr.esgi.bookindex;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;

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

    //@ViewById(R.id.surfaceView)
    private SurfaceView cameraView;
    //@ViewById(R.id.surfaceText)
    private TextView cameraScanRes;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private Camera camera;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_scan);

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
            this.camera = getCamera(cameraSource);

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
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(R.menu.scanner_menu, menu);
        final Switch _switch = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.cameraFlash);
        if(this.camera!=null && this.getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            _switch.setOnCheckedChangeListener((btnView, isChecked) -> {
                Parameters parameters = this.camera.getParameters();
                parameters.setFlashMode(isChecked ? Parameters.FLASH_MODE_TORCH : Parameters.FLASH_MODE_OFF);
                this.camera.setParameters(parameters);
            }
            /*Toast.makeText(getApplication(), isChecked ? "ON" : "OFF", Toast.LENGTH_SHORT).show()*/);
        else
            _switch.setEnabled(false);
        return true; //super.onCreateOptionsMenu(menu);
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

    @Nullable
    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        for(final Field field : CameraSource.class.getDeclaredFields()) {
            if(field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    final Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null)
                        return camera;
                    else
                        return null;
                } catch(final IllegalAccessException e) {
                    Log.e("ScanActivity", "getCamera", e);
                }
                break;
            }
        }
        return null;
    }
}
