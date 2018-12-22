package com.example.franco.testids;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.tensorflow.lite.Interpreter;

public class MainActivity extends Activity {

    final float[][] tensorOutput = new float[][]{{0,0}};
    final float[][] tensorInput = new float[1][48];
    List<Float> headerFrequency = new ArrayList<Float>();
    String modelFile="BINKS.tflite";
    String PID;
    Interpreter tflite;
    EditText input;
    Button btn;
    Button straceBtn;
    Button preprocess;
    Button predict;
    TextView out;
    TextView tensorOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sets the UI components for interactivity and display
        input = (EditText) findViewById(R.id.txt);
        btn = (Button) findViewById(R.id.btn);
        straceBtn = (Button) findViewById(R.id.straceBtn);
        preprocess = (Button) findViewById(R.id.preprocess);
        predict = (Button) findViewById(R.id.predict);
        out = (TextView) findViewById(R.id.out);
        out.setMovementMethod(new ScrollingMovementMethod());
        tensorOut = (TextView) findViewById(R.id.tensorOut);

        //Runs ps command to display PID for user
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AdbCommands exe = new AdbCommands();
                String output = exe.runPSCommand();
                out.setText(output);
            }
        });

        //Grabs PID from user and run command to generate logs
        straceBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AdbCommands exe = new AdbCommands();
                PID = input.getText().toString();
                exe.runStraceCommand(PID);
                String output = "Please close the application being diagnosed";
                out.setText(output);
            }
        });

        //This converts the strace log files into an array of floats to pass to BINKS
        preprocess.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Preprocessing preprocessing = new Preprocessing();
                headerFrequency = preprocessing.frequencyAnalysis(preprocessing.readLogFile("data/local/tmp/logs.txt"));
                out.setText("Please Press Predict");
                //moves the frequency matrix into a format for BINKS
                for(int i = 0; i < headerFrequency.size(); i++)
                {
                    tensorInput[0][i] = headerFrequency.get(i);
                }
            }
        });

        //creates testable instance of AI
        try{
            tflite = new Interpreter(loadModelFile(MainActivity.this, modelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this is where the test runs and outputs right next to the preprocessing button
        predict.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                 tflite.run(tensorInput,tensorOutput);
                 if(tensorOutput[0][0] < tensorOutput[0][1])
                 {
                     tensorOut.setText("Malware Detected");
                 }
                 else {
                     tensorOut.setText("Safe Application");
                 }
            }
        });
    }
    /*
    Function to load the tensor onto the phone
     */
    private MappedByteBuffer loadModelFile(Activity activity, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}