package com.acaeweb.app.Anim;

import android.app.*;
import android.view.animation.*;
import com.acaeweb.app.*;
import android.graphics.*;

public class Animation
{
	public android.view.animation.Animation 
		translate_from_left,
		translate_from_right,
		fade_in,
		fade_out
	;
	
	private Gui gui = new Gui();
	public Animation(Activity ctx) {
		//selecao de exercicios
		translate_from_left = new TranslateAnimation(-gui.getWidth(ctx), 0, 0, 0);
		translate_from_left.setDuration(1000);
		
		translate_from_right = new TranslateAnimation(gui.getWidth(ctx), 0, 0, 0);
		translate_from_right.setDuration(1000);
		
		//fade animation
		fade_in = new AlphaAnimation(1, 0);
		fade_in.setBackgroundColor(Color.BLACK);
		fade_in.setDuration(200);
		
		fade_out = new AlphaAnimation(0, 1);
		fade_out.setBackgroundColor(Color.BLACK);
		fade_out.setDuration(200);
		
	}
}
