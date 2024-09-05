// HELLO FROM SEATTLE //
import javax.swing.JOptionPane;
import javax.swing.*;
import java.time.*;
import java.util.Random;
import java.util.StringTokenizer;
import java.lang.Math;
import java.util.*;

public class WorldSkills
{
	static String s = "C:\\Miguel_java_work\\WorldSkills\\beep-02.wav";   // You're going to have to change this with the path of your audio file
		static SimpleAudioPlayer audioPlayer;
	public static void timer(String tempo)
	{
		int inter=Integer.parseInt(tempo);
		int intervalopersonalizado = (inter * 60000); 
		JFrame frame = new JFrame( "Timer" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		JLabel label = new JLabel( "Timer" );
		frame.getContentPane().add( label );
		frame.pack();
		frame.setVisible( true );
		long ls = System.currentTimeMillis();
		long end = ls + intervalopersonalizado; /*intervalo*/
		long i = 0;
		long minutosPassados = i / 60;
		while (System.currentTimeMillis() < end)
		{
			long curr = System.currentTimeMillis();
			if (curr >= ls + 1000)
			{
				i++;
				ls = curr;
				minutosPassados = i / 60;
				System.out.println("passou " + i + " segundos");
				label.setText("Passaram - se " + minutosPassados + " minutos, ou " + i + " segundos ");
			}
		}
		audioPlayer.playFromBeginning();
		String  hhh = 
		"Seu timer de " + tempo + " minutos acabou";
		String  kk   = "O tempo acabou!";    
		JOptionPane.showMessageDialog(null, hhh, kk, JOptionPane.PLAIN_MESSAGE );
		System.out.println("Time for break");
	}
	public static void pomodoro(String tempoum, String tempodois)
	{
		timer(tempoum);
		timer(tempodois);
	}
	
	public static void main (String [] args)
	{
		int respostas;
		int concentracao;
		int qtdestudo;
		int freqestudo;
		if (args.length > 0&& "test".equals(args[0])) {
			executarTestes();
			System.out.println("Testes finalizados!");
			System.exit(0);
		}
		
		try
		{
		 audioPlayer = new SimpleAudioPlayer(s);
		 System.out.println("Tudo Funcionando");
		}
		catch (Exception ex)
		{
		System.out.println("Error making the audio player");
		ex.printStackTrace();
		}
		String  message = 
        "Bem vindo. \nPor favor, escolha se gostaria de utilizar o cronometro, selecionar uma materia para estudo intercalado, utilizar um timer pomodoro, \nreceber dicas de estudo personalizadas, visualizar a pesquisa ou curiosidades. \n(Escreva exatamente'cronometro','pesquisa', 'curiosidades', 'dicas de estudo', 'estudo intercalado', ou 'pomodoro')";
		String  title   = "Bem vindo";    
		String  name = JOptionPane.showInputDialog(null, message, title, JOptionPane.PLAIN_MESSAGE );
		if (name == null || name.equals(""))
		{
			System.out.println ("Por favor, selecione um servico.");
		}
		
		else if (name.equalsIgnoreCase("curiosidades"))
		{
			
			
			String querVer = "Gostaria de visualizar outra curiosidade?";
			String curiosidadeUm = "Criancas treinadas musicalmente tem melhor desempenho em diversas habilidades \nmotoras e auditivas, como reconhecimento de tons e discriminacao de ritmos \n(Forgeard et al., 2008)";
			String curiosidadeDois = "O jeito que criancas iletradas processam os componentes do som: \ntom, tempo e timbre - podem prevem a futura habilidade de leitura da crianca \n(How Music and Language Shape the Brain, Northwestern University)";
			String curiosidadeTres = "Um grande numero de pesquisas utilizou imagens de ressonancia magnetica funcional (fMRI) para comparar musicos e nao musicos. \nDiferencas na atividade cerebral foram observadas em muitas regioes do cerebro quando os individuos foram solicitados a realizar tarefas musicais.";
			String curiosidadeQuatro = "Em 2020, hospitais e unidades de saude pelo Brasil aderiram ao uso de musica para auxiliar no tratamento de pacientes internados com COVID-19. \nA musicoterapia foi usada para melhorar o humor e reduzir a ansiedade, alem de promover relaxamento e bem-estar aos pacientes. \n(PUCRS)";
			String curiosidadeCinco = "Atualmente, neurocientistas tambem investigam a musica como ferramenta de intervencao em diferentes alteracoes neurologicas, como autismo e dislexia.";
			String curiosidadeSeis = "Um estudo publicado na revista Applied Cognitive Psychology, investigou os efeitos da musica classica na concentracao e na produtividade. \nOs resultados revelaram que a musica instrumental classica melhorou significativamente o desempenho em tarefas de leitura e escrita, enquanto a musica pop teve um efeito neutro. \nIsso reforca a ideia de que a musica classica sem voz pode ser uma aliada poderosa para o foco.";
			String curiosidadeSete = "A musica pode tambem cumprir papeis politicos. \nAlguns governos ditatoriais utilizaram o poder da musica a seu favor, usaram cancoes para transmitir seus valores e ideais. \nMuitos ditadores ja se valeram da musica para alienar as pessoas, nao so mediante as letras, mas tambem o ritmo. \n(Brasil Paralelo)";
			String curiosidadeOito = "A musica afeta a velocidade de crescimento das plantas. \nCientistas sul-coreanos tocaram musicas como a Sonata ao Luar de Beethoven, enquanto monitoravam o nivel de atividade genetica das plantas. (Instituto Nacional Sul-Coreano de Biotecnologia)";
			do
			{
				Random uygr = new Random();                       // GERADOR DE CURIOSIDADES ALEATORIAS
				int placeholder = uygr.nextInt(8);
				int uygr_int = placeholder / 1;
				if(uygr_int == 0)
				JOptionPane.showMessageDialog(null,curiosidadeUm);
			
				if(uygr_int == 1)
				
				JOptionPane.showMessageDialog(null,curiosidadeDois);
				
				if(uygr_int == 2)
				
				JOptionPane.showMessageDialog(null,curiosidadeTres);
				
				if(uygr_int == 3)
					
				JOptionPane.showMessageDialog(null,curiosidadeQuatro);
				
				if(uygr_int == 4)
					
				JOptionPane.showMessageDialog(null,curiosidadeCinco);
				
				if(uygr_int == 5)
					
				JOptionPane.showMessageDialog(null,curiosidadeSeis);
				
				if(uygr_int == 6)
					
				JOptionPane.showMessageDialog(null,curiosidadeSete);
				
				if(uygr_int == 7)
					
				JOptionPane.showMessageDialog(null,curiosidadeOito);
				
			}
			while (JOptionPane.showConfirmDialog(null,querVer) == JOptionPane.YES_OPTION);
			
		}
		else if (name.equals("imagem"))
		{
			
		}
		else if (name.equalsIgnoreCase("teste"))
		{
			Random ghgh = new Random();                       
				int placeholderDois = ghgh.nextInt(8);
				int ghgh_int = placeholderDois / 1;
				
				for(int popopo = 0; popopo < 1000; ++ popopo)
				{
					System.out.println("numero:" + ghgh_int);
					placeholderDois = ghgh.nextInt(8);
					ghgh_int = placeholderDois / 1;	 
				}
		}
		else if (name.equalsIgnoreCase("cronometro"))
		{		
			String  bbbb = "Selecione a duracao do cronometro (digite apenas o numero de minutos. ex: 1; 5.)";    //CRONOMETRO
			String  aaa   = "Selecione o tempo";    
			String tom = JOptionPane.showInputDialog(null, bbbb, aaa, JOptionPane.PLAIN_MESSAGE );
			timer(tom);	
		}
				else if (name.equalsIgnoreCase("pomodoro"))
		{		
			String  corpopomodoroaa = "Selecione a duracao do primeiro cronometro (digite apenas o numero de minutos. ex: 25; 30.)";   // CRONOMETRO POMODORO
			String  titulopomodoroaa   = "Selecione o tempo de estudo";    
			String pomodoroumaa = JOptionPane.showInputDialog(null, corpopomodoroaa, titulopomodoroaa, JOptionPane.PLAIN_MESSAGE );
			String  corpopomodorodoisaa = "Selecione a duracao do segundo cronometro (digite apenas o numero de minutos. ex: 25; 30.)";
			String  titulopomodorodoisaa   = "Selecione o tempo de descanso";    
			String pomodorodoisaa = JOptionPane.showInputDialog(null, corpopomodorodoisaa, titulopomodorodoisaa, JOptionPane.PLAIN_MESSAGE );
			pomodoro(pomodoroumaa,pomodorodoisaa);
		}

		else if (name.equalsIgnoreCase("pesquisa"))             //PESQUISA
		{
			String corpo = "Apos uma pesquisa sobre os efeitos musicais no cerebro, foi observado a presenca de varios beneficios ao escutar musicas durante o periodo de estudo.";
			String titulo = "Pesquisa";
			JOptionPane.showMessageDialog(null, corpo, titulo, JOptionPane.PLAIN_MESSAGE );
			
			String pesquisadois = "Um dos efeitos estudados e a criacao de um ambiente desprovido de barulhos externos que podem por em xeque a concentracao do estudante.";
			String pesquisatres = "Quando esse meio e criado, o cerebro relacionara notas musicais a medida que a leitura e exercida, \nresultando no aumento das probabilidades da retencao do conteudo estudado.";
			String pesquisaquatro = "Ademais, as musicas afetam diretamente a percepcao da passagem temporal, \nmimetizando a falsa sensacao de aceleracao ou vagareza, sendo diretamente proporcional ritmo.";
			String pesquisacinco = "O estudo intitulado ''Emotional valence contributes to music induced analgesia'', \nasserta que ouvir musica reduz a intensidade da dor.";
			String pesquisaseis = "Destarte, A musica pode influenciar significativamente uma sociedade e o cerebro de um individuo, assistindo a realizacao de diversas tarefas e cura de multiplas molestias.";
			JOptionPane.showMessageDialog(null,pesquisadois);
			JOptionPane.showMessageDialog(null,pesquisatres);
			JOptionPane.showMessageDialog(null,pesquisaquatro);
			JOptionPane.showMessageDialog(null,pesquisacinco);
			JOptionPane.showMessageDialog(null,pesquisaseis);
		}
		else if (name.equalsIgnoreCase("estudo intercalado"))             // SELETOR ALEATORIO DE MATERIAS
		{
			String outro = "Gostaria de selecionar outra materia?";
			String corpomat = "O estudo intercalado consiste em intercalar os topicos estudados para maximizar o foco.\nOferecemos um seletor aleatorio de topicos para estudo personalizado.\nPara utilizar, digite os topicos que gostaria de estudar (todos em letra minuscula, separados por ponto e virgula; ex: portugues;matematica;historia)";
			String titulomat = "Estudo Intercalado";
			String materiasParaSelecionar = JOptionPane.showInputDialog(null, corpomat, titulomat, JOptionPane.QUESTION_MESSAGE );
			String materiaSelecionada = "teste";
			String titmat = "Topico Escolhido";
			do
			{
			materiaSelecionada = sortearMateria(materiasParaSelecionar); /* Seletor esta sorteando materias repetidas, tentar remover a materia selecionada do array para consertar*/
			JOptionPane.showMessageDialog(null, materiaSelecionada);
			}
			while (JOptionPane.showConfirmDialog(null,outro) == JOptionPane.YES_OPTION);
			
		}
		
		else if (name.equalsIgnoreCase("dicas de estudo"))                 // QUESTIONARIO ------------------------------------------------------
		{
			String msgerro = "Por favor, insira um valor valido";
			String corpodois = "Voce tem facilidade em se concentrar? (escreva um numero de 1 a 5, com 5 sendo muita facilidade.";
			String titulodois = "Questionario";
			String perguntaum = JOptionPane.showInputDialog(null, corpodois, titulodois, JOptionPane.QUESTION_MESSAGE );
			respostas=Integer.parseInt(perguntaum);
			concentracao = respostas;
			if (!entreumecinco(concentracao))
				JOptionPane.showMessageDialog(null, msgerro);
			while(perguntaum.equals(""))
			{
				;
			}
			
			String corpotres = "Quanto tempo voce estuda por dia? (escreva um numero de 1 a 5, com 1 sendo muito pouco e 5 sendo muito.";
			String titulotres = "Questionario_perguntadois";
			String perguntadois = JOptionPane.showInputDialog(null, corpotres, titulotres, JOptionPane.QUESTION_MESSAGE );
			qtdestudo = Integer.parseInt(perguntadois);
			respostas = respostas + qtdestudo;
			if (!entreumecinco(qtdestudo))
				JOptionPane.showMessageDialog(null, msgerro);
			while(perguntadois.equals(""))
			{
				;
			}
			
			String corpoquatro = "Com qual frequencia voce estuda? (escreva um numero de 1 a 5, com 1 sendo uma vez ao mes e 5 sendo diariamente.";
			String tituloquatro = "Questionario_perguntatres";
			String perguntatres = JOptionPane.showInputDialog(null, corpoquatro, tituloquatro, JOptionPane.QUESTION_MESSAGE );
			freqestudo = Integer.parseInt(perguntatres);
			respostas = respostas + freqestudo;
			if (!entreumecinco(freqestudo))
				JOptionPane.showMessageDialog(null, msgerro);
			while(perguntadois.equals(""))
			{
				;
			}
			
			String corpocinco = "Voce tem uma preferencia de uma materia sobre outra? (escreva um numero de 1 a 5, com 1 sendo que avalia todas igualmente \n e 5 sendo muita diferenca em preferencia.";
			String titulocinco = "Questionario_perguntaquatro";
			String perguntaquatro = JOptionPane.showInputDialog(null, corpocinco, titulocinco, JOptionPane.QUESTION_MESSAGE );
			int preferencias = Integer.parseInt(perguntaquatro);
			if (!entreumecinco(preferencias))
				JOptionPane.showMessageDialog(null, msgerro);
			while(perguntadois.equals(""))
			{
				;
			}
			String corposeis = "Voce tem uma preferencia de um estilo musical para estudar?";
			String tituloseis = "Questionario_perguntacinco";
			String perguntacinco = JOptionPane.showInputDialog(null, corposeis, tituloseis, JOptionPane.QUESTION_MESSAGE );
			boolean estilodecente = false;
			if (perguntacinco.equalsIgnoreCase("jazz"))
				estilodecente = true;
			if (perguntacinco.equalsIgnoreCase("ambiente"))
				estilodecente = true;
			if (perguntacinco.equalsIgnoreCase("classica"))
				estilodecente = true;
			if (perguntacinco.equalsIgnoreCase("lofi"))
				estilodecente = true;
			if (perguntacinco.equalsIgnoreCase("instrumental"))
				estilodecente = true;
			if (preferencias > 2)
			{
				int estint = --preferencias; 
				String dicapreferencia = "Percebemos que voce tem uma preferencia de uma materia sobre outra. \nTente utilizar o estudo intercalado, inserindo a materia que mais tem dificuldade " + estint + " vezes, para melhorar.";
				JOptionPane.showMessageDialog(null, dicapreferencia);
			}
			;
			if(concentracao < 4)
			{
				int tempopomodoro = concentracao * 15;
				int intervalopomodoro = concentracao * 3;
				String dicapomodoro = "Percebemos que voce tem certa dificuldade em concentracao. \nEntao, recomendamos para voce o metodo pomodoro. \nInicie seus estudos com um tempo de " + tempopomodoro + " minutos de estudo, e um tempo de " + intervalopomodoro + " minutos de intervalo. \nTente cada vez mais ir aumentando a duracao de cada ciclo.";
				JOptionPane.showMessageDialog(null, dicapomodoro);
			}
			
			if(concentracao > 3 && qtdestudo < 4)
			{
				int divisor = ++qtdestudo;
				if (estilodecente)
				{
					String dicamusica = "Percebemos que voce julga nao estudar o suficiente, e tem uma boa concentracao. \nTente, ouvir musica (" + perguntacinco + ") por 1 / " + divisor + " do tempo que estiver estudando \n(tente usar o timer pomodoro para isso).";
					JOptionPane.showMessageDialog(null, dicamusica);
				}
				else
				{
					String dicamusicaalt = "Percebemos que voce julga nao estudar o suficiente, e tem uma boa concentracao. \nTente, ouvir musica por 1 / " + divisor + " do tempo que estiver estudando \n(tente usar o timer pomodoro para isso).";
					JOptionPane.showMessageDialog(null, dicamusicaalt);
				}
			}
			
		}
		else
		{
			String mensagemDeErro = "Por favor, selecione um servico valido.";
			JOptionPane.showMessageDialog(null,mensagemDeErro);
		}
	}
	
	public static boolean entreumecinco (int argumento)
	{
		if (argumento > 0 && argumento < 6)
			return true;
		else 
			return false;
	}
	
	public static String sortearMateria(String materias) {
		
		String[] elementos = materias.split(";");
		Random r = new Random();
		int posicaoSorteada = r.nextInt(elementos.length);
		
		return elementos[posicaoSorteada];
	}
	
	public static void testSortearMateria1() {
		String materia = "portugues";
		
		for (int i = 0; i< 5 ; ++i) {
			String resultado = sortearMateria(materia);
			if (!"portugues".equals(resultado)) {
				System.out.println("ERRO NO TESTE testSortearMateria1");
			}
		}
		System.out.println("testSortearMateria1 finalizado");
	}
	
	
	public static void testSortearMateria2() {
		String materia = "portugues;matematica;historia;geografia";
		
		for (int i = 0; i< 5 ; ++i) {
			String resultado = sortearMateria(materia);
			if (!"portugues".equals(resultado) &&
					!"matematica".equals(resultado) &&
					!"historia".equals(resultado) &&
					!"geografia".equals(resultado)) {
				System.out.println("ERRO NO TESTE testSortearMateria2");
			}
		}
		System.out.println("testSortearMateria2 finalizado");
	}
	
	public static boolean verificarMateriaValida(String materias) {
	if (materias==null)
		return false;
	
	if (materias.trim().equals(""))
		return false;
	
		return true;
	}
	
	
	
	public static void testValidarMaterias() {
		if (verificarMateriaValida(null))
		{
			System.out.println("erro!");
		}
		else 
		System.out.println("certo!");

	}
	
	public static void executarTestes() {
		testSortearMateria1();
		testSortearMateria2();
		testValidarMaterias();
	}
}