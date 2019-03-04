package com.acaeweb.app;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.acaeweb.app.ui.*;
import java.io.*;

public class MainActivity extends Activity 
{
	Gui gui = new Gui();
	
	public LinearLayout layout_main;
	
	Home_Screen Home_Screen;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		
		layout_main = new LinearLayout(this);
		//layout_main.setBackgroundDrawable(gui.loadTexture(MainActivity.this, "gui/Fundo_desfocado.jpg"));
        setContentView(layout_main);
		
		//create the folder of app
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.acaeweb.app");
		if(!file.exists()) {
			file.mkdirs();
		}
		
		//init the home screen
		Home_Screen = new Home_Screen(this, layout_main);
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == event.KEYCODE_BACK) {
			//load the current screen from file current_screen.dat
			gui.data.current_screen = gui.data.load("/Android/data/com.acaeweb.app/current_screen.dat");
			
			//on home screen
			if(gui.data.current_screen.equals("home_screen")) {
				finish();
			}
			
			//on exe selection
			if(gui.data.current_screen.equals("selecao_exercicio_screen")) {
				layout_main.removeAllViews();
				//init the home screen
				Home_Screen Home_Screen = new Home_Screen(this, layout_main);
			}
			
			
			//cancel the count down timer
			if(gui.data.current_screen.equals("jogo_da_memoria")) {
				Home_Screen.SelecaoExercicio_Screen.JogoDaMemoria_Screen.cT.cancel();
				layout_main.removeAllViews();
				//init the home screen
				Home_Screen = new Home_Screen(this, layout_main);
			}
			if(gui.data.current_screen.equals("brincando_numeros_screen")) {
				Home_Screen.SelecaoExercicio_Screen.BrincandoComNumeros_Screen.cT.cancel();
				layout_main.removeAllViews();
				//init the home screen
				Home_Screen = new Home_Screen(this, layout_main);
			}
			if(gui.data.current_screen.equals("jogo_das_silabas")) {
				Home_Screen.SelecaoExercicio_Screen.JogoDasSilabas_Screen.cT.cancel();
				layout_main.removeAllViews();
				//init the home screen
				Home_Screen = new Home_Screen(this, layout_main);
			}
		}
		return false;
	}
	
	
}
