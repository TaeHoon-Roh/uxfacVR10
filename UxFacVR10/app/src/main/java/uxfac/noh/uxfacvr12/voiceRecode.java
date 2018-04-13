package uxfac.noh.uxfacvr12;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class voiceRecode {
    Intent intent;
    SpeechRecognizer speechRecognizer;
    Activity activity;
    public int voiceFlag = 0;


    public voiceRecode(StartActivity startActivity) {
        this.activity = startActivity;
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, activity.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-us");

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity);
        speechRecognizer.setRecognitionListener(recognitionListener);
    }

    public void start() {
        speechRecognizer.startListening(intent);
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.i("Voice Class", " Start Voice Class");
            voiceFlag = 1;
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {
            Log.i("Voice Error", " To late");
            voiceFlag = -1;
        }

        @Override
        public void onResults(Bundle results) {
            String str = "";
            str = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(str);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            for (int i = 0; i < mResult.size(); i++)
                Log.i("Voice", "" + i + " result : " + rs[i]);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

}
