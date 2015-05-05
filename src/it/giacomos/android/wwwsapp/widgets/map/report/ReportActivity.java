package it.giacomos.android.wwwsapp.widgets.map.report;

import it.giacomos.android.wwwsapp.R;
import it.giacomos.android.wwwsapp.preferences.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

public class ReportActivity extends ActionBarActivity implements OnClickListener, OnItemSelectedListener
{
	private double mLatitude, mLongitude;
	private String mLocality;
	
	public ReportActivity()
	{
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.report_activity_layout);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		((Button)findViewById(R.id.bSend)).setOnClickListener(this);

		((Button)findViewById(R.id.bCancel)).setOnClickListener(this);

		String[] textItems = getResources().getStringArray(R.array.report_sky_textitems);
		IconTextSpinnerAdapter skySpinnerAdapter = 
				new IconTextSpinnerAdapter(this, 
						R.layout.report_icon_text_spinner_row, 
						textItems, this);
		skySpinnerAdapter.setType(IconTextSpinnerAdapter.SPINNER_SKY);
		Spinner spinner = (Spinner) findViewById(R.id.spinSky);
		spinner.setAdapter(skySpinnerAdapter);
		/* spinner.setOnItemSelectedListener(this) is called after initialization
		 * from observations.
		 */
		spinner.setSelection(1);
		
		textItems = getResources().getStringArray(R.array.report_wind_textitems);
		IconTextSpinnerAdapter windSpinnerAdapter = 
				new IconTextSpinnerAdapter(this, 
						R.layout.report_icon_text_spinner_row, 
						textItems, this);
		windSpinnerAdapter.setType(IconTextSpinnerAdapter.SPINNER_WIND);
		spinner = (Spinner) findViewById(R.id.spinWind);
		spinner.setAdapter(windSpinnerAdapter);
		spinner.setSelection(1);

		CheckBox cb = (CheckBox) findViewById(R.id.cbTemp);
		cb.setOnClickListener(this);
		cb = (CheckBox) findViewById(R.id.cbReportIncludeLocality);
		cb.setChecked(true);
		cb.setOnClickListener(this);
		findViewById(R.id.ettemp).setEnabled(false);

