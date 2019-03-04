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

public class JogoDasSilabas_Screen extends Gui {
	private Activity ctx;
	private LinearLayout layout;
	GameOver_Screen GameOver_Screen;
	Animation animation;

	private LinearLayout JogoSilabasLayout;
	private TextView timer_text;
	private TextView ajuda_text;

	//silabas layout
	private LinearLayout silabasBox[] = new LinearLayout[2];
	private TextView silabasBoxText[] = new TextView[2];

	//word layout
	private LinearLayout wordBox[] = new LinearLayout[2];
	private TextView wordBoxText[] = new TextView[2];

	public CountDownTimer cT;
	private int countDownTimer = 61000;
	String timerMin = "0";
	int timerSeg = 00;

	private int currentAnimalIndex = 0;
	private int lastAnimalIndex = 0;

	private String animais[][] = {
		//nome do animal, textura
		{"ARARA", "animais/Arara.png"},
		{"BALEIA", "animais/Baleia.png"},
		{"CACHORRO", "animais/Cachorro.png"},
		{"COBRA", "animais/Cobra.png"},
		{"GALINHA", "animais/Galinha.png"},
		{"GATO", "animais/Gato.png"},
		{"PEIXE", "animais/Peixe.png"},
		{"POLVO", "animais/Polvo.png"},
		{"URSO", "animais/Urso.png"},
		{"VACA", "animais/Vaca.png"}
	};

	private String animaisSilabas[][] = {
		{"A", "RA", "RA"},
		{"BA", "LEI", "A"},
		{"CA", "CHOR", "RO"},
		{"CO", "BRA"},
		{"GA", "LI", "NHA"},
		{"GA", "TO"},
		{"PEI", "XE"},
		{"POL", "VO"},
		{"UR", "SO"},
		{"VA", "CA"}
	};

	private String currentSilabas[];


	int acertos = 0, erros = 0;
	int ajudas = 3;

