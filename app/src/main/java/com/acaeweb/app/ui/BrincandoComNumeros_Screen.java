package com.acaeweb.app.ui;

import android.app.*;
import android.content.res.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.acaeweb.app.*;
import com.acaeweb.app.Anim.*;
import java.io.*;
import java.util.*;

public class BrincandoComNumeros_Screen extends Gui {
	private Activity ctx;
	private LinearLayout layout;
	GameOver_Screen GameOver_Screen;

	Animation animation;

	private LinearLayout BrincandoNumerosLayout;
	private TextView timer_text;
	private TextView voltar_text;
	private TextView ajuda_text;

	//resultado
	private LinearLayout resultadoBox[] = new LinearLayout[4];
	private TextView resultadoBoxText[] = new TextView[4];


	public CountDownTimer cT;
	String timerMin = "0";
	int timerSeg = 00;
	int countDownTimer = 61000;

	private int num1 = 0, num2 = 0, result = 0;
	private int results[] = {-1, -1, -1, -1};

	int acertos = 0, erros = 0;
	int ajudas = 3;

	public BrincandoComNumeros_Screen(Activity context, LinearLayout layout) {
		this.ctx = context;
		this.layout = layout;

		animation = new Animation(ctx);
		initFonts(ctx);
		
		init();
		//faz a contagem regressiva do tempo - 59s
		createCountDown();

		data.current_screen = "brincando_numeros_screen";
		data.save(appFolderDir + "/current_screen.dat", data.current_screen.getBytes());

		GameOver_Screen = new GameOver_Screen(ctx, layout);
	}