		/* populate Name field with last value */
		Settings s = new Settings(this);
		String userName = s.getReporterUserName();
		EditText et = (EditText) findViewById(R.id.etUserName);
		et.setText(userName);
		et.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence cs, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable ed) {
				setEnabled(ed.length() > 0);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {}

		});
		setEnabled(!userName.isEmpty());

		Intent i = getIntent();
		initByLocation(i.getStringExtra("temp"), i.getIntExtra("sky", 0), i.getIntExtra("wind", 0));
		mLatitude = i.getDoubleExtra("latitude", 0);
		mLongitude = i.getDoubleExtra("longitude", 0);
		mLocality = i.getStringExtra("locality");
		TextView localityTv = (TextView) findViewById(R.id.tvLocality);
		if(mLocality != null)
			localityTv.setText(mLocality);
		else 
			localityTv.setText(R.string.locality_unavailable);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onClick(View view)
	{
		EditText etTemp = null, teUserName, etComment;
		teUserName = (EditText) findViewById(R.id.etUserName);

		if(view.getId() == R.id.cbTemp)
		{
			CheckBox check = (CheckBox) view;
			etTemp = (EditText) findViewById(R.id.ettemp);
			etTemp.setEnabled(check.isChecked());
		}
		else if(view.getId() == R.id.bSend || view.getId() == R.id.bCancel)
		{
			Settings se = new Settings(this);
			if(se.getReporterUserName().compareTo(teUserName.getText().toString()) != 0)
				se.setReporterUserName(teUserName.getText().toString());
		}
		else if(view.getId() == R.id.cbReportIncludeLocality)
		{
			CheckBox cb = (CheckBox) view;
			findViewById(R.id.tvLocality).setEnabled(cb.isChecked());
		}

		if(view.getId() == R.id.bSend)
		{

			String user, temp, comment = "";

			int sky = -1 , wind = -1;


			CheckBox cb = (CheckBox) findViewById(R.id.cbTemp);
			if(cb.isChecked()) /* pick temperature only if cb is checked */
			{
				etTemp = (EditText) findViewById(R.id.ettemp);
				temp = etTemp.getText().toString();
			}
			else
				temp = "";

			

			user = teUserName.getText().toString();
			etComment = (EditText) findViewById(R.id.etComment);
			comment = etComment.getText().toString();
			Spinner sp = (Spinner) findViewById(R.id.spinSky);
			sky = sp.getSelectedItemPosition();
			sp = (Spinner) findViewById(R.id.spinWind);
			wind = sp.getSelectedItemPosition();
			
			CheckBox cbSkyIsRight = (CheckBox) findViewById(R.id.cbSkyRight);
			if(cbSkyIsRight.getVisibility() == View.VISIBLE && !cbSkyIsRight.isChecked())
			{
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this).
						setMessage(R.string.reportSkyCorrectMustBeChecked)
						.setPositiveButton(R.string.ok_button, null)
						.setTitle(R.string.reportSkyCorrectMustBeCheckedTitle);
				AlertDialog alertDialog = dialogBuilder.create();
				alertDialog.show();
			}
			else if(sky == 0)
			{
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this).setMessage(R.string.reportSkyMustBeValid)
						.setPositiveButton(R.string.ok_button, null).setTitle(R.string.reportSkyMustBeValidTitle);
				AlertDialog alertDialog = dialogBuilder.create();
				alertDialog.show();
			}
			else
			{
				Intent intent = new Intent();
				intent.putExtra("comment", comment);
				intent.putExtra("user", user);
				intent.putExtra("sky", sky);
				intent.putExtra("wind", wind);
				intent.putExtra("temperature", temp);
				intent.putExtra("latitude", mLatitude);
				intent.putExtra("longitude", mLongitude);
				
				CheckBox cbIncludeLocationName = (CheckBox) findViewById(R.id.cbReportIncludeLocality);
				if(cbIncludeLocationName.isChecked() && mLocality != null)
					intent.putExtra("locality", mLocality);
				else
					intent.putExtra("locality", "-");
				
				setResult(Activity.RESULT_OK, intent);
				finish();
			}

		}
		else if(view.getId() == R.id.bCancel)
		{
			setResult(Activity.RESULT_CANCELED, null);
			finish();
		}

	}

	private void setEnabled(boolean en)
	{
		findViewById(R.id.cbTemp).setEnabled(en);
		findViewById(R.id.spinWind).setEnabled(en);
		findViewById(R.id.spinSky).setEnabled(en);
		findViewById(R.id.bSend).setEnabled(en);
		if(!en)
			findViewById(R.id.etUserName).setBackgroundResource(R.drawable.background_with_border);
		else
			findViewById(R.id.etUserName).setBackgroundColor(Color.WHITE);
		if(!en)
			Toast.makeText(this, R.string.reportMustInsertUserName, Toast.LENGTH_LONG).show();

	}

	private void initByLocation(String temp, int sky, int wind) 
	{
		CheckBox cbSkyIsRight = (CheckBox) findViewById(R.id.cbSkyRight);
		Spinner spinSky = ((Spinner)findViewById(R.id.spinSky));
		spinSky.setSelection(sky, true);
		((Spinner)findViewById(R.id.spinWind)).setSelection(wind, true);
		((EditText)findViewById(R.id.ettemp)).setText(temp);
		/* no sky preset from observations: no need for the sky conditions correct check box */
		if(sky == 0)
			cbSkyIsRight.setVisibility(View.GONE);
		else /* sky conditions preset from observations: the user must confirm their correctness */
			cbSkyIsRight.setVisibility(View.VISIBLE);

		/* after the initByLocation initializes the spinner */
		spinSky.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) 
	{
		if(parent.getId() == R.id.spinSky)
		{
			CheckBox cbSkyIsRight = (CheckBox) findViewById(R.id.cbSkyRight);
			cbSkyIsRight.setChecked(true);
			cbSkyIsRight.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
}
