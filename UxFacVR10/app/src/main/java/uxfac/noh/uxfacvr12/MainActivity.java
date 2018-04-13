package uxfac.noh.uxfacvr12;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.Surface;
import android.widget.Toast;

import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

import java.util.Arrays;

import uxfac.noh.uxfacvr12.ui.camera.GraphicOverlay;

import static android.content.ContentValues.TAG;

public class MainActivity extends GvrActivity implements GvrRenderer.GvrRendererEvents{

    private String tag = "VR_Activity";

    private GvrView cameraView;
    private GvrRenderer gvrRenderer;
    private GraphicOverlay graphicOverlay;

    private CameraDevice cameraDevice;
    private CameraManager cameraManager;
    private SurfaceTexture surfaceTexture;
    private CameraMethod cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (GvrView) findViewById(R.id.camera_view);
        graphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);

        setGvrView(cameraView);

        gvrRenderer = new GvrRenderer(cameraView, this);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Toast.makeText(getApplicationContext(), "UpKey", Toast.LENGTH_SHORT).show();
            Log.i(tag,"UpKey");
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Toast.makeText(getApplicationContext(), "DownKey", Toast.LENGTH_SHORT).show();
            Log.i(tag,"DownKey");
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return false;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureCreated(SurfaceTexture surfaceTexture) {
        this.surfaceTexture = surfaceTexture;
        cm = new CameraMethod(cameraView, cameraManager, surfaceTexture, graphicOverlay);
        cm.openCamera();

    }
}