	private void createCountDown() {
		cT = new CountDownTimer(countDownTimer, 1000) {

			@Override
			public void onTick(long p1) {
				timerMin = String.format("%02d", p1 / 60000);
				timerSeg = (int) ((p1 % 60000) / 1000);
				timer_text.setText(timerMin + ":" + String.format("%02d", timerSeg));
				countDownTimer -= 1000;
			}

			@Override
			public void onFinish() {
				timer_text.setText("00");
				GameOver_Screen.setScore("Acertos: " + acertos + "\nErros: " + erros);
				GameOver_Screen.show();
				GameOver_Screen.buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View p1) {
						sound.buttonClick(ctx);
						layout.removeAllViews();
						BrincandoComNumeros_Screen BrincandoComNumeros_Screen = new BrincandoComNumeros_Screen(ctx, layout);
						GameOver_Screen.hide();
					}
				});
			}


		};
		cT.start();
	}

	private void init() {
		randomNumbers();
		randomResults();
		screen();
	}

	private void screen() {
		try {
			BrincandoNumerosLayout = LinearLayout(ctx, 0, 0, getWidth(ctx), getHeight(ctx));
			BrincandoNumerosLayout.setOrientation(LinearLayout.VERTICAL);
			BrincandoNumerosLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			BrincandoNumerosLayout.setBackgroundColor(Color.WHITE);
			layout.addView(BrincandoNumerosLayout);

			//timer
			LinearLayout timer_layout = LinearLayout(ctx, 0, 0, getWidth(ctx) / 2, getHeight(ctx) / 11);
			timer_layout.setBackgroundDrawable(loadTexture(ctx, "gui/Timer.png"));
			timer_layout.setGravity(Gravity.CENTER | Gravity.CENTER);
			BrincandoNumerosLayout.addView(timer_layout);
			//timer text
			timer_text = label(ctx, 0, 0, timerMin + ":" + String.format("%02d", timerSeg), 24, "#FFFFFF", false, "#000000");
			timer_layout.addView(timer_text);

			//operation layout 
			LinearLayout operation_layout = LinearLayout(ctx, 0, getHeight(ctx) / 10, getWidth(ctx), getHeight(ctx) / 5);
			operation_layout.setGravity(Gravity.CENTER | Gravity.CENTER);
			BrincandoNumerosLayout.addView(operation_layout);

			TextView number1Text = label(ctx, 0, 0, Integer.toString(num1), getWidth(ctx) / 6, "#000000", false, "#000000");
			number1Text.setTypeface(fontAdorable);
			operation_layout.addView(number1Text);

			TextView operationText = label(ctx, getWidth(ctx) / 20, 0, "+", getWidth(ctx) / 6, "#000000", false, "#000000");
			operationText.setTypeface(fontAdorable);
			operation_layout.addView(operationText);

			TextView number2Text = label(ctx, getWidth(ctx) / 20, 0, Integer.toString(num2), getWidth(ctx) / 6, "#000000", false, "#000000");
			number2Text.setTypeface(fontAdorable);
			operation_layout.addView(number2Text);

			//resultados
			LinearLayout resultadoLayout = LinearLayout(ctx, 0, getHeight(ctx) / 5, getWidth(ctx) / 8 * 4 + getWidth(ctx) / 10, getHeight(ctx) / 7);
			resultadoLayout.setBackgroundColor(Color.BLACK);
			resultadoLayout.setGravity(Gravity.CENTER | Gravity.CENTER);
			BrincandoNumerosLayout.addView(resultadoLayout);

			for(int i = 0; i < 4; i++) {
				resultadoBox[i] = LinearLayout(ctx, 0, 0, getWidth(ctx) / 8, getHeight(ctx) / 14);
				resultadoBox[i].setBackgroundDrawable(loadTexture(ctx, "gui/Item.png"));
				resultadoBox[i].setGravity(Gravity.CENTER | Gravity.CENTER);
				resultadoBox[i].setId(i);
				resultadoBox[i].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View p1) {
						if(!resultadoBoxText[p1.getId()].getText().equals("")) {
							if(Integer.parseInt(resultadoBoxText[p1.getId()].getText().toString()) == result) {
								//Toast.makeText(ctx, "Resultado correto", Toast.LENGTH_SHORT).show();
								acertos++;
								layout.removeView(BrincandoNumerosLayout);
								init();

								if(acertos % 7 == 0) { //for each 5 acertos add most 4seg
									countDownTimer += 10000;
									cT.cancel();
									createCountDown();
									timer_text.setTextColor(Color.parseColor("#60C400"));
									timer_text.postDelayed(new Runnable() {
										@Override
										public void run() {
											timer_text.setTextColor(Color.WHITE);
										}
									}, 2000);


									showToast(ctx, "+10 seg", "#60C400");
									sound.levelUp(ctx);
								}
								
								else if(acertos % 12 == 0) {
									ajudas++;
									ajuda_text.setText("Ajuda(" + ajudas + ")");
									
									showToast(ctx, "+1 ajuda", "#60C400");
									sound.levelUp(ctx);
								}
							} else {
								erros++;
								countDownTimer -= 4000;
								cT.cancel();
								createCountDown();
								timer_text.setTextColor(Color.parseColor("#C4242B"));
								timer_text.postDelayed(new Runnable() {
									@Override
									public void run() {
										timer_text.setTextColor(Color.WHITE);
									}
								}, 2000);

								showToast(ctx, "-4 seg", "#C4242B");
							}
						}
					}
				});
				resultadoLayout.addView(resultadoBox[i]);

				resultadoBoxText[i] = label(ctx, 0, 0, Integer.toString(results[i]), 18, "#000000", false, "#000000");
				resultadoBoxText[i].setTypeface(fontAdorable);
				resultadoBox[i].addView(resultadoBoxText[i]);
			}

			//area menu
			LinearLayout area_menu_layout = LinearLayout(ctx, 0, getHeight(ctx) / 5 - getHeight(ctx) / 50, getWidth(ctx), getHeight(ctx) / 9);
			area_menu_layout.setBackgroundDrawable(loadTexture(ctx, "gui/Area_Menu.png"));
			area_menu_layout.setGravity(Gravity.CENTER_VERTICAL);
			BrincandoNumerosLayout.addView(area_menu_layout);

			ajuda_text = label(ctx, getWidth(ctx) / 10, 0, "Ajuda(" + ajudas + ")", getWidth(ctx) / 16, "#FF2416", false, "#FF0000");
			ajuda_text.setTypeface(fontBromo);
			ajuda_text.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					if(ajudas > 0) {
						ajudas--;
						ajuda_text.setText("Ajuda(" + ajudas + ")");
						
						int random = (int) Math.floor(Math.random() * resultadoBoxText.length);
						while(resultadoBoxText[random].getText().equals(result + "") || resultadoBoxText[random].getText().equals("")) {
							random = (int) Math.floor(Math.random() * resultadoBoxText.length);
						}
						
						resultadoBoxText[random].setText("");
					}
				}
			});
			area_menu_layout.addView(ajuda_text);

			voltar_text = label(ctx, getWidth(ctx) / 2 - getWidth(ctx) / 7, 0, "Voltar", getWidth(ctx) / 16, "#00B3FF", false, "#FF0000");
			voltar_text.setTypeface(fontBromo);
			voltar_text.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					cT.cancel();
					layout.removeAllViews();
					Home_Screen Home_Screen = new Home_Screen(ctx, layout);
				}
			});
			area_menu_layout.addView(voltar_text);

		} catch(IOException e) {}
	}

	//aleatoriza os numeros
	private void randomNumbers() {
		num1 = (int) Math.floor(Math.random() * 9);
		num2 = (int) Math.floor(Math.random() * 9);
		while(num2 == num1) {
			num2 = (int) Math.floor(Math.random() * 9);
		}

		result = num1 + num2;
	}

	//aleatoriza os possiveis resultados
	private void randomResults() {
		results[0] = result;
		randomResultIndexValue(1);
		randomResultIndexValue(2);
		randomResultIndexValue(3);

		ArrayList<Integer> values = new ArrayList<>();
		for(int i = 0; i < results.length; i++) {
			values.add(results[i]);
		}

		Collections.shuffle(values);

		for(int i = 0; i < results.length; i++) {
			results[i] = values.get(i);
		}
	}

	//aleatoriza um resultado
	private void randomResultIndexValue(int index) {
		while(isOnArray(index) == true || results[index] == -1) {
			results[index] = (int) Math.floor(Math.random() * 20);
		}
	}

	//retorna se tal valor ja conte! na array results
	private boolean isOnArray(int index) {
		for(int i = 0; i < results.length; i++) {
			if(results[i] == results[index] && i != index) {
				return true;
			}
		}
		return false;
	}
}
