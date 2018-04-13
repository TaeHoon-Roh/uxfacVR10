package uxfac.noh.uxfacvr12;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import com.google.vr.sdk.base.GvrView;

import java.util.Arrays;

import uxfac.noh.uxfacvr12.ui.camera.GraphicOverlay;

import static android.content.ContentValues.TAG;

public class CameraMethod {

    private CameraDevice cameraDevice;
    private CaptureRequest.Builder previewBuilder;
    private CaptureRequest mPreviewRequest;
    private Handler mBackgroundHandler;
    private CameraCaptureSession previewSession;
    private SurfaceTexture surfaceTexture;
    private StreamConfigurationMap map;
    private Size previewSize;
    private Camera camera;

    private GvrView cameraView;
    private CameraManager cameraManager;
    private String TAG = "CameraMethod";

    public CameraMethod(GvrView cameraView, CameraManager cameraManager, SurfaceTexture surfaceTexture, GraphicOverlay graphicOverlay) {
        this.cameraView = cameraView;
        this.cameraManager = cameraManager;
        this.surfaceTexture = surfaceTexture;
    }

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            startPreview();
        }


        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            Log.d(TAG, "onDisconnected");
        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {
            Log.e(TAG, "onError");
        }


    };


    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            Integer mode = result.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
            Face[] faces = result.get(CaptureResult.STATISTICS_FACES);
            if (faces != null && mode != null) {
                //Log.i("tag", "faces : " + faces.length + " , mode : " + mode);
                if(faces.length != 0){
                    Log.i("Face Check", "Faces : " + faces[0].getMouthPosition().x + " " + faces[0].getMouthPosition().y);
                }
            }



        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }

    };

    //카메라 옵션 설정부분
    public void openCamera() {
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            //Log.i("OpenCamera",": "+cameraId);
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            String str = map.toString();
            //Log.i("OpenCamera",": "+str);
            previewSize = map.getOutputSizes(SurfaceTexture.class)[0];
            //Log.i("OpenCamera",": " +previewSize);
            cameraManager.openCamera(cameraId, stateCallback, cameraView.getHandler());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void startPreview() {
        if (cameraDevice == null) {
            Log.e(TAG, "preview failed");
            return;
        }

        Surface surface = new Surface(surfaceTexture);

        try {
            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        previewBuilder.addTarget(surface);

        try {
            cameraDevice.createCaptureSession(Arrays.asList(surface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            previewSession = session;
                            updatePreview();

                            //face detect 추가
                            // When the session is ready, we start displaying the preview.
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE,
                                        CameraMetadata.STATISTICS_FACE_DETECT_MODE_FULL);
                                // Flash is automatically enabled when necessary.

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = previewBuilder.build();
                                previewSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            Log.e(TAG, "onConfigureFailed");
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    protected void updatePreview() {
        if (null == cameraDevice) {
            Log.e(TAG, "updatePreivew error, return");
        }

        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        HandlerThread thread = new HandlerThread("CameraPreview");
        thread.start();
        Handler backgroundHandler = new Handler(thread.getLooper());

        try {
            previewSession.setRepeatingRequest(previewBuilder.build(), null, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
