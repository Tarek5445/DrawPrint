package mcp.drawprint;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FingerLine mCustomView;
    private int canvasWidth;
    private int canvasHeight;
    ArrayList<int[]> sx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        mCustomView = (FingerLine)findViewById(R.id.FL);


    }
    private void deleteDialog(){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle("Make a New Sheet");
        deleteDialog.setMessage("Are you Sure You Want to Erase All");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                mCustomView.eraseAll();
                dialog.dismiss();
            }
        });
        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        deleteDialog.show();
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.actionbar, menu );
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.printer:
                canvasWidth = mCustomView.getWidth();
                canvasHeight = mCustomView.getHeight();
                sx = mCustomView.sX;

                new AsyncTask<Void,Void,Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        FingerLine fl1= (FingerLine)findViewById(R.id.FL);
                        OutputStream os = null;
                        DataOutputStream output = null;
                        Socket socket = null;
                        try {
                            socket = new Socket("10.0.1.1", 1111);
                            os = socket.getOutputStream();
                            output = new DataOutputStream(os);

                            // send the svg dimensions first
                            String dimension = canvasWidth + " " + canvasHeight;
                            output.writeUTF(dimension);

                            for (int i=0;i<sx.size();i++) {
                                String temp = sx.get(i)[0] + " " + sx.get(i)[1] + " " + sx.get(i)[2] + " " + sx.get(i)[3];
                                output.writeUTF(temp);
                            }



                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally{
                            try {
                                if (os != null)
                                    os.close();
                                if (output != null)
                                    output.close();
                                if (socket != null)
                                    socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        return null;
                    }

                }.execute();


                return true;

            case R.id.newsheet:
                deleteDialog();
                mCustomView.sX.clear();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}