package com.acaeweb.app;

import android.app.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.view.*;
import android.widget.*;
import android.widget.TableRow.*;
import java.io.*;

public class Gui extends Utils
{
	public Data data = new Data();
	public Sounds sound = new Sounds();
	
	public Typeface fontAdorable, fontBromo;
	
	//button
	public Button button(Activity ctx, int x, int y, int width, int height, String txt, int txtSize, boolean shadow, String shadowColor) {
		Button btn = new Button(ctx);
		LayoutParams btnParams = new TableRow.LayoutParams(width, height);
		btn.setText(txt);
		btn.setTextSize(txtSize);
		btn.setLayoutParams(btnParams);
		btnParams.setMargins(x, y, 0, 0);
		if(shadow==true){
			btn.setShadowLayer(0.0001f, Math.round(btn.getLineHeight() / 11.5), Math.round(btn.getLineHeight() / 11.5), Color.parseColor(shadowColor));
		}
		return btn;
	}

	//text
	public TextView label(Activity ctx, int x, int y, String text, int textSize, String textColor, boolean shadow, String shadowColor) {
		TextView txt = new TextView(ctx);
		LayoutParams txtParams = new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT);
		txt.setText(text);
		txt.setTextSize(textSize);
		txt.setTextColor(Color.parseColor(textColor));
		txt.setLayoutParams(txtParams);
		txtParams.setMargins(x, y, 0, 0);
		if(shadow == true){
			txt.setShadowLayer(1, Math.round(txt.getLineHeight() / 8), Math.round(txt.getLineHeight() / 8), android.graphics.Color.parseColor(shadowColor));
		}
		return txt;
	}

	//layout's
	public LinearLayout LinearLayout(Activity ctx, int x, int y, int width, int height)
	{
		LinearLayout layout = new LinearLayout(ctx);
		LayoutParams layoutParams = new TableRow.LayoutParams(width, height);
		layout.setLayoutParams(layoutParams);
		layoutParams.setMargins(x,y,0,0);
		return layout;
	}

	public AbsoluteLayout AbsoluteLayout(Activity ctx, int x, int y, int width, int height)
	{
		AbsoluteLayout layout = new AbsoluteLayout(ctx);
		LayoutParams layoutParams = new TableRow.LayoutParams(width, height);
		layout.setLayoutParams(layoutParams);
		layoutParams.setMargins(x,y,0,0);
		return layout;
	}


	//other methods to use in the gui
	public BitmapDrawable loadTexture(Activity ctx, String storage) throws IOException
	{
		return new BitmapDrawable(BitmapFactory.decodeStream(ctx.getAssets().open(storage)));
	}

	public int getWidth(Activity ctx)
	{
		return ctx.getWindowManager().getDefaultDisplay().getWidth();
	}

	public int getHeight(Activity ctx)
	{
		return ctx.getWindowManager().getDefaultDisplay().getHeight();
	}
	
	
	public void initFonts(Activity ctx) {
		fontAdorable = Typeface.createFromAsset(ctx.getAssets(), "fonts/Adorable.otf");
		fontBromo = Typeface.createFromAsset(ctx.getAssets(), "fonts/BROMO.otf");
	}
	
	
	public void showToast(Activity ctx, String text, String color) {
		TextView toastText = new TextView(ctx);
		toastText.setText(text);
		toastText.setTextColor(Color.parseColor(color));
		toastText.setTypeface(fontAdorable);
		toastText.setTextSize(getWidth(ctx) / 16);

		Toast message = new Toast(ctx);
		message.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		message.setDuration(4000);
		message.setView(toastText);
		message.show();
	}
	
	public class Sounds {
		public void levelUp(Activity ctx) {
			try {
				AssetFileDescriptor afd = ctx.getAssets().openFd("sounds/levelup.mp3");
				MediaPlayer player = new MediaPlayer();
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				player.prepare();
				player.start();
			} catch(IOException e) {}
		}
		public void buttonClick(Activity ctx) {
			try {
				AssetFileDescriptor afd = ctx.getAssets().openFd("sounds/button_click.mp3");
				MediaPlayer player = new MediaPlayer();
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				player.prepare();
				player.start();
			} catch(IOException e) {}
		}
	}
	
}
