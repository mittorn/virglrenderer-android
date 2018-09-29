package settings;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.RelativeLayout.*;
import android.content.*;
import android.text.*;
import android.text.style.*;
import android.graphics.*;
import java.awt.font.*;
import android.widget.GridLayout.*;
import java.io.*;
import android.util.*;
import java.nio.*;

public class activity extends Activity
{
	public static final int sdk = Integer.valueOf(Build.VERSION.SDK);
	public static final String T = "virgl-activity";
	;public static final int FL_RING = (1<<0);
	//#define FL_GLX (1<<1)                                                                                                                                           
	public static final int FL_GLES = (1<<2);
	//#define FL_OVERLAY (1<<3)
	public static final int FL_MULTITHREAD = (1<<4);

	public SpannableString style_button_string(String str)
	{
		if(sdk < 21)
			str = str.toUpperCase();

		SpannableString span_string = new SpannableString(str.toUpperCase());

		if(sdk < 21)
			span_string.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), 0);

		span_string.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, str.length(), 0);

		return span_string;
	}

	private EditText add_edit(LinearLayout layout, String title)
	{
		LinearLayout.LayoutParams edit_params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		edit_params.setMargins(10,20,10,20);//размеры строки для ввода аргументов
		
		TextView title_view = new TextView(this);
        title_view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        title_view.setText(title);
        title_view.setTextAppearance(this, android.R.attr.textAppearanceLarge);

		EditText edit = new EditText(this);
        edit.setLayoutParams(edit_params);
		edit.setSingleLine(true);
		if(sdk < 21)
		{
			edit.setBackgroundColor(0xFF353535);
			edit.setTextColor(0xFFFFFFFF);
			edit.setPadding(5,5,5,5);
		}

		layout.addView(title_view);
		layout.addView(edit);
		return edit;
	}
	private Button add_button(LinearLayout panel,String title, View.OnClickListener listener)
	{
		Button startButton = new Button(this);

		// Set launch button title here
		startButton.setText(style_button_string(title));
		//startButton.setTextAlignment()
		LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		//buttonParams.addRule(Layout.ALIGN_PARENT_BOTTOM);
		buttonParams.weight = 1;
		startButton.setGravity(Gravity.BOTTOM);
		startButton.setLayoutParams(buttonParams);
		if(sdk < 21)
		{
			startButton.getBackground().setAlpha(96);
			startButton.getBackground().invalidateSelf();
			startButton.setTextColor(0xFFFFFFFF);
			startButton.setTextAppearance(this, android.R.attr.textAppearanceLarge);
			startButton.setTextSize(20);
		}
		startButton.setOnClickListener(listener);

		// Add other options here

		panel.addView(startButton);
		return startButton;
	}
	private CheckBox add_checkbox(LinearLayout layout, String title)
	{
		CheckBox cb = new CheckBox(this);
		cb.setText(title);
		layout.addView(cb);
		return cb;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// set theme
		if ( sdk >= 21 )
			super.setTheme( 0x01030224 );
		else 
			super.setTheme( 0x01030005 );

        // Build layout
        RelativeLayout launcher = new RelativeLayout(this);
     //   launcher.setOrientation(LinearLayout.VERTICAL);
       // launcher.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		launcher.setBackgroundColor(0xFF252525);
		TextView launcherTitle = new TextView(this);
        LayoutParams titleparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		titleparams.setMargins(5,15,5,1);//размеры верхнего layout
		//titleparams.weight = 1;
		launcherTitle.setLayoutParams(titleparams);
        launcherTitle.setText("virgl renderer");
        launcherTitle.setTextColor(0xFF4db017);
        launcherTitle.setTextAppearance(this, android.R.attr.textAppearanceMedium);
		launcherTitle.setTextSize(25);
		launcherTitle.setBackgroundColor(0xFF555555);
		launcherTitle.setCompoundDrawablePadding(10);
		try
		{
			launcherTitle.setCompoundDrawablesWithIntrinsicBounds(getApplicationContext().getPackageManager().getApplicationIcon(getPackageName()),null,null,null);
			launcherTitle.setPadding(9,9,6,0);
		}
		catch(Exception e)
		{
			launcherTitle.setPadding(60,6,6,6);
		}
		launcher.addView(launcherTitle);
		LinearLayout launcherBody = new LinearLayout(this);
        launcherBody.setOrientation(LinearLayout.VERTICAL);
		ScrollView.LayoutParams bp = new ScrollView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		bp.setMargins(10,15,10,10);//размеры верхнего layout

        launcherBody.setLayoutParams(bp);
		launcherBody.setPadding(10,0,10,30);
		launcherBody.setBackgroundColor(0xFF454545);
		LinearLayout launcherBorder = new LinearLayout(this);
		RelativeLayout.LayoutParams params_ = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);

		LinearLayout panel = new LinearLayout(this);
		panel.setId(1000);
		launcherTitle.setId(1001);
		params_.addRule(RelativeLayout.BELOW, launcherTitle.getId());
		params_.setMargins(5,15,5,1);//размеры верхнего layout
		
		params_.addRule(RelativeLayout.ABOVE, panel.getId());
		launcherBorder.setLayoutParams(params_);
		launcherBorder.setBackgroundColor(0xFF555555);
		launcherBorder.setOrientation(LinearLayout.VERTICAL);

		ScrollView launcherBorder2 = new ScrollView(this);
		LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		sp.setMargins(5,15,5,10);//размеры верхнего layout
		
		launcherBorder2.setLayoutParams(sp);
		//launcherBorder2.setOrientation(LinearLayout.VERTICAL);
		launcherBorder2.setBackgroundColor(0xFF353535);
		launcherBorder2.addView(launcherBody);
		launcherBorder2.setPadding(10,0,10,10);
		launcherBorder.addView(launcherBorder2);
		launcherBorder.setPadding(10,0,10,20);
		launcher.addView(launcherBorder);

		final EditText socket_path = add_edit(launcherBody, "Socket path (/tmp/.virgl_test)");
		final EditText ring_path = add_edit(launcherBody, "Ring buffer path (/dev/shm)");
		final CheckBox use_ring = add_checkbox(launcherBody, "Use ring buffer instead of socket");
		final CheckBox use_gles = add_checkbox(launcherBody, "Use GL ES 3.x instead of OpenGL");
		final CheckBox use_threads = add_checkbox(launcherBody, "Use multi-thread egl access");
		int flags = 0;
		try
		{
			//char[] buffer = new char[64];
			CharBuffer buffer = CharBuffer.allocate(128);
			FileReader settings_reader = new FileReader(getFilesDir().getPath()+"/settings");
			//int len = settings_reader.read(buffer);
			//buffer.notifyAll()
			//buffer.
			BufferedReader reader = new BufferedReader(settings_reader);
			//reader.readLine()
			//CharBuffer buf = CharBuffer.allocate(settings_reader.read(buffer));
			//buffer.read(buf);
			//StringReader reader = new StringReader(settings_reader);
			String[] parts = reader.readLine().split(" ");
			//Log.d(T,"read:"+Integer.valueOf(parts[0]) + ", " + parts[1] + ", " + parts[2]);
			flags = Integer.valueOf(parts[0]);
			socket_path.setText(parts[1]);
			ring_path.setText(parts[2]);
			reader.close();
			settings_reader.close();
		}
		catch(Exception e){}
		use_ring.setChecked((flags & FL_RING) != 0);
		use_gles.setChecked((flags & FL_GLES) != 0);
		use_threads.setChecked((flags & FL_MULTITHREAD) != 0);
		panel.setOrientation(LinearLayout.HORIZONTAL);
		add_button(panel,"Clean services", new View.OnClickListener(){@Override public void onClick(View v){
			for(int i = 1; i < 32; i++)
			{
				try{
				stopService( new Intent().setClassName(activity.this, "process.p"+i));
				}
				catch(Exception e){}
			}
			}});
		add_button(panel,"Start service", new View.OnClickListener(){@Override public void onClick(View v){
			try{
				int flags = 0;
				if(use_ring.isChecked())
					flags |= FL_RING;
				if(use_gles.isChecked())
					flags |= FL_GLES;
				if(use_threads.isChecked())
					flags |= FL_MULTITHREAD;
				
				FileWriter writer = new FileWriter(getFilesDir().getPath()+"/settings");
				writer.write(String.valueOf(flags));
				writer.write(' ');
				writer.write(socket_path.getText().toString());
				writer.write(' ');
				writer.write(ring_path.getText().toString());
				writer.close();
				startService( new Intent().setClassName(activity.this, "process.p1"));
			}
			catch(Exception e)
			{}
			}});
		//panel.setWeightSum(2);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//params.weight = 1;
		//params.alignWithParent = true;
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	
		panel.setLayoutParams(params);//
		//panel.setGravity(Gravity.BOTTOM);
		//RelativeLayout.ALIGN_PARENT_BOTTOM;

		launcher.addView(panel);
		//launcher.setWeightSum(5000000);
        setContentView(launcher);
		//mPref = getSharedPreferences("mod", 0);
		//socket_path.setText(mPref.getString("argv","-dev 3 -log"));

		// Uncomment this if you have pak file
		// ExtractAssets.extractPAK(this, false);
	}

	
}