	public JogoDasSilabas_Screen(Activity contexto, LinearLayout layout) {
		this.ctx = contexto;
		this.layout = layout;
		animation = new Animation(ctx);

		initFonts(ctx);
		init();

		//faz a contagem regressiva do tempo - 59s
		createCountDown();

		data.current_screen = "jogo_das_silabas";
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
				GameOver_Screen.show();
				GameOver_Screen.setScore("Acertos: " + acertos);
				GameOver_Screen.buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View p1) {
						sound.buttonClick(ctx);
						layout.removeAllViews();
						JogoDasSilabas_Screen BrincandoComNumeros_Screen = new JogoDasSilabas_Screen(ctx, layout);
						GameOver_Screen.hide();
					}
				});
			}

		};
		cT.start();
	}

	public void init() {
		sortearAnimal();

		wordBox = new LinearLayout[animaisSilabas[currentAnimalIndex].length];
		wordBoxText = new TextView[animaisSilabas[currentAnimalIndex].length];
		silabasBox = new LinearLayout[animaisSilabas[currentAnimalIndex].length];
		silabasBoxText = new TextView[animaisSilabas[currentAnimalIndex].length];
		currentSilabas = new String[animaisSilabas[currentAnimalIndex].length];

		randomizarSilabas();

		screen();
	}

	public void screen() {
		try {
			JogoSilabasLayout = LinearLayout(ctx, 0, 0, getWidth(ctx), getHeight(ctx));
			JogoSilabasLayout.setOrientation(LinearLayout.VERTICAL);
			JogoSilabasLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			JogoSilabasLayout.setBackgroundColor(Color.WHITE);
			layout.addView(JogoSilabasLayout);

			//timer
			LinearLayout timer_layout = LinearLayout(ctx, 0, 0, getWidth(ctx) / 2, getHeight(ctx) / 11);
			timer_layout.setBackgroundDrawable(loadTexture(ctx, "gui/Timer.png"));
			timer_layout.setGravity(Gravity.CENTER | Gravity.CENTER);
			JogoSilabasLayout.addView(timer_layout);
			//timer text
			timer_text = label(ctx, 0, 0, timerMin + ":" + String.format("%02d", timerSeg), 24, "#FFFFFF", false, "#000000");
			timer_layout.addView(timer_text);

			//animal
			LinearLayout animalImage = LinearLayout(ctx, 0, getHeight(ctx) / 10, getWidth(ctx) / 3, getHeight(ctx) / 6);
			animalImage.setBackgroundDrawable(loadTexture(ctx, animais[currentAnimalIndex][1]));
			JogoSilabasLayout.addView(animalImage);

			//word
			LinearLayout wordLayout = LinearLayout(ctx, 0, getHeight(ctx) / 20, getWidth(ctx) / 7 * animaisSilabas[currentAnimalIndex].length + getWidth(ctx) / 10, getHeight(ctx) / 7);
			wordLayout.setGravity(Gravity.CENTER | Gravity.CENTER);
			JogoSilabasLayout.addView(wordLayout);


			for(int i = 0; i < animaisSilabas[currentAnimalIndex].length; i++) {
				wordBox[i] = LinearLayout(ctx, getWidth(ctx) / 50, 0, getWidth(ctx) / 7, getHeight(ctx) / 13);
				wordBox[i].setBackgroundDrawable(loadTexture(ctx, "gui/Espaco_Silaba.png"));
				wordBox[i].setGravity(Gravity.CENTER | Gravity.CENTER);
				wordBox[i].setId(i);
				wordBox[i].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View p1) {
						if(!wordBoxText[p1.getId()].equals("")) {
							for(int a = 0; a < silabasBox.length; a++) {
								if(silabasBoxText[a].getText().equals("")) {
									silabasBoxText[a].setText(wordBoxText[p1.getId()].getText());
									wordBoxText[p1.getId()].setText("");
									break;
								}
							}
						}
					}
				});
				wordLayout.addView(wordBox[i]);

				wordBoxText[i] = label(ctx, 0, 0, "", getWidth(ctx) / 16, "#000000", false, "#000000");
				wordBoxText[i].setTypeface(fontBromo);
				wordBox[i].addView(wordBoxText[i]);
			}

			//silabas
			final LinearLayout silabasLayout = LinearLayout(ctx, 0, getHeight(ctx) / 15, getWidth(ctx) / 7 * animaisSilabas[currentAnimalIndex].length + getWidth(ctx) / 10, getHeight(ctx) / 7);
			silabasLayout.setBackgroundColor(Color.BLACK);
			silabasLayout.setGravity(Gravity.CENTER | Gravity.CENTER);
			JogoSilabasLayout.addView(silabasLayout);

			for(int i = 0; i < animaisSilabas[currentAnimalIndex].length; i++) {
				silabasBox[i] = LinearLayout(ctx, getWidth(ctx) / 50, 0, getWidth(ctx) / 7, getHeight(ctx) / 13);
				silabasBox[i].setBackgroundDrawable(loadTexture(ctx, "gui/Silaba.png"));
				silabasBox[i].setGravity(Gravity.CENTER | Gravity.CENTER);
				silabasBox[i].setId(i);
				silabasBox[i].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View p1) {
						for(int a = 0; a < wordBox.length; a++) {
							if(wordBoxText[a].getText().equals("")) {
								wordBoxText[a].setText(silabasBoxText[p1.getId()].getText());
								silabasBoxText[p1.getId()].setText("");
								ifPalavraCerta();
								break;
							}
						}
					}
				});
				silabasLayout.addView(silabasBox[i]);

				silabasBoxText[i] = label(ctx, 0, 0, currentSilabas[i], getWidth(ctx) / 16, "#000000", false, "#000000");
				silabasBoxText[i].setTypeface(fontBromo);
				silabasBox[i].addView(silabasBoxText[i]);
			}

			//area menu
			LinearLayout area_menu_layout = LinearLayout(ctx, 0, getHeight(ctx) / 7, getWidth(ctx), getHeight(ctx) / 9);
			area_menu_layout.setBackgroundDrawable(loadTexture(ctx, "gui/Area_Menu.png"));
			area_menu_layout.setGravity(Gravity.CENTER_VERTICAL);
			JogoSilabasLayout.addView(area_menu_layout);

			ajuda_text = label(ctx, getWidth(ctx) / 10, 0, "Ajuda(" + ajudas + ")", getWidth(ctx) / 16, "#FF2416", false, "#FF0000");
			ajuda_text.setTypeface(fontBromo);
			ajuda_text.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					if(ajudas > 0) {
						ajudas--;
						ajuda_text.setText("Ajuda(" + ajudas + ")");
						
						int random = (int) Math.floor(Math.random() * animaisSilabas[currentAnimalIndex].length);
						while(!wordBoxText[random].getText().equals("")) {
							random = (int) Math.floor(Math.random() * animaisSilabas[currentAnimalIndex].length);
						}
						
						wordBoxText[random].setText(animaisSilabas[currentAnimalIndex][random]);
						for(int i = 0; i < animaisSilabas[currentAnimalIndex].length; i++) {
							if(silabasBoxText[i].getText().equals(animaisSilabas[currentAnimalIndex][random])) {
								silabasBoxText[i].setText("");
								break;
							}
						}
						
						ifPalavraCerta();
					}
				}
			});
			area_menu_layout.addView(ajuda_text);

			TextView voltar_text = label(ctx, getWidth(ctx) / 2 - getWidth(ctx) / 7, 0, "Voltar", getWidth(ctx) / 16, "#00B3FF", false, "#FF0000");
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

	//sorteia um animal
	private void sortearAnimal() {
		currentAnimalIndex = (int) Math.floor((Math.random() * animais.length));
		while(currentAnimalIndex == lastAnimalIndex) {
			currentAnimalIndex = (int) Math.floor((Math.random() * animais.length));
		}

		lastAnimalIndex = currentAnimalIndex;
	}

	//verifica se a palavra estar certa
	private boolean isParavraCerta() {
		String word = "";
		for(int i = 0; i < wordBoxText.length; i++) {
			if(!wordBoxText[i].getText().equals("")) {
				word += wordBoxText[i].getText();
			}
		}
		if(word.equals(animais[currentAnimalIndex][0])) {
			return true;
		}
		return false;
	}
	
	private void ifPalavraCerta() {
		if(isParavraCerta() == true) {
			//Toast.makeText(ctx, "Palavra estar certa", Toast.LENGTH_SHORT).show();
			acertos++;
			layout.removeView(JogoSilabasLayout);
			init();

			//for each 10 acertos add most 10seg
			if(acertos % 10 == 0) {
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
			
			if(acertos % 13 == 0) {
				ajudas++;
				ajuda_text.setText("Ajuda(" + ajudas + ")");
				
				showToast(ctx, "+1 ajuda", "#60C400");
				sound.levelUp(ctx);
			}
		}
	}

	//randomiza as silabas
	private void randomizarSilabas() {
		List<String> silabas = new ArrayList<>();
		for(int i = 0; i < animaisSilabas[currentAnimalIndex].length; i++) {
			silabas.add(animaisSilabas[currentAnimalIndex][i]);
		}

		//se aa silabaa tiverem em ordem
		while(silabasIsInOrdem(silabas) == true) {
			Collections.shuffle(silabas);
			for(int i = 0; i < animaisSilabas[currentAnimalIndex].length; i++) {
				currentSilabas[i] = silabas.get(i);
			}
		}

	}

	//verifica se as silabas estao em ordem, caso true, randomiza
	private boolean silabasIsInOrdem(List<String> list) {
		String word = "";
		for(int i = 0; i < list.size(); i++) {
			word += list.get(i).toString();
		}

		if(word.equals(animais[currentAnimalIndex][0])) {
			return true;
		}
		return false;
	}
}
