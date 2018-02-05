package fr.esgi.bookindex.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import fr.esgi.bookindex.R;
import fr.esgi.bookindex.utils.CameraUtils;
import fr.esgi.bookindex.utils.PermissionUtils;
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
    final static private int PERM_CAMERA = 50;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private CameraView cameraView;
    private TextView cameraScanRes;
    private ProgressBar requestCam;
    private TextView requestCamText;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_scan);
        try {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(final NullPointerException e) { Log.e("ScanActivity", "error with parent activity", e); }

        this.cameraScanRes = this.findViewById(R.id.surfaceText);
        this.cameraView = findViewById(R.id.camera_view);
        this.cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onCameraError(final @NonNull CameraException exception) {
                Log.e("ScanActivity", "Camera error", exception);
            }
        });
        this.cameraView.mapGesture(Gesture.LONG_TAP, GestureAction.FOCUS/*_WITH_MARKER*/);
        //this.cameraView.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER);
        //this.cameraView.setOnClickListener(v -> { if(this.cameraView.isActivated()) this.cameraView.findFocus(); });

        final BarcodeDetector detector = this.barcodeDetector = new BarcodeDetector.Builder(this.getApplicationContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                //.setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13)
                .build();
        if(!detector.isOperational()) {
            this.setResult(RES_ERRINIT); //Could not set up the detector!
            this.finish();
        } else {
            (this.requestCam = this.findViewById(R.id.requestCam)).setVisibility(View.VISIBLE);
            (this.requestCamText = this.findViewById(R.id.requestCamText)).setVisibility(View.VISIBLE);
            //CameraUtils.configureCamera(this);
            /*this.cameraView.addFrameProcessor(f -> detector.detect(new Frame.Builder().setRotation(f.getRotation()).setTimestampMillis(f.getTime())
                                         .setImageData(ByteBuffer.wrap(f.getData()), f.getSize().getWidth(), f.getSize().getWidth(), f.getFormat())
                                         .build()));*/
            this.cameraSource = new CameraSource.Builder(this, detector).setRequestedPreviewSize(1600, 1024).setAutoFocusEnabled(true).build();
            detector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }
                @Override
                public void receiveDetections(final Detector.Detections<Barcode> detections) {
                    Log.d("Detector", "receive detections "+detections.getFrameMetadata().getTimestampMillis());
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                    if (barcodes.size() != 0) {
                        ScanActivity.this.cameraScanRes.post(() -> ScanActivity.this.cameraScanRes.setText( // ✓ \u2713  ✔ \u2714  ✘ \u2718  ✗ \u2717  ► \u25ba  ▷ \u25b7
                                barcodes.valueAt(0).displayValue)
                                /*IntStream.range(0, barcodes.size()).mapToObj(barcodes::valueAt)
                                        .map(bc -> (bc.valueFormat == Barcode.ISBN ? '\u25ba' : '\u25b7')+' '+bc.displayValue)
                                        .collect(Collectors.joining("\n")))*/
                        );
                        /*IntStream.range(0, barcodes.size()).mapToObj(barcodes::valueAt).filter(bc -> bc.valueFormat == Barcode.ISBN).findFirst()
                                .ifPresent(bc -> {
                                    ScanActivity.this.setResult(ScanActivityOld.RES_OK,  new Intent().putExtra(FIELD_BCODE, bc.rawValue));
                                    ScanActivity.this.finish();
                                });*/
                    }
                }
            });
            if(PermissionUtils.checkPermission(this, Manifest.permission.CAMERA, PERM_CAMERA))
                this.initCam(true);
        }
    }

    /**
     * If has camera permissions ... or not
     */
    private void initCam(final boolean hasPerm) {
        if(!hasPerm) {
            this.requestCam.setVisibility(View.VISIBLE);
            this.requestCam.setEnabled(false);
            this.requestCamText.setVisibility(View.VISIBLE);
            this.requestCamText.setText(R.string.perm_not_accepted);
        } else {
            this.requestCamText.setVisibility(View.GONE);
            this.requestCam.setVisibility(View.GONE);
            this.cameraView.start();
            try {
                this.cameraSource.start();
            } catch(final IOException e) { Log.e("ScanActivity", "Error start cameraSource", e); }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(CameraUtils.isGranted(this)) {
            this.cameraView.start();
            try {
                this.cameraSource.start();
            } catch(final IOException e) { Log.e("ScanActivity", "Error start cameraSource", e); }
        } else
            if(PermissionUtils.checkPermission(this, Manifest.permission.CAMERA, PERM_CAMERA))
                this.initCam(true);
    }

    @Override
    protected void onPause() {
        this.cameraView.stop();
        this.cameraSource.stop();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERM_CAMERA:
                this.initCam(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull final Menu menu) {
        this.getMenuInflater().inflate(R.menu.scanner_menu, menu);
        final Switch _switch = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.cameraFlash);
        if(this.getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            _switch.setOnCheckedChangeListener((btnView, isChecked) -> this.cameraView.setFlash(isChecked ? Flash.TORCH : Flash.OFF));
        else
            //_switch.setEnabled(false);
            _switch.setVisibility(View.GONE);
        return true; //super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: // Respond to the action bar's Up/Home button
                //NavUtils.navigateUpFromSameTask(this);
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
        if(this.cameraView != null)
            this.cameraView.destroy();
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
