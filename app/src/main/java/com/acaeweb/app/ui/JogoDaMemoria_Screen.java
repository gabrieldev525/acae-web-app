package com.acaeweb.app.ui;

import android.app.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.acaeweb.app.*;
import com.acaeweb.app.Anim.*;
import java.io.*;
import java.util.*;

public class JogoDaMemoria_Screen extends Gui {
	private Activity ctx;
	private LinearLayout layout;
	GameOver_Screen GameOver_Screen;
	Animation animation;

	private LinearLayout jogodamemoria_layout;
	private TextView timer_text;
	private TextView ajuda_text;

	//cartas
	private LinearLayout cartas_layout_linha[] = new LinearLayout[2];
	private LinearLayout cartas_layout[] = new LinearLayout[6];

	private int cartas_desviradas[] = {-1, -1};
	private int pares_acertados[] = {
		-1, -1, -1, -1,
		-1, -1, -1, -1
	};
	private int cartasQuantidade = 6;

	public CountDownTimer cT;
	int countDownTimer = 61000;
	String timerMin = "0";
	int timerSeg = 00;

	//animations
	android.view.animation.Animation cartasVirando;
	Flip3dAnimation animCartas;


	//todos os animais
	private String animais[][] = {
		//nome do animal, textura
		{"Arara", "animais/Arara.png"},
		{"Baleia", "animais/Baleia.png"},
		{"Cachorro", "animais/Cachorro.png"},
		{"Cobra", "animais/Cobra.png"},
		{"Galinha", "animais/Galinha.png"},
		{"Gato", "animais/Gato.png"},
		{"Peixe", "animais/Peixe.png"},
		{"Polvo", "animais/Polvo.png"},
		{"Urso", "animais/Urso.png"},
		{"Vaca", "animais/Vaca.png"}
	};

	//index da array animais em que estao os animais atual selecionado
	private int currentCartasAnimais[] = {-1, -1, -1, -1};

	//define a posição atual das cartas e cada animal
	private int animaisCartasPosicoes[] = {
		-1, -1, -1, -1,
		-1, -1, -1, -1
	};


	int score = 0;
	int ajudas = 3;

