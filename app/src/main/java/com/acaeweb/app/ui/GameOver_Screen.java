package com.acaeweb.app.ui;

import android.app.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import com.acaeweb.app.*;
import java.io.*;

public class GameOver_Screen extends Gui {
	private Activity ctx;
	private LinearLayout layout;

	private LinearLayout gameOverLayout;
	private PopupWindow gameOverWindow;
	private boolean showed = false;

	public LinearLayout buttonPlayAgain;

	public Typeface fontBromo, fontAdorable;

	private TextView scoreText;

	public GameOver_Screen(Activity context, LinearLayout layout) {
		this.ctx = context;
		this.layout = layout;

		fontBromo = Typeface.createFromAsset(ctx.getAssets(), "fonts/BROMO.otf");
		fontAdorable = Typeface.createFromAsset(ctx.getAssets(), "fonts/Adorable.otf");

		screen();
	}

	public void screen() {
		try {
			gameOverLayout = LinearLayout(ctx, 0, 0, getWidth(ctx), getHeight(ctx));
			gameOverLayout.setBackgroundColor(Color.argb(3000, 0, 0, 0));
			gameOverLayout.setGravity(Gravity.CENTER | Gravity.CENTER);

			gameOverWindow = new PopupWindow(gameOverLayout, getWidth(ctx), getHeight(ctx));

			LinearLayout gameOverBack = LinearLayout(ctx, 0, 0, getWidth(ctx) - getWidth(ctx) / 20, getHeight(ctx) / 3 + getHeight(ctx) / 30);
			gameOverBack.setBackgroundDrawable(loadTexture(ctx, "gui/background_gameover.png"));
			gameOverBack.setGravity(Gravity.CENTER_HORIZONTAL);
			gameOverBack.setOrientation(LinearLayout.VERTICAL);
			gameOverLayout.addView(gameOverBack);

			TextView gameOverText = label(ctx, 0, getHeight(ctx) / 40, "Fim de Jogo", getWidth(ctx) / 16, "#000000", false, "#000000");
			gameOverText.setTypeface(fontAdorable);
			gameOverBack.addView(gameOverText);

			scoreText = label(ctx, 0, getHeight(ctx) / 40, "Score", getWidth(ctx) / 25, "#000000", false, "#000000");
			scoreText.setTypeface(fontAdorable);
			gameOverBack.addView(scoreText);

			LinearLayout buttonsLayout = LinearLayout(ctx, 0, getHeight(ctx) / 30, getWidth(ctx) - getWidth(ctx) / 20, getHeight(ctx) / 9);
			buttonsLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			gameOverBack.addView(buttonsLayout);

			LinearLayout buttonMenu = LinearLayout(ctx, -getWidth(ctx) / 40, 0, getWidth(ctx) / 5 - getWidth(ctx) / 40, getHeight(ctx) / 9);
			buttonMenu.setBackgroundDrawable(loadTexture(ctx, "gui/button_menu.png"));
			buttonMenu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					layout.removeAllViews();
					Home_Screen Home_Screen = new Home_Screen(ctx, layout);
					hide();
				}
			});
			buttonsLayout.addView(buttonMenu);

			buttonPlayAgain = LinearLayout(ctx, (getWidth(ctx) / 40) * 2, 0, getWidth(ctx) / 5 - getWidth(ctx) / 40, getHeight(ctx) / 9);
			buttonPlayAgain.setBackgroundDrawable(loadTexture(ctx, "gui/button_play_again.png"));
			buttonsLayout.addView(buttonPlayAgain);
		} catch(IOException e) {}
	}

	public void show() {
		if(showed == false) {
			ctx.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameOverWindow.showAtLocation(ctx.getWindow().getDecorView(), Gravity.LEFT, 0, 0);
					showed = true;
				}
			});
		}
	}

	public void hide() {
		if(showed == true) {
			ctx.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					gameOverWindow.dismiss();
					showed = false;
				}
			});
		}
	}

	public void setScore(final String text) {
		ctx.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				scoreText.setText(text);
			}
		});
	}
}
