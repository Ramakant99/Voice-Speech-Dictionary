package com.ramakant9.voicedictionary;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
 
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
 
public class MainActivity extends Activity {
 TextToSpeech tts;
	
	String term,word;
	String url = "https://en.oxforddictionaries.com/definition/";
	ProgressDialog mProgressDialog;
	 private final int CODE = 100;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
 
		
		ImageButton descbutton = (ImageButton) findViewById(R.id.descbutton);
		
	
 
 tts=new TextToSpeech(this, new OnInitListener() {
		
		@Override
		public void onInit(int arg0) {
			// TODO Auto-generated method stub
			
		}
	});

	
	
 		// Capture button click
		descbutton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// Execute Description AsyncTask
				promptSpeechInput();
				
			}
		});
 
 
	}
	
	
	
	
	
	
	 private void promptSpeechInput() {
	        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
	    
	            startActivityForResult(intent, CODE);
	      
	    }
	 
	   
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	 
	        switch (requestCode) {
	        case CODE: {
	            if (resultCode == RESULT_OK && null != data) {
	 
	                ArrayList<String> result = data
	                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	                term=url+result.get(0);
	                word=result.get(0);
	                new Description().execute();
	            }
	            break;
	        }
	 
	        }
	    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	    
		private class Description extends AsyncTask<Void, Void, Void> {
		String desc;
 
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(MainActivity.this);
			mProgressDialog.setTitle("Voice Dictionary");
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
		}
 
		@Override
		protected Void doInBackground(Void... params) {
			try {
		
				
				Document document = Jsoup.connect(term).get();
		
				Elements description = document
						.select("span[class=ind]");
		
				if(description!=null){
				
				desc = description.get(0).text();		        	
				tts.speak(desc,TextToSpeech.QUEUE_ADD ,null);}
		
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
 
		@Override
		protected void onPostExecute(Void result) {
			// Set description into TextView
			TextView txtdesc = (TextView) findViewById(R.id.desctxt);
			TextView txtwrd = (TextView) findViewById(R.id.text);
			txtwrd.setText(word);
			txtdesc.setText(desc);
			mProgressDialog.dismiss();
		
	}
 	}
}