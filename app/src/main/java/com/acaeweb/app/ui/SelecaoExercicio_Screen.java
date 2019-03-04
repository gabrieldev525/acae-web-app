package com.acaeweb.app.ui;

import android.app.*;
import android.widget.*;
import com.acaeweb.app.*;
import java.io.*;
import android.view.*;
import com.acaeweb.app.Anim.*;

public class SelecaoExercicio_Screen extends Gui {
	private Activity ctx;
	private LinearLayout layout;

	private LinearLayout SelecaoExercicioLayout;

	private Animation animation;

	public BrincandoComNumeros_Screen BrincandoComNumeros_Screen;
	public JogoDaMemoria_Screen JogoDaMemoria_Screen;
	public JogoDasSilabas_Screen JogoDasSilabas_Screen;

	public SelecaoExercicio_Screen(Activity contexto, LinearLayout layout) {
		this.ctx = contexto;
		this.layout = layout;

		animation = new Animation(contexto);

		screen();

		data.current_screen = "selecao_exercicio_screen";
		data.save(appFolderDir + "/current_screen.dat", data.current_screen.getBytes());
	}

	public void screen() {
		try {
			SelecaoExercicioLayout = LinearLayout(ctx, 0, 0, getWidth(ctx), getHeight(ctx));
			SelecaoExercicioLayout.setBackgroundDrawable(loadTexture(ctx, "gui/Fundo_desfocado.jpg"));
			SelecaoExercicioLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			SelecaoExercicioLayout.setOrientation(LinearLayout.VERTICAL);
			layout.addView(SelecaoExercicioLayout);

			LinearLayout title = LinearLayout(ctx, 0, getHeight(ctx) / 30, getWidth(ctx) / 1 - getWidth(ctx) / 3, getHeight(ctx) / 9);
			title.setBackgroundDrawable(loadTexture(ctx, "gui/Title Jogos.png"));
			SelecaoExercicioLayout.addView(title);

			//jogo da memoria
			LinearLayout lista_jogodamemoria = LinearLayout(ctx, 0, getHeight(ctx) / 30, getWidth(ctx), getHeight(ctx) / 8);
			lista_jogodamemoria.setBackgroundDrawable(loadTexture(ctx, "gui/Botao_1.png"));
			lista_jogodamemoria.startAnimation(animation.translate_from_left);
			lista_jogodamemoria.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					layout.removeAllViews();
					JogoDaMemoria_Screen = new JogoDaMemoria_Screen(ctx, layout);
				}
			});
			SelecaoExercicioLayout.addView(lista_jogodamemoria);

			//jogo das silabas
			LinearLayout lista_jogosilabas = LinearLayout(ctx, 0, getHeight(ctx) / 30, getWidth(ctx), getHeight(ctx) / 8);
			lista_jogosilabas.setBackgroundDrawable(loadTexture(ctx, "gui/Botao2.png"));
			lista_jogosilabas.startAnimation(animation.translate_from_right);
			lista_jogosilabas.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					layout.removeAllViews();
					JogoDasSilabas_Screen = new JogoDasSilabas_Screen(ctx, layout);
				}
			});
			SelecaoExercicioLayout.addView(lista_jogosilabas);

			//brincando com a matematica
			LinearLayout lista_brincandomatematica = LinearLayout(ctx, 0, getHeight(ctx) / 30, getWidth(ctx), getHeight(ctx) / 8);
			lista_brincandomatematica.setBackgroundDrawable(loadTexture(ctx, "gui/Botao3.png"));
			lista_brincandomatematica.startAnimation(animation.translate_from_left);
			lista_brincandomatematica.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View p1) {
					sound.buttonClick(ctx);
					layout.removeAllViews();
					BrincandoComNumeros_Screen = new BrincandoComNumeros_Screen(ctx, layout);
				}
			});
			SelecaoExercicioLayout.addView(lista_brincandomatematica);


		} catch(IOException e) {}
	}
}
