package com.acaeweb.app.ui;

import android.app.*;
import android.widget.*;
import com.acaeweb.app.*;
import android.view.*;
import android.graphics.*;
import java.io.*;
import android.view.View.*;
import android.content.*;
import android.os.*;
import com.acaeweb.app.Anim.*;

public class Home_Screen extends Gui 
{
	
	private final Activity ctx;
	private LinearLayout layout;
	
	Animation animation;
	
	Button btnBrincar, btnConfig;

	public LinearLayout home_layout;
	//private Gui Gui = new Gui();
	
	public SelecaoExercicio_Screen SelecaoExercicio_Screen;

	public Home_Screen(Activity contexto, LinearLayout layout) {
		this.ctx = contexto;
		this.layout = layout;
		
		animation = new Animation(ctx);
		
		screen();
		
		//set the current screen dat
		data.current_screen = "home_screen";
		data.save(appFolderDir + "/current_screen.dat", data.current_screen.getBytes());
	}
	

	public void screen() {
		try {
			//background
			home_layout = LinearLayout(ctx, 0, 0, getWidth(ctx), getHeight(ctx));
			home_layout.setOrientation(LinearLayout.VERTICAL);
			home_layout.setGravity(Gravity.CENTER_HORIZONTAL);
			home_layout.setBackgroundDrawable(loadTexture(ctx, "gui/fundo.jpg"));
			layout.addView(home_layout);

			//logo
			LinearLayout logo = LinearLayout(ctx, 0, getHeight(ctx) / 30, getWidth(ctx) / 1 - getWidth(ctx) / 5, getHeight(ctx) / 9);
			logo.setBackgroundDrawable(loadTexture(ctx, "gui/logo.png"));
			home_layout.addView(logo);
			
			//botão brincar
			btnBrincar = button(ctx, 0, getHeight(ctx) / 3, getWidth(ctx) / 4, getHeight(ctx) / 4 - getHeight(ctx) / 10, "", 16, false, "#000000");
			btnBrincar.setBackgroundDrawable(loadTexture(ctx, "gui/Icone_Iniciar.png"));
			//btnBrincar.setOnTouchListener(onPressedOpacity());
			btnBrincar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					layout.removeAllViews();
					SelecaoExercicio_Screen = new SelecaoExercicio_Screen(ctx, layout);
				}
			});
			home_layout.addView(btnBrincar);
			
			//texto brincar
			LinearLayout txtBrincar = LinearLayout(ctx, 0, getHeight(ctx) / 50, getWidth(ctx) / 3, getHeight(ctx) / 14);
			txtBrincar.setBackgroundDrawable(loadTexture(ctx, "gui/brincar.png"));
			home_layout.addView(txtBrincar);
			
			//botão configurações
			btnConfig = button(ctx, getWidth(ctx) / 2 - getWidth(ctx) / 8, getHeight(ctx) / 6, getWidth(ctx) / 8, getHeight(ctx) / 14, "", 16, false, "#000000");
			btnConfig.setBackgroundDrawable(loadTexture(ctx, "gui/configuracoes.png"));
			btnConfig.setOnTouchListener(onPressedOpacity());
			//btnConfig.setOnClickListener(this);
			//home_layout.addView(btnConfig);
		} catch(IOException e) { }
	}
	
	private View.OnTouchListener onPressedOpacity() {
		return new View.OnTouchListener() {
			@Override
			public boolean onTouch(View p1, MotionEvent p2) {
				switch(p2.getActionMasked()) {
					case 0:
						p1.getBackground().setAlpha(128);
						break;
					case 1:
						p1.getBackground().setAlpha(50);
						break;
				}
				return true;
			}
		};
	}
} 
