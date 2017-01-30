package fr.sgr.formation.voteapp;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.sgr.formation.voteapp.trace.modele.Trace;
import fr.sgr.formation.voteapp.trace.services.TraceInvalideException;
import fr.sgr.formation.voteapp.trace.services.TraceServices;
import fr.sgr.formation.voteapp.utilisateurs.modele.Adresse;
import fr.sgr.formation.voteapp.utilisateurs.modele.ProfilsUtilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Utilisateur;
import fr.sgr.formation.voteapp.utilisateurs.modele.Ville;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateurInvalideException;
import fr.sgr.formation.voteapp.utilisateurs.services.UtilisateursServices;
import fr.sgr.formation.voteapp.utilisateurs.services.VilleService;
import lombok.extern.slf4j.Slf4j;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class Initialisation {

	@Autowired
	private VilleService villeService;
	@Autowired
	private UtilisateursServices utilisateursServices;
	@Autowired
	private TraceServices traceServices;

	@SuppressWarnings("deprecation")
	@PostConstruct
	@Transactional(propagation = Propagation.REQUIRED)
	/**
	 * Méthode pour créer des données initiales
	 * @throws UtilisateurInvalideException
	 * @throws TraceInvalideException
	 */
	public void init() throws UtilisateurInvalideException, TraceInvalideException {
		log.info("Initialisation des villes par défaut dans la base...");
		String[] nomsVilles = {"BORDEAUX","TOULOUSE","LYON","MARSEILLE","RENNES","PARIS","CASTELNAU TURSAN","CASTELNER","CASTETS","CAZERES SUR L ADOUR","CLASSUN","CLEDES","CLERMONT","ESCALANS","FARGUES","GAILLERES","GARROSSE","GEAUNE","GOUTS","HASTINGUES","HAUT MAUCO","HERM","HERRE","HEUGAS","HINX","LABOUHEYRE","LACQUY","LAGLORIEUSE","LAUREDE","LENCOUACQ","LEON","LESGOR","LINXE","LOURQUEN","LUXEY","MAGESCQ","MAILLAS","MAILLERES","MANT","MAURIES","MAURRIN","MONSEGUR","MONTAUT","MOUSTEY","MUGRON","ONARD","OZOURT","POMAREZ","PORT DE LANNE","POUYDESSEAUX","PUJO LE PLAN","ST ETIENNE D ORTHE","ST MARTIN DE HINX","ST SEVER","ST YAGUEN","SEIGNOSSE","SOLFERINO","TOULOUZETTE","VERT","LE VIGNAU","AMBLOY","AVARAY","CHAUMONT SUR LOIRE","CHAUMONT SUR THARONNE","CHAUVIGNY DU PERCHE","CHEMERY","CHITENAY","CORMENON","CORMERAY","CROUY SUR COSSON","EPIAIS","FEINGS","LA FERTE ST CYR","LA FONTENELLE","FORTAN","FOSSE","FOUGERES SUR BIEVRE","LE GAULT PERCHE","GOMBERGEAN","HERBAULT","JOSNES","LANGON","LESTIOU","LA MADELEINE VILLEFROUIN","MARAY","MESLAND","MONTRICHARD VAL DE CHER","MONTROUVEAU","NOURRAY","NOYERS SUR CHER","OISLY","BEAUCE LA ROMAINE","BEAUCE LA ROMAINE","PEZOU","PONTLEVOY","PRAY","RHODON","RUAN SUR EGVONNE","ST ARNOULT","ST CYR DU GAULT","ST GERVAIS LA FORET","ST JULIEN DE CHEDON","ST RIMAY","SANTENAY","SARGE SUR BRAYE","SAVIGNY SUR BRAYE"};
		String[] cpVilles = {"33000","31000","69000","13000","35000","75000","40320","40700","40260","40270","40320","40320","40180","40310","40500","40090","40110","40320","40400","40300","40280","40990","40310","40180","40180","40210","40120","40090","40250","40120","40550","40400","40260","40250","40430","40140","40120","40120","40700","40320","40270","40700","40500","40410","40250","40380","40380","40360","40300","40120","40190","40300","40390","40500","40400","40510","40210","40250","40420","40270","41310","41500","41150","41600","41270","41700","41120","41170","41120","41220","41290","41120","41220","41270","41360","41330","41120","41270","41310","41190","41370","41320","41500","41370","41320","41150","41400","41800","41310","41140","41700","41160","41240","41100","41400","41190","41290","41270","41800","41190","41350","41400","41800","41190","41170","41360"};
		for (int i=0; i<nomsVilles.length; i++){
			Ville ville = new Ville();
			ville.setCodePostal(cpVilles[i]);
			ville.setNom(nomsVilles[i]);
			villeService.creer(ville);
		}
		
		log.info("Initialisation d'utilisateurs par défaut");
		String[] listeNom = {"Garcia","Blanc","Serra","Guisset","Martinez","Mas","Pla","Sola","Lopez","Torres","Gil","Richard","Sanchez","Simon","Esteve","Salvat","Vidal","Bertrand","Bonnet","Mestres","Perez","Batlle","Brunet","Tixador","Marty","Costa","Canal","Arnaud","Martin","Carrere","Garrigue","Rolland","Fernandez","Ribes","Roger","Comes","Fabre","Navarro","Bernard","Pastor","Rodriguez","Olive","Diaz","Julia","Pages","Grau","Robert","Delmas","Ruiz","Moreno","Prats","Bonafous","Pujol","Bosch","Sarda","Fourcade","Ferrer","Munoz","Petit","Erre","Puig","Verges","Sala","Nogues","Durand","Roca","Maury","Bonet","Gomez","Roig","Planas","Camps","Soler","Font","Raynaud","Raynal","Vila","Parent","Bataille","Roque","Gonzalez","Bousquet","Michel","Mathieu","Hernandez","Gimenez","Rey","Payre","Coste","Coll","Boyer","Dubois","Calvet","Thomas","Fons","Sales","Pons","Marti","Figueres","Guerrero"};
		String[] listePrenom ={"Ivy","Janna","Kendra","Leia","Linoy","Loane","Nathalie","Nelya","Safia","Salimata","Alima","Blessing","Dyna","Florence","Layna","Matilda","Matilde","Aaron","Abdellah","Adam","Agathe","Ahmed","Aicha","Aissatou","Alban","Alexis","Alix","Alma","Amara","Amaury","Amel","Amelia","Aminata","Amir","Ana","Anastasia","Andy","Angele","Angelique","Angelo","Anna","Anthony","Arnaud","Arsene","Ashley","Asma","Athena","Auguste","Aurele","Avi","Awa","Ayman","Baptiste","Benjamin","Brayan","Briac","Brian","Brieuc","Bruno","Bryan","Colombe","Constance","Constantin","Damien","Delphine","Diana","Dimitri","Dounia","Eden","Eglantine","Elea","Elena","Eleonore","Eliane","Eliot","Eliott","Elliot","Elouan","Elya","Emy","Enora","Esteban","Esther","Ethel","Fatima","Flavio","Florence","Florent","Franck","Gabin","Gad","Giulia","Helene","Heloise","Henri","Hugues","Ibrahim","Idriss","Igor","Ilan","Jacob","Mateo","Tao","Zadig","Alessandro","Ivan","Olivier","Wael","Andy","Calixte","Damien","Dany","Ewen","Gaetan","Lazare","Matteo","Yassin","Yossef","Aksil","Alois","Armel","Jeremy","Luc","Melvil","Yacouba","Elouan","Levi","Loan","Makan","Malone","Morgan","Mylan","Yannis","Yoan","Adrian","Daoud","Eloi","Guilhem","Hedi","Kenzi","Leon","Mouhamadou","Muhammad","Niels","Nohe","Ahmad","Anass","Cheikh","Darius","elio","Ennio","Idir","Jonah","Joud","Manil","Marcello","Michael","Nayel","Philemon","Rayen","Taym","Aissa","Aris","Edward","Fabien","Harold","John","Juba","Kilian","Lukas","Mady","Michael","Moise","Oskar","Seydina","Swan","Wesley","Sarah","Sofia","Josephine","Margaux","Valentine","Alix","Ava","Marie","Suzanne","Clara","Pauline","Alicia","Laura","Sophia","Maya","Capucine","Noemie","Elise","Assia"};
		String listeCharMdp = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890/*-+";
		String[] listeRue = {"RUE DES ABATTOIRS","RUE ADRIEN PICHON","RUE DES AJONCS","RUE ALAIN GERBAULT","RUE ALBERT CAMUS","RUE ALBERT EINSTEIN","RUE ALBERT SCHWEITZER","RUE ALBERT THOMAS","RUE ALEXANDRE DUMAS","RUE ALEXANDRE LEDRU ROLLIN","RUE ALEXANDRE VEZIN","RUE ALFRED DE MUSSET","RUE ALFRED DE VIGNY","RUE ALFRED KASTLER","RUE ALFRED NOBEL","RUE ALPHONSE DAUDET","RUE ALPHONSE DE LAMARTINE","RUE DES AMANDIERS","RUE AMBROISE PARE","RUE AMIRAL COURBET","RUE ANATOLE FRANCE","RUE ANCIEN HOPITAL","RUE DE L ANCIEN HOTEL DE VILLE","RUE ANDERS CELSIUS","RUE ANDRE AMPERE","RUE ANDRE CHENIER","RUE ANDRE LE NOTRE","RUE ANGELA DUVAL","RUE ANITA CONTI","RUE D ANJOU","RUE ANNIE GIRARDOT","RUE ANTOINE DE BAIF","RUE ANTOINE DE CONDORCET","RUE ANTOINE DE JUSSIEU","RUE ANTOINE DE SAINT EXUPERY","RUE ANTOINE LAVOISIER","RUE ANTOINE PARMENTIER","RUE DES ARDOISES","RUE ARISTIDE BRIAND","RUE ARMAND BARBES","RUE DES ARMATEURS","RUE ARSENE D ARSONVAL","RUE ARSENE NOUTEAU","RUE ARTHUR RIMBAUD","RUE A SANTOS DUMONT","RUE AUGUSTE BAPTISTE LECHAT","RUE AUGUSTE BRIZEUX","RUE AUGUSTE CHENEVEAUX","RUE AUGUSTE COMTE","RUE AUGUSTE PICCARD","RUE AUGUSTE RODIN","RUE AUGUSTE RENOIR","RUE AUGUSTIN FRESNEL","RUE AUTEL DES DRUIDES","RUE DE L AVIATION","RUE D AVILES","RUE BAC DE MINDIN","RUE BAPTISTE MARCET","RUE DE BEL AIR","RUE BENJAMIN FRANKLIN","RUE BERNARD PALISSY","RUE DE LA BERTHAUDERIE","RUE BLAISE PASCAL","RUE DU BOIS SAVARY","RUE DES BOULEAUX","RUE DE BRETAGNE","RUE DE LA BRIANDAIS","RUE DU BRIVET","RUE DES CABOTEURS","RUE CAMILLE DESMOULINS","RUE CAMILLE PELLETAN","RUE DU CAMPUS","RUE DU CAPITAINE DAUCE","RUE DE CARDURAND","RUE DE LA CASERNE","RUE DES CEDRES","RUE DES CELTES","RUE CELESTIN FREINET","RUE CESAR FRANCK","RUE DES CHANTIERS","RUE DES CHARDONNERETS","RUE CHARLES BAUDELAIRE","RUE CHARLES BRUNELLIERE","RUE CHARLES COULOMB","RUE CHARLES DELESCLUZE","RUE CHARLES DE MONTESQUIEU","RUE CHARLES GARNIER","RUE CHARLES LE GOFFIC","RUE CHARLES GOUNOD","RUE CHARLES LEBRUN","RUE CHARLES LINDBERG","RUE CHARLES LONGUET","RUE CHARLES SAINTE BEUVE","RUE CHEVALIER DE SAINT GEORGE","RUE CHRISTOPHE COLOMB","RUE CLAUDE BERNARD","RUE CLAUDE BERTHOLLET","RUE CLAUDE CHAPPE","RUE CLAUDE DEBUSSY","RUE CLAUDE PERRAULT","RUE CLEMENT ADER","RUE CLEMENT MAROT","RUE DU CLOS DE L ETANG","RUE COMMANDANT CHARCOT","RUE DU CDT GUSTAVE GATE","RUE DES COMMANDIERES","RUE DE LA COMMUNE DE PARIS","RUE DES CORMORANS","RUE DE LA COTE D ARGENT","RUE DE LA COTE D OPALE","RUE DE LA COTE D EMERAUDE","RUE DE LA COTE DE JADE","RUE DE LA COTE DE NACRE","RUE DU CORPS DE GARDE","RUE DU CROISIC","RUE LA CROIX AMISSE","RUE DE LA CROIX DE MEAN","RUE DE LA CROIX FRAICHE","RUE DANIEL AUBER","RUE DANIEL MAYER","RUE DENIS DIDEROT","RUE DENIS PAPIN","RUE DES DENTELIERES","RUE DE LA DERMURIE","RUE DESIREE TARTOUE","RUE DE TOULOUSE LAUTREC","RUE DIEUDONNE COSTES","RUE DU DOC PIERRE ROUX","RUE DOCTEUR XAVIER BICHAT","RUE DU DOLMEN","RUE DE L ECLUSE","RUE EDGAR DEGAS","RUE EDGAR QUINET","RUE EDMOND ABOUT","RUE EDMOND ROSTAND","RUE EDOUARD BRANLY","RUE EDOUARD VAILLANT","RUE EMILE BROODCOORENS","RUE ELISEE NEGRIN 1903-1930","RUE EMILE COMBES","RUE EMILE LITTRE","RUE EMILE OLLIVAUD","RUE DES ERABLES","RUE ERNEST LAVISSE","RUE ETIENNE CHAILLON","RUE ETIENNE DE CONDILLAC","RUE ETIENNE DOLET","RUE ETIENNE JODELLE","RUE DE L ETOILE DU MATIN","RUE EUGENE CORNET","RUE EUGENE DAVIERS","RUE EUGENE DELACROIX","RUE DES FAUVETTES","RUE FERDINAND BUISSON","RUE FERNAND DE MAGELLAN","RUE FERNAND GASNIER","RUE FERNAND NOUVION","RUE FERNAND PELLOUTIER","RUE FIDELE SIMON","RUE DE LA FLORIDE","RUE FLORENCE NIGHTINGALE","RUE DE LA FORME JOUBERT","RUE DU FORT","RUE DU FOUR","RUE FRANCIS DE PRESSENSE","RUE FRANCOIS ARAGO","RUE FRANCOIS ADRIEN BOIELDIEU","RUE FRANCOIS DE CHATEAUBRIAND","RUE FRANCOIS COPPEE","RUE FRANCOIS MADIOT","RUE FRANCOIS MARCEAU","RUE FRANCOIS RABELAIS","RUE FRANCOIS RUDE","RUE FRANCOIS VILLON","RUE FRANCOIS VOLTAIRE","RUE DU DR FRANCOISE DOLTO","RUE FRANCOISE SAGAN","RUE FRANZ SCHUBERT","RUE FREDERIC ENGELS","RUE FREDERIC MISTRAL","RUE DES FRENES","RUE FRERES DE GONCOURT","RUE FRERES MONVOISIN","RUE DES FRERES PEREIRE","RUE GABRIEL FAURE","RUE GABRIELLE COLETTE","RUE GABRIEL PERI","RUE GABRIEL POULAIN","RUE GASPARD MONGE","RUE GASTON GUILLORE","RUE GASTON NASSIET","RUE GAULOISE","RUE DES GAUVIGNETS","RUE DU GEN MAURICE DE SARRAIL","RUE GEORGE SAND","RUE GEORGES BIZET","RUE GEORGES CLEMENCEAU","RUE GEORGES COURTELINE","RUE GEORGES CUVIER","RUE GEORGES DANTON","RUE GEORGES DE BUFFON","RUE GEORGES ESCOULAN","RUE GEORGES GUYNEMER","RUE GEORGES PAPAUD","RUE GEORGES THUAUD","RUE GEORGES VILLEBOIS MAREUIL","RUE GEORGES PHILIPPAR","RUE GIRARD DE LA CANTRIE","RUE DES GOELANDS","RUE GRAHAM BELL","RUE DU GRAND ORMEAU","RUE DU GUATEMALA","RUE GUILLAUME APOLLINAIRE","RUE GUSTAVE EIFFEL","RUE GUSTAVE FLAUBERT","RUE GUY DE MAUPASSANT","RUE DE LA GUYANE","RUE D HAITI","RUE DES HALLES","RUE HELENE BOUCHER","RUE HENRI BARBUSSE","RUE HENRI BRISSON","RUE HENRI DE MONFREID","RUE HENRI GAUTIER","RUE HENRI LE DEAN","RUE HENRI MATISSE","RUE HENRI SELLIER","RUE DES HETRES","RUE DES HIBISCUS","RUE DE L HIPPODROME","RUE HIPPOLYTE DURAND","RUE DU HONDURAS","RUE HONORE DAUMIER","RUE HONORE DE BALZAC","RUE HONORE DE MIRABEAU","RUE DES IFS","RUE DE L ILE DE FRANCE","RUE DE L ILE DE RETON","RUE DE L ILE DES ROCHELLES","RUE DE L ILE DU BIGNON","RUE DE L ILE DU LIN","RUE DE L ILE DU PE","RUE ISAAC NEWTON","RUE DE L ISAU","RUE JACQUES CARTIER","RUE JACQUES DAGUERRE","RUE JACQUES JOLLINIER","RUE JACQUES OFFENBACH","RUE JACQUES PREVERT","RUE DE LA JAMAIQUE","RUE JEAN BAPTISTE COLBERT","RUE JEAN BAPTISTE GREUZE","RUE JEAN BAPTISTE LAMARCK","RUE JEAN BAPTISTE MOLIERE","RUE JEAN BAPTISTE TREILHARD","RUE JEAN BART","RUE JEAN BAUDIN","RUE JEAN D ALEMBERT","RUE JEAN DAURAT","RUE JEAN DE LA BRUYERE","RUE JEAN DE LA FONTAINE","RUE JEAN GIRAUDOUX","RUE JEAN GUTENBERG","RUE JEAN HAAS","RUE JEAN HENRI DUNANT","RUE JEAN JACOTOT","RUE JEAN JAURES","RUE JEAN JOSEPH MOUNIER","RUE JEAN MACE","RUE JEAN MARAT","RUE JEAN MARIE PERRET","RUE JEANNE CHAUVIN","RUE JEANNE D ARC","RUE JEAN PHILIPPE RAMEAU","RUE JEAN PIERRE DUFREXOU","RUE JEAN RACINE","RUE JEAN RICHEPIN","RUE JEAN SYLVAIN BAILLY","RUE JEANNE BARRET","RUE JOACHIM DU BELLAY","RUE JOHN SCOTT","RUE JOSEPH BARNAVE","RUE JOSEPH BLANCHARD","RUE JOSEPH ET E. MONTGOLFIER","RUE JOSEPH JACQUART","RUE JOSEPH LAKANAL","RUE JOSEPH LE BRIX","RUE JOSEPH LE DELEZIR","RUE JULES BUSSON","RUE JULES FAVRE","RUE JULES FENELON","RUE JULES GUESDE","RUE JULES MANSART","RUE JULES MASSENET","RUE JULES MICHELET","RUE JULES RENARD","RUE JULES ROMAINS","RUE JULES SANDEAU","RUE JULES SIMON","RUE JULES VALLES","RUE JULES VERNE","RUE JULIE DAUBIE","RUE DE KERBRUN","RUE DU LAVOIR","RUE LAZARE HOCHE","RUE LEONARD DE VINCI","RUE LEON BOURGEOIS","RUE DU LERIOUX","RUE DE LA LOIRE","RUE LOUIS BLANC","RUE LOUIS BLANQUI","RUE LOUIS BLERIOT","RUE LOUIS BOURDALOUE","RUE LOUIS BRAILLE","RUE LOUIS BREGUET","RUE LOUIS CARRE","RUE LOUIS JOSEPH GAY LUSSAC","RUE LOUIS LUMIERE","RUE LOUIS NEEL","RUE LOUIS PASTEUR","RUE LOUIS SEGUIN","RUE LOUIS THENARD","RUE LOUISE MICHEL","RUE LOUISON BOBET","RUE LUC VAUVENARGUES","RUE MADAME DE SEVIGNE","RUE DU MAINE","RUE MARCEL CARNE","RUE MARCEL SEMBAT","RUE MARCELIN BERTHELOT","RUE DU MARCHE","RUE MARC SANGNIER","RUE MARIA VERONE","RUE MARGUERITE YOURCENAR","RUE MARIE JOSEPH MOLLE","RUE DES MARRONNIERS","RUE MARTIN LUTHER KING","RUE MARYSE BASTIE","RUE DE LA MATTE","RUE DE MAUDES","RUE MAURICE GARIN","RUE MAURICE MAUMENEE","RUE MAX JACOB","RUE MAXIMILIEN DE ROBESPIERRE","RUE DES MELEZES","RUE DU MENAUDOUX","RUE DU MENHIR","RUE DES MESANGES","RUE DE MEXICO","RUE MICHAEL FARADAY","RUE MICHEL ANGE","RUE MICHEL DE MONTAIGNE","RUE DES MOUETTES","RUE DU MOULIN DE LA NOE","RUE NEUVE","RUE DU NICARAGUA","RUE NICEPHORE NIEPCE","RUE NICOLAS BOILEAU","RUE NICOLAS COPERNIC","RUE DES NOISETIERS","RUE DE NORMANDIE","RUE DE NORMANDIE NIEMEN","RUE NUNGESSER ET COLI","RUE OLYMPE DE GOUGES","RUE DE L OREE DU VAL","RUE DES ORMES","RUE D OUESSANT","RUE DE LA PAIX","RUE DU PALAIS","RUE DU PARC A L EAU","RUE DU PAS NICOLAS","RUE PAUL BERT","RUE PAUL BROUSSE","RUE PAUL CLAUDEL","RUE PAUL GAUGUIN","RUE PAUL LANGEVIN","RUE PAUL VALERY","RUE PAUL VERLAINE","RUE PAUL-EMILE VICTOR","RUE DU PERTHUISCHAUD","RUE PETIT BRETON","RUE DE LA PETITE CALIFORNIE","RUE DE LA PETITE PATURE","RUE DES PEUPLIERS","RUE PHILIBERT DELORME","RUE PHILIPPE LEBON","RUE PIERRE BROSSOLETTE","RUE PIERRE CURIE","RUE PIERRE DE BAYARD","RUE PIERRE DE BEAUMARCHAIS","RUE PIERRE DE MARIVAUX","RUE PIERRE DE RONSARD","RUE PIERRE LAPLACE","RUE PIERRE LAROUSSE","RUE PIERRE LEPINE","RUE PIERRE LOTI","RUE PIERRE LOYGUE","RUE PIERRE MARIE JURET","RUE PIERRE MENDES FRANCE","RUE PIERRE VERGNIAUD","RUE PIERRE WALDECK ROUSSEAU","RUE DES PINSONS","RUE PITRE GRENAPIN","RUE DU PLESSIS","RUE DE PORNICHET","RUE DU PORT","RUE DU PRE DE CRAN","RUE DE PREZEGAT","RUE DU DOCTEUR ALBERT CALMETTE","RUE PROSPER TOUPIN","RUE RAOUL DUFY","RUE RENE CASSIN","RUE RENE DESCARTES","RUE RENE DUGUAY TROUIN","RUE RENE GUILLOUZO","RUE RENE GUY CADOU","RUE RENE HALLUARD","RUE RENE LESAGE","RUE RENE REAUMUR","RUE ROBERT SURCOUF","RUE ROGER SALENGRO","RUE ROLAND GARROS","RUE DE LA ROUILLARDERIE","RUE SADI LECOINTE","RUE DE SAILLE","RUE SAINTE BEUVE","RUE SAINT JUST","RUE DE SAINTONGE","RUE SAMUEL CHAMPLAIN","RUE DES SAPINS","RUE DES SAULES","RUE SEVERINE","RUE DU SOLEIL LEVANT","RUE DES SORBIERS","RUE DU STADE DE PORCE","RUE DE STALINGRAD","RUE STEPHANE MALLARME","RUE SUZANNE LACORE","RUE THEOPHILE LEFEBVRE","RUE THOMAS EDISON","RUE DES TILLEULS","RUE DES TISSERANDS","RUE DES TONNELIERS","RUE DE TOURAINE","RUE DE TOUTES AIDES","RUE DU TRAICT","RUE DE TRIGNAC","RUE DE LA TRINITE","RUE DES TROENES","RUE VASCO DE GAMA","RUE DE LA VECQUERIE","RUE DU VELODROME","RUE DES VENETES","RUE VICTOR MARRE","RUE DE LA VIEILLE EGLISE","RUE DES VIGNES DU CLOS","RUE DE LA VIGNE ROSEE","RUE DE LA VILLE AUX FEVES","RUE DE LA VILLE ETABLE","RUE DE LA VILLE HALLUARD","RUE DE VINCENNES","RUE VINCENT AURIOL","RUE DU 28 FEVRIER 1943","RUE DE LA VIREE DE LA CROIX","RUE VIVANT LACOUR","RUE WOLFGANG AMADEUS MOZART","RUE YOURI GAGARINE","RUE D YPRES"};
		for (int i =0; i<1000; i++){
			int indexNom = (int)Math.round(Math.random()*(listeNom.length-1));
			int indexPrenom = (int)Math.round(Math.random()*(listePrenom.length-1));
			int indexImage = (int)Math.round(Math.random()*3000);
			int indexRue =  (int)Math.round(Math.random()*(listeRue.length-1));
			int indexVille =  (int)Math.round(Math.random()*(nomsVilles.length-1));
			int indexJour = (int)Math.round(Math.random()*28+1);
			int indexMois = (int)Math.round(Math.random()*12+1);
			int indexAnnee = (int)Math.round(Math.random()*56);
			double indexProfil = Math.random();
			
			String login = listeNom[indexNom].toUpperCase().toLowerCase()+listePrenom[indexPrenom].toUpperCase().toLowerCase();
			String email = listePrenom[indexPrenom].toUpperCase().toLowerCase()+"."+listeNom[indexNom].toUpperCase().toLowerCase()+"@gmail.com";
			String mdp = "";
			for (int j =0; j<8; j++){
				int indexMdp = (int)Math.round(Math.random()*(listeCharMdp.length()-1));
				mdp += listeCharMdp.charAt(indexMdp);
			}
			Adresse adresse = new Adresse();
			adresse.setRue(listeRue[indexRue]);
			Ville ville = new Ville();
			ville.setCodePostal(cpVilles[indexVille]);
			ville.setNom(nomsVilles[indexVille]);
			ville.setId(villeService.rechercherIdVille(ville));
			
			adresse.setVille(ville);
			Date dateNaissance= new Date();
			dateNaissance.setYear(indexAnnee);
			dateNaissance.setMonth(indexMois);
			dateNaissance.setDate(indexJour);
			ProfilsUtilisateur profil1 = ProfilsUtilisateur.UTILISATEUR;
			ProfilsUtilisateur profil2 = ProfilsUtilisateur.ADMINISTRATEUR;
			ProfilsUtilisateur profil3 = ProfilsUtilisateur.GERANT;
			ArrayList<ProfilsUtilisateur> profils = new ArrayList<ProfilsUtilisateur>();
			profils.add(profil1);
			if (indexProfil > 0.8){
				profils.add(profil2);
				if (indexProfil > 0.95){
					profils.add(profil3);
				}
			}
			String photo = "http://www.hebus.com/image-"+indexImage+".html";
			Utilisateur toto = new Utilisateur();
			toto.setLogin(login);
			toto.setEmail(email);
			toto.setNom(listeNom[indexNom].toUpperCase().toLowerCase());
			toto.setPrenom(listePrenom[indexPrenom].toUpperCase().toLowerCase());
			toto.setMotDePasse(mdp);
			toto.setAdresse(adresse);
			toto.setDateDeNaissance(dateNaissance);
			toto.setProfils(profils);
			toto.setPhoto(photo);
			
			int ii = 1;
			while (utilisateursServices.rechercherParLogin(login)!=null){
				login += login+ii;
				toto.setLogin(login);
				ii++;
			}
			ii = 1;
			while (utilisateursServices.rechercherMail(email)){
				email = listePrenom[indexPrenom].toUpperCase().toLowerCase()+"."+listeNom[indexNom].toUpperCase().toLowerCase()+ii+"@gmail.com";
				toto.setEmail(email);
				ii++;
			}
			toto = utilisateursServices.creer(toto);
			
			Trace trace = new Trace();
			trace.setAction("Création utilisateur");
			trace.setDate(new Date());
			trace.setEmail(toto.getEmail());
			trace.setResultat("SUCCESS");
			traceServices.creer(trace);
		}
	}
	
	

}