	public JogoDaMemoria_Screen(Activity context, LinearLayout layout) {
		this.ctx = context;
		this.layout = layout;
		animation = new Animation(ctx);

		cartasVirando = new android.view.animation.RotateAnimation(100, 0);
		cartasVirando.setDuration(1000);


		initFonts(ctx);
		//inicializa
		init();
		screen();

		//faz a contagem regressiva do tempo - 59s
		createCountDown();

		data.current_screen = "jogo_da_memoria";
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
				GameOver_Screen.setScore("Pontos: " + score);
				GameOver_Screen.buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View p1) {
						sound.buttonClick(ctx);
						layout.removeAllViews();
						JogoDaMemoria_Screen BrincandoComNumeros_Screen = new JogoDaMemoria_Screen(ctx, layout);
						GameOver_Screen.hide();
					}
				});
			}

		};
		cT.start();
	}

	public void init() {
		initVariables();
		sortearAnimais();
	}

	private void initVariables() {
		//cartas_desviradas[]
		cartas_desviradas[0] = -1;
		cartas_desviradas[1] = -1;

		//pares_acertados[]
		pares_acertados = new int[cartasQuantidade];
		for(int i = 0; i < pares_acertados.length; i++) {
			pares_acertados[i] = -1;
		}

		//currentCartasAnimais[]
		currentCartasAnimais = new int[cartasQuantidade / 2];
		for(int i = 0; i < currentCartasAnimais.length; i++) {
			currentCartasAnimais[i] = -1;
		}

		//animaisCartasPosicoes[]
		animaisCartasPosicoes = new int[cartasQuantidade];
		for(int i = 0; i < currentCartasAnimais.length; i++) {
			currentCartasAnimais[i] = -1;
		}
	}

	public void screen() {
		try {
			jogodamemoria_layout = LinearLayout(ctx, 0, 0, getWidth(ctx), getHeight(ctx));
			jogodamemoria_layout.setOrientation(LinearLayout.VERTICAL);
			jogodamemoria_layout.setGravity(Gravity.CENTER_HORIZONTAL);
			jogodamemoria_layout.setBackgroundColor(Color.WHITE);
			layout.addView(jogodamemoria_layout);

			//timer
			LinearLayout timer_layout = LinearLayout(ctx, 0, 0, getWidth(ctx) / 2, getHeight(ctx) / 11);
			timer_layout.setBackgroundDrawable(loadTexture(ctx, "gui/Timer.png"));
			timer_layout.setGravity(Gravity.CENTER | Gravity.CENTER);
			jogodamemoria_layout.addView(timer_layout);
			//timer text
			timer_text = label(ctx, 0, 0, timerMin + ":" + String.format("%02d", timerSeg), 24, "#FFFFFF", false, "#000000");
			timer_layout.addView(timer_text);

			//cartas
			LinearLayout cartasLayout = LinearLayout(ctx, 0, getHeight(ctx) / 8, getWidth(ctx), getHeight(ctx) / 5 * 2 + getHeight(ctx) / 20);
			cartasLayout.setOrientation(LinearLayout.VERTICAL);
			jogodamemoria_layout.addView(cartasLayout);

			//linha de cartas
			for(int i = 0; i < 2; i++) {
				cartas_layout_linha[i] = LinearLayout(ctx, 0, getHeight(ctx) / 40, getWidth(ctx), (getWidth(ctx) / ((cartasQuantidade + 3) / 2)) + getHeight(ctx) / 24);
				cartas_layout_linha[i].setGravity(Gravity.CENTER_HORIZONTAL);
				cartasLayout.addView(cartas_layout_linha[i]);
			}

			//cartas
			int currentCartasLinha = 0;
			cartas_layout = new LinearLayout[cartasQuantidade];
			for(int i = 0; i < cartasQuantidade; i++) {
				cartas_layout[i] = LinearLayout(ctx, (getWidth(ctx) - (getWidth(ctx) / (((cartasQuantidade + 3) / 2) / 2))) / (cartasQuantidade * 4) , 0, getWidth(ctx) / ((cartasQuantidade + 3) / 2), (getWidth(ctx) / ((cartasQuantidade + 3) / 2)) + getHeight(ctx) / 24);
				cartas_layout[i].setBackgroundDrawable(loadTexture(ctx, "gui/Carta_Costa.png"));
				cartas_layout[i].setId(i);
				if(i == Math.floor(cartasQuantidade / 2)) {
					currentCartasLinha++;
				}
				cartas_layout[i].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View p1) {
						if(p1.isClickable() == true) {
							if(cartaDesvirada(p1.getId()) == false) {
								if(cartas_desviradas[0] == -1) {
									setCartasClickable(false);
									applyRotation(p1, cartaDesvirada(p1.getId()));
									cartas_desviradas[0] = p1.getId();

									//verifica se o par é igual
									p1.postDelayed(new Runnable() {
										@Override
										public void run() {
											if(cartas_desviradas[1] != -1 && isPar(animaisCartasPosicoes[cartas_desviradas[0]], animaisCartasPosicoes[cartas_desviradas[1]]) == true) {
												setCartasClickable(cartas_desviradas[0], false);
												setCartasClickable(cartas_desviradas[1], false);
												//push
												pushValueOnPares_Acertados(cartas_desviradas[0]);
												pushValueOnPares_Acertados(cartas_desviradas[1]);

												//remove os pares acertados
												/*cartas_layout_linha[(int) Math.floor(cartas_layout[cartas_desviradas[0]].getId() / 3)].removeView(cartas_layout[cartas_desviradas[0]]);
												 cartas_layout_linha[(int) Math.floor(cartas_layout[cartas_desviradas[1]].getId() / 3)].removeView(cartas_layout[cartas_desviradas[1]]);*/

												cartas_desviradas[0] = -1;
												cartas_desviradas[1] = -1;
											} else if(cartas_desviradas[1] != -1 && isPar(cartas_desviradas[0], cartas_desviradas[1]) == false) {
												applyRotation(cartas_layout[cartas_desviradas[0]], true);
												applyRotation(cartas_layout[cartas_desviradas[1]], true);

												cartas_desviradas[0] = -1;
												cartas_desviradas[1] = -1;
											}


											setCartasClickable(true);

											//fim de jogo
											if(isGameOver() == true) {
												Toast.makeText(ctx, "Fim de jogo", Toast.LENGTH_SHORT).show();
											}
										}
									}, animCartas.getDuration());

								} else if(cartas_desviradas[1] == -1) {
									applyRotation(p1, cartaDesvirada(p1.getId()));
									setCartasClickable(false);
									cartas_desviradas[1] = p1.getId();

									//verifica se o par é igual
									p1.postDelayed(new Runnable() {
										@Override
										public void run() {
											if(cartas_desviradas[0] != -1 && isPar(animaisCartasPosicoes[cartas_desviradas[0]], animaisCartasPosicoes[cartas_desviradas[1]]) == true) {
												setCartasClickable(cartas_desviradas[0], false);
												setCartasClickable(cartas_desviradas[1], false);
												//push
												pushValueOnPares_Acertados(cartas_desviradas[0]);
												pushValueOnPares_Acertados(cartas_desviradas[1]);

												//remove os pares acertados
												/*cartas_layout_linha[(int) Math.floor(cartas_layout[cartas_desviradas[0]].getId() / 3)].removeView(cartas_layout[cartas_desviradas[0]]);
												 cartas_layout_linha[(int) Math.floor(cartas_layout[cartas_desviradas[1]].getId() / 3)].removeView(cartas_layout[cartas_desviradas[1]]);*/

												cartas_desviradas[0] = -1;
												cartas_desviradas[1] = -1;
											} else if(cartas_desviradas[0] != -1 && isPar(cartas_desviradas[0], cartas_desviradas[1]) == false) {
												applyRotation(cartas_layout[cartas_desviradas[0]], true);
												applyRotation(cartas_layout[cartas_desviradas[1]], true);

												cartas_desviradas[0] = -1;
												cartas_desviradas[1] = -1;
											}

											setCartasClickable(true);

											ifgameover();
										}
									}, animCartas.getDuration());
								}
							} else {
								applyRotation(p1, cartaDesvirada(p1.getId()));
								cartas_desviradas[getCartaDesviradaIndex(p1.getId())] = -1;
							}
						}
					} 
				});
				cartas_layout_linha[currentCartasLinha].addView(cartas_layout[i]);
			}

			//area menu
			LinearLayout area_menu_layout = LinearLayout(ctx, 0, getHeight(ctx) / 4, getWidth(ctx), getHeight(ctx) / 9);
			area_menu_layout.setBackgroundDrawable(loadTexture(ctx, "gui/Area_Menu.png"));
			area_menu_layout.setGravity(Gravity.CENTER_VERTICAL);
			jogodamemoria_layout.addView(area_menu_layout);

			ajuda_text = label(ctx, getWidth(ctx) / 10, 0, "Ajuda(" + ajudas + ")", getWidth(ctx) / 16, "#FF2416", false, "#FF0000");
			ajuda_text.setTypeface(fontBromo);
			ajuda_text.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					if(ajudas > 0) {
						ajudas --;
						ajuda_text.setText("Ajuda(" + ajudas + ")");

						int random = (int) Math.floor(Math.random() * cartasQuantidade);
						while(cartaAcertada(random) == true || cartaDesvirada(random) == true) {
							random = (int) Math.floor(Math.random() * animaisCartasPosicoes.length);
						}
						applyRotation(cartas_layout[random], false);

						if(cartas_desviradas[0] == -1) {
							cartas_desviradas[0] = random;
							setCartasClickable(cartas_desviradas[0], false);
						} else if(cartas_desviradas[1] == -1) {
							cartas_desviradas[1] = random;
							ajuda_text.postDelayed(new Runnable() {
								@Override
								public void run() {
									if(cartas_desviradas[0] != -1 && isPar(animaisCartasPosicoes[cartas_desviradas[0]], animaisCartasPosicoes[cartas_desviradas[1]]) == false) {
										applyRotation(cartas_layout[cartas_desviradas[0]], true);
										applyRotation(cartas_layout[cartas_desviradas[1]], true);

										cartas_desviradas[0] = -1;
										cartas_desviradas[1] = -1;
									} else if(cartas_desviradas[1] != -1 && isPar(animaisCartasPosicoes[cartas_desviradas[0]], animaisCartasPosicoes[cartas_desviradas[1]]) == true) {
										setCartasClickable(cartas_desviradas[0], false);
										setCartasClickable(cartas_desviradas[1], false);
										//push
										pushValueOnPares_Acertados(cartas_desviradas[0]);
										pushValueOnPares_Acertados(cartas_desviradas[1]);

										cartas_desviradas[0] = -1;
										cartas_desviradas[1] = -1;

										ifgameover();
									}
								}
							}, 1000);
						}
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

	private void ifgameover() {
		//fim de jogo
		if(isGameOver() == true) {
			//Toast.makeText(ctx, "Fim de jogo", Toast.LENGTH_SHORT).show();
			score++;

			layout.removeView(jogodamemoria_layout);
			if(score == 5) {
				cartasQuantidade += 2;
				countDownTimer += 15000;
				cT.cancel();
				createCountDown();

				showToast(ctx, "+15 seg", "#60C400");
			} else {
				int aum = 0;
				if(cartasQuantidade == 6) {
					aum = 8000;
				} else if(cartasQuantidade > 6) {
					aum = 14000;
				}
				countDownTimer += aum;
				cT.cancel();
				createCountDown();

				showToast(ctx, "+" + (aum / 1000) + " seg", "#60C400");
			}

			if(score % 5  == 0) {
				ajudas++;
				ajuda_text.setText("Ajuda(" + ajudas + ")");

				showToast(ctx, "+1 ajuda", "#60C400");
			}

			sound.levelUp(ctx);
			init();
			screen();
		}
	}

	//rotorna se a carta estar desvirada
	public boolean cartaDesvirada(int id) {
		for(int i = 0; i < cartas_desviradas.length; i++) {
			if(cartas_desviradas[i] == id) {
				return true;
			}
		}
		return false;
	}

	//retorna a posição da carta na array cartas_desviradas
	public int getCartaDesviradaIndex(int id) {
		for(int i = 0; i < cartas_desviradas.length; i++) {
			if(cartas_desviradas[i] == id) {
				return i;
			}
		}
		return -1;
	}

	//aplica a rotação nas cartas
	private void applyRotation(final View view, boolean virado) {
		animCartas = new Flip3dAnimation(view, virado);
		animCartas.applyPropertiesInRotation(800);
		view.startAnimation(animCartas);
		view.postDelayed(new Runnable() {

			@Override
			public void run() {
				if(cartaDesvirada(view.getId())) {
					//vira a carta e pega o animal daquela correpondente carta
					view.setBackgroundDrawable(drawCartaFrente(animais[animaisCartasPosicoes[view.getId()]][1]));
				} else {
					try {
						view.setBackgroundDrawable(loadTexture(ctx, "gui/Carta_Costa.png"));
					} catch(IOException e) {}
				}
			}


		}, animCartas.getDuration() - 200);
	}

	//desenha a frente da carta
	private BitmapDrawable drawCartaFrente(String animalTexture) {
		Bitmap blank = Bitmap.createBitmap(172, 226, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(blank);

		try {
			//carta desvirada
			canvas.drawBitmap(BitmapFactory.decodeStream(ctx.getAssets().open("gui/Carta_Frente.png")), 0, 0, null);
			//animal
			canvas.drawBitmap(BitmapFactory.decodeStream(ctx.getAssets().open(animalTexture)), (canvas.getWidth() - 149) / 2, (canvas.getHeight() - 136) / 2, null);
		} catch(IOException e) {}

		return new BitmapDrawable(blank);
	}

	//sortea os animais para a rodada atual
	private void sortearAnimais() {
		for(int i = 0; i < cartasQuantidade / 2; i++) {
			if(currentCartasAnimais[i] == -1) {
				int random = (int) Math.floor((Math.random() * animais.length));
				//verifica se o animal ja foi sorteado alguma vez, caso sim, sorteia outro
				while(isSorteado(random) == true && i > 0) {
					random = (int) Math.floor((Math.random() * animais.length));
				}
				currentCartasAnimais[i] = random;
			}
		}

		//define a posição de cada animal nas cartas (em ordem)
		int n = 0;
		for(int i = 0; i < animaisCartasPosicoes.length; i++) {
			if(i % 2 == 0 && i > 0) {
				n++;
			}
			animaisCartasPosicoes[i] = currentCartasAnimais[n];
		}

		//random as posicoes das cartas
		randomAnimalPosition();
	}

	//random as posicoes das cartas
	private void randomAnimalPosition() {
		ArrayList<Integer> list = new ArrayList<>();
		for(int i = 0; i < animaisCartasPosicoes.length; i++) {
			list.add(animaisCartasPosicoes[i]);
		}

		Collections.shuffle(list);

		for(int i = 0; i < list.size(); i++) {
			animaisCartasPosicoes[i] = list.get(i);
		}
	}



	private boolean allPositionRandom() {
		for(int i = 0; i < animaisCartasPosicoes.length; i++) {
			if(animaisCartasPosicoes[i] == -1) {
				return false;
			}
		}
		return true;
	}

	//retorna se um animal ja foi sorteado
	private boolean isSorteado(int index) {
		for(int i = 0; i < currentCartasAnimais.length; i++) {
			if(currentCartasAnimais[i] == index) {
				return true;
			}
		}
		return false;
	}

	private boolean haveCartasOnPosition(int index) {
		for(int i = 0; i < animaisCartasPosicoes.length; i++) {
			if(animaisCartasPosicoes[i] == index) {
				return true;
			}
		}
		return false;
	}

	//verifica se as cartas sao iguais
	private boolean isPar(int index1, int index2) {
		return (index1 == index2);
	}

	//define as cartas clicaveis
	private void setCartasClickable(boolean clickable) {
		for(int i = 0; i < cartas_layout.length; i++) {
			if(cartaAcertada(i) == false) {
				cartas_layout[i].setClickable(clickable);
			}
		}
	}

	private boolean cartaAcertada(int index) {
		for(int i = 0; i < pares_acertados.length; i++) {
			if(pares_acertados[i] == index) {
				return true;
			}
		}
		return false;
	}

	//define as cartas clicavel
	private void setCartasClickable(int cartaIndex, boolean clickable) {
		cartas_layout[cartaIndex].setClickable(clickable);
	}

	//adiciona um valor na array pares_acertados
	private void pushValueOnPares_Acertados(int value) {
		for(int i = 0; i < pares_acertados.length; i++) {
			if(pares_acertados[i] == -1) {
				pares_acertados[i] = value;
				break;
			}
		}
	}

	//verifica se o jogo terminou
	private boolean isGameOver() {
		for(int i = 0; i < pares_acertados.length; i++) {
			if(pares_acertados[i] == -1) {
				return false;
			}
		}
		return true;
	}

	//push a array int
	private int[] push(int[] array, int valor) {
		int array1[] = new int[array.length + 1];
		for(int i = 0; i < array.length; i++) {
			array1[i] = array[i];
		}
		array1[array.length] = valor;
		return array1;
	}

	//push a array String
	private String[] push(String[] array, String valor) {
		String array1[] = new String[array.length + 1];
		for(int i = 0; i < array.length; i++) {
			array1[i] = array[i];
		}
		array1[array.length] = valor;
		return array1;
	}
}
