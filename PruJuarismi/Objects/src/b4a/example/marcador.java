package b4a.example;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class marcador extends Activity implements B4AActivity{
	public static marcador mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.marcador");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (marcador).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.marcador");
        anywheresoftware.b4a.keywords.Common.ToastMessageShow("This application was developed with B4A trial version and should not be distributed.", true);
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.marcador", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (marcador) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (marcador) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return marcador.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (marcador) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (marcador) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static int _idlocal = 0;
public static String _strlocal = "";
public static int _idvis = 0;
public static String _strvis = "";
public static String _dbfilename = "";
public static String _dbfiledir = "";
public static int _contadorlocal = 0;
public static int _contadorvisitante = 0;
public static int _contadormj = 0;
public static int _personalesmj = 0;
public anywheresoftware.b4a.objects.LabelWrapper _lbllocpuntos = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblvispuntos = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblowner = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblmjpuntos = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgestadistica = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnpermj1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnpermjm1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpermj = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtequipoloc = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtequipovis = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnfinpartido = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgsalir = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _splocal = null;
public anywheresoftware.b4a.objects.SpinnerWrapper _spvis = null;
public b4a.example.main _main = null;
public b4a.example.estpartidos _estpartidos = null;
public b4a.example.menuestad _menuestad = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 45;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 47;BA.debugLine="Activity.LoadLayout(\"Marcador\")";
mostCurrent._activity.LoadLayout("Marcador",mostCurrent.activityBA);
 //BA.debugLineNum = 48;BA.debugLine="New";
_new();
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 64;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 60;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public static String  _btnborrar_click() throws Exception{
 //BA.debugLineNum = 174;BA.debugLine="Sub btnBorrar_Click";
 //BA.debugLineNum = 175;BA.debugLine="contadorLocal=0";
_contadorlocal = (int) (0);
 //BA.debugLineNum = 176;BA.debugLine="contadorVisitante=0";
_contadorvisitante = (int) (0);
 //BA.debugLineNum = 177;BA.debugLine="contadorMJ=0";
_contadormj = (int) (0);
 //BA.debugLineNum = 178;BA.debugLine="muestraPuntosLocal";
_muestrapuntoslocal();
 //BA.debugLineNum = 179;BA.debugLine="muestraPuntosVis";
_muestrapuntosvis();
 //BA.debugLineNum = 180;BA.debugLine="muestraPuntosMJ";
_muestrapuntosmj();
 //BA.debugLineNum = 181;BA.debugLine="End Sub";
return "";
}
public static String  _btnfinpartido_click() throws Exception{
String _strsql = "";
 //BA.debugLineNum = 186;BA.debugLine="Sub btnFinPartido_Click";
 //BA.debugLineNum = 187;BA.debugLine="Dim strSQL As String";
_strsql = "";
 //BA.debugLineNum = 188;BA.debugLine="strSQL = \"INSERT INTO PARTIDOS (IDUSER, FECPARTID";
_strsql = "INSERT INTO PARTIDOS (IDUSER, FECPARTIDO, EQUIPOLOC, EQUIPOVIS, RESLOC, RESVIS) VALUES("+BA.NumberToString(mostCurrent._main._intuser)+",'"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+_strlocal+"','"+_strvis+"',"+BA.NumberToString(_contadorlocal)+","+BA.NumberToString(_contadorvisitante)+")";
 //BA.debugLineNum = 190;BA.debugLine="Main.SQL1.ExecNonQuery(strSQL)";
mostCurrent._main._sql1.ExecNonQuery(_strsql);
 //BA.debugLineNum = 192;BA.debugLine="strSQL = \"INSERT INTO MIJUGADOR (IDUSER, FECPARTI";
_strsql = "INSERT INTO MIJUGADOR (IDUSER, FECPARTIDO, EQUIPOLOC, EQUIPOVIS, PUNTOS, PERSONALES) VALUES("+BA.NumberToString(mostCurrent._main._intuser)+",'"+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())+"','"+_strlocal+"','"+_strvis+"',"+BA.NumberToString(_contadormj)+","+BA.NumberToString(_personalesmj)+")";
 //BA.debugLineNum = 194;BA.debugLine="Main.SQL1.ExecNonQuery(strSQL)";
mostCurrent._main._sql1.ExecNonQuery(_strsql);
 //BA.debugLineNum = 196;BA.debugLine="Msgbox(\"Resultados guardados\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Resultados guardados"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 198;BA.debugLine="End Sub";
return "";
}
public static String  _btnloc1_click() throws Exception{
 //BA.debugLineNum = 159;BA.debugLine="Sub btnLoc1_click";
 //BA.debugLineNum = 160;BA.debugLine="contadorLocal=contadorLocal+1";
_contadorlocal = (int) (_contadorlocal+1);
 //BA.debugLineNum = 161;BA.debugLine="muestraPuntosLocal";
_muestrapuntoslocal();
 //BA.debugLineNum = 162;BA.debugLine="End Sub";
return "";
}
public static String  _btnlocm1_click() throws Exception{
 //BA.debugLineNum = 154;BA.debugLine="Sub btnLocm1_Click";
 //BA.debugLineNum = 155;BA.debugLine="contadorLocal=contadorLocal-1";
_contadorlocal = (int) (_contadorlocal-1);
 //BA.debugLineNum = 156;BA.debugLine="muestraPuntosLocal";
_muestrapuntoslocal();
 //BA.debugLineNum = 157;BA.debugLine="End Sub";
return "";
}
public static String  _btnmjp1_click() throws Exception{
 //BA.debugLineNum = 120;BA.debugLine="Sub btnMJP1_Click";
 //BA.debugLineNum = 121;BA.debugLine="contadorMJ=contadorMJ+1";
_contadormj = (int) (_contadormj+1);
 //BA.debugLineNum = 122;BA.debugLine="muestraPuntosMJ";
_muestrapuntosmj();
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _btnmjpm1_click() throws Exception{
 //BA.debugLineNum = 115;BA.debugLine="Sub btnMJPm1_Click";
 //BA.debugLineNum = 116;BA.debugLine="contadorMJ=contadorMJ-1";
_contadormj = (int) (_contadormj-1);
 //BA.debugLineNum = 117;BA.debugLine="muestraPuntosMJ";
_muestrapuntosmj();
 //BA.debugLineNum = 118;BA.debugLine="End Sub";
return "";
}
public static String  _btnpermj1_click() throws Exception{
 //BA.debugLineNum = 139;BA.debugLine="Sub btnPerMJ1_Click";
 //BA.debugLineNum = 140;BA.debugLine="personalesMJ=personalesMJ+1";
_personalesmj = (int) (_personalesmj+1);
 //BA.debugLineNum = 141;BA.debugLine="muestraPersonalesMJ";
_muestrapersonalesmj();
 //BA.debugLineNum = 142;BA.debugLine="End Sub";
return "";
}
public static String  _btnpermjm1_click() throws Exception{
 //BA.debugLineNum = 134;BA.debugLine="Sub btnPerMJm1_Click";
 //BA.debugLineNum = 135;BA.debugLine="personalesMJ=personalesMJ-1";
_personalesmj = (int) (_personalesmj-1);
 //BA.debugLineNum = 136;BA.debugLine="muestraPersonalesMJ";
_muestrapersonalesmj();
 //BA.debugLineNum = 137;BA.debugLine="End Sub";
return "";
}
public static String  _btnvis1_click() throws Exception{
 //BA.debugLineNum = 149;BA.debugLine="Sub btnVis1_Click";
 //BA.debugLineNum = 150;BA.debugLine="contadorVisitante=contadorVisitante+1";
_contadorvisitante = (int) (_contadorvisitante+1);
 //BA.debugLineNum = 151;BA.debugLine="muestraPuntosVis";
_muestrapuntosvis();
 //BA.debugLineNum = 152;BA.debugLine="End Sub";
return "";
}
public static String  _btnvism1_click() throws Exception{
 //BA.debugLineNum = 144;BA.debugLine="Sub btnVism1_Click";
 //BA.debugLineNum = 145;BA.debugLine="contadorVisitante=contadorVisitante-1";
_contadorvisitante = (int) (_contadorvisitante-1);
 //BA.debugLineNum = 146;BA.debugLine="muestraPuntosVis";
_muestrapuntosvis();
 //BA.debugLineNum = 147;BA.debugLine="End Sub";
return "";
}
public static String  _cargasplocal() throws Exception{
String _strsql = "";
int _i = 0;
 //BA.debugLineNum = 209;BA.debugLine="Sub cargaSpLocal";
 //BA.debugLineNum = 211;BA.debugLine="Dim strSQL As String = \"SELECT equipo FROM \" & Ma";
_strsql = "SELECT equipo FROM "+mostCurrent._main._dbtableequipos;
 //BA.debugLineNum = 213;BA.debugLine="Main.cursor1 = Main.SQL1.ExecQuery(strSQL)";
mostCurrent._main._cursor1.setObject((android.database.Cursor)(mostCurrent._main._sql1.ExecQuery(_strsql)));
 //BA.debugLineNum = 215;BA.debugLine="For i = 0 To Main.cursor1.RowCount - 1";
{
final int step3 = 1;
final int limit3 = (int) (mostCurrent._main._cursor1.getRowCount()-1);
for (_i = (int) (0) ; (step3 > 0 && _i <= limit3) || (step3 < 0 && _i >= limit3); _i = ((int)(0 + _i + step3)) ) {
 //BA.debugLineNum = 216;BA.debugLine="Main.cursor1.Position = i";
mostCurrent._main._cursor1.setPosition(_i);
 //BA.debugLineNum = 218;BA.debugLine="spLocal.Add(Main.cursor1.GetString(\"Equipo\"))";
mostCurrent._splocal.Add(mostCurrent._main._cursor1.GetString("Equipo"));
 //BA.debugLineNum = 219;BA.debugLine="spVis.Add(Main.cursor1.GetString(\"Equipo\"))";
mostCurrent._spvis.Add(mostCurrent._main._cursor1.GetString("Equipo"));
 }
};
 //BA.debugLineNum = 222;BA.debugLine="spLocal.Prompt=\"Local\"";
mostCurrent._splocal.setPrompt(BA.ObjectToCharSequence("Local"));
 //BA.debugLineNum = 223;BA.debugLine="spVis.Prompt=\"Visitante\"";
mostCurrent._spvis.setPrompt(BA.ObjectToCharSequence("Visitante"));
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 18;BA.debugLine="Public DBFileName As String				: DBFileName = \"ma";
mostCurrent._dbfilename = "";
 //BA.debugLineNum = 18;BA.debugLine="Public DBFileName As String				: DBFileName = \"ma";
mostCurrent._dbfilename = "marcador.db";
 //BA.debugLineNum = 20;BA.debugLine="Public DBFileDir As String			'		: DBFileDir = Fil";
mostCurrent._dbfiledir = "";
 //BA.debugLineNum = 23;BA.debugLine="Private contadorLocal As Int";
_contadorlocal = 0;
 //BA.debugLineNum = 24;BA.debugLine="Private contadorVisitante As Int";
_contadorvisitante = 0;
 //BA.debugLineNum = 25;BA.debugLine="Private contadorMJ As Int";
_contadormj = 0;
 //BA.debugLineNum = 26;BA.debugLine="Private personalesMJ As Int";
_personalesmj = 0;
 //BA.debugLineNum = 28;BA.debugLine="Private lblLocPuntos As Label";
mostCurrent._lbllocpuntos = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private lblVisPuntos As Label";
mostCurrent._lblvispuntos = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private LblOwner As Label";
mostCurrent._lblowner = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private lblMJPuntos As Label";
mostCurrent._lblmjpuntos = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private imgEstadistica As ImageView";
mostCurrent._imgestadistica = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private btnPerMJ1 As Button";
mostCurrent._btnpermj1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private btnPerMJm1 As Button";
mostCurrent._btnpermjm1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private lblPerMJ As Label";
mostCurrent._lblpermj = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Public txtEquipoLoc As EditText";
mostCurrent._txtequipoloc = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Public txtEquipoVis As EditText";
mostCurrent._txtequipovis = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private btnFinPartido As Button";
mostCurrent._btnfinpartido = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private imgSalir As ImageView";
mostCurrent._imgsalir = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private spLocal As Spinner";
mostCurrent._splocal = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private spVis As Spinner";
mostCurrent._spvis = new anywheresoftware.b4a.objects.SpinnerWrapper();
 //BA.debugLineNum = 43;BA.debugLine="End Sub";
return "";
}
public static String  _imgestadistica_click() throws Exception{
 //BA.debugLineNum = 125;BA.debugLine="Sub imgEstadistica_Click";
 //BA.debugLineNum = 126;BA.debugLine="StartActivity(MenuEstad)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._menuestad.getObject()));
 //BA.debugLineNum = 127;BA.debugLine="End Sub";
return "";
}
public static String  _imgsalir_click() throws Exception{
 //BA.debugLineNum = 202;BA.debugLine="Sub imgSalir_Click";
 //BA.debugLineNum = 203;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 204;BA.debugLine="End Sub";
return "";
}
public static String  _lbllocpuntos_click() throws Exception{
 //BA.debugLineNum = 169;BA.debugLine="Sub lblLocPuntos_Click";
 //BA.debugLineNum = 170;BA.debugLine="contadorLocal=contadorLocal+2";
_contadorlocal = (int) (_contadorlocal+2);
 //BA.debugLineNum = 171;BA.debugLine="muestraPuntosLocal";
_muestrapuntoslocal();
 //BA.debugLineNum = 172;BA.debugLine="End Sub";
return "";
}
public static String  _lblmjpuntos_click() throws Exception{
 //BA.debugLineNum = 110;BA.debugLine="Sub lblMJPuntos_Click";
 //BA.debugLineNum = 111;BA.debugLine="contadorMJ=contadorMJ+2";
_contadormj = (int) (_contadormj+2);
 //BA.debugLineNum = 112;BA.debugLine="muestraPuntosMJ";
_muestrapuntosmj();
 //BA.debugLineNum = 113;BA.debugLine="End Sub";
return "";
}
public static String  _lblpermj_click() throws Exception{
 //BA.debugLineNum = 129;BA.debugLine="Sub lblPerMJ_Click";
 //BA.debugLineNum = 130;BA.debugLine="personalesMJ=personalesMJ+1";
_personalesmj = (int) (_personalesmj+1);
 //BA.debugLineNum = 131;BA.debugLine="muestraPersonalesMJ";
_muestrapersonalesmj();
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}
public static String  _lblvispuntos_click() throws Exception{
 //BA.debugLineNum = 164;BA.debugLine="Sub lblVisPuntos_Click";
 //BA.debugLineNum = 165;BA.debugLine="contadorVisitante=contadorVisitante+2";
_contadorvisitante = (int) (_contadorvisitante+2);
 //BA.debugLineNum = 166;BA.debugLine="muestraPuntosVis";
_muestrapuntosvis();
 //BA.debugLineNum = 167;BA.debugLine="End Sub";
return "";
}
public static String  _muestrapersonalesmj() throws Exception{
 //BA.debugLineNum = 98;BA.debugLine="Sub muestraPersonalesMJ";
 //BA.debugLineNum = 99;BA.debugLine="If personalesMJ>5 Then";
if (_personalesmj>5) { 
 //BA.debugLineNum = 100;BA.debugLine="Msgbox(\"No se pueden tener más de 5 faltas perso";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("No se pueden tener más de 5 faltas personales"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 101;BA.debugLine="personalesMJ=5";
_personalesmj = (int) (5);
 };
 //BA.debugLineNum = 103;BA.debugLine="lblPerMJ.Text=personalesMJ";
mostCurrent._lblpermj.setText(BA.ObjectToCharSequence(_personalesmj));
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return "";
}
public static String  _muestrapuntoslocal() throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub muestraPuntosLocal";
 //BA.debugLineNum = 87;BA.debugLine="lblLocPuntos.Text=contadorLocal";
mostCurrent._lbllocpuntos.setText(BA.ObjectToCharSequence(_contadorlocal));
 //BA.debugLineNum = 88;BA.debugLine="End Sub";
return "";
}
public static String  _muestrapuntosmj() throws Exception{
 //BA.debugLineNum = 94;BA.debugLine="Sub muestraPuntosMJ";
 //BA.debugLineNum = 95;BA.debugLine="lblMJPuntos.text=contadorMJ";
mostCurrent._lblmjpuntos.setText(BA.ObjectToCharSequence(_contadormj));
 //BA.debugLineNum = 96;BA.debugLine="End Sub";
return "";
}
public static String  _muestrapuntosvis() throws Exception{
 //BA.debugLineNum = 90;BA.debugLine="Sub muestraPuntosVis";
 //BA.debugLineNum = 91;BA.debugLine="lblVisPuntos.Text=contadorVisitante";
mostCurrent._lblvispuntos.setText(BA.ObjectToCharSequence(_contadorvisitante));
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _new() throws Exception{
 //BA.debugLineNum = 68;BA.debugLine="Sub New";
 //BA.debugLineNum = 69;BA.debugLine="contadorLocal=0";
_contadorlocal = (int) (0);
 //BA.debugLineNum = 70;BA.debugLine="lblLocPuntos.Text=contadorLocal";
mostCurrent._lbllocpuntos.setText(BA.ObjectToCharSequence(_contadorlocal));
 //BA.debugLineNum = 73;BA.debugLine="contadorVisitante=0";
_contadorvisitante = (int) (0);
 //BA.debugLineNum = 74;BA.debugLine="lblVisPuntos.Text=contadorVisitante";
mostCurrent._lblvispuntos.setText(BA.ObjectToCharSequence(_contadorvisitante));
 //BA.debugLineNum = 75;BA.debugLine="LblOwner.Text=\"Fecha del partido: \" &  DateTime.D";
mostCurrent._lblowner.setText(BA.ObjectToCharSequence("Fecha del partido: "+anywheresoftware.b4a.keywords.Common.DateTime.Date(anywheresoftware.b4a.keywords.Common.DateTime.getNow())));
 //BA.debugLineNum = 77;BA.debugLine="If Main.intUser=0 Then";
if (mostCurrent._main._intuser==0) { 
 //BA.debugLineNum = 78;BA.debugLine="imgEstadistica.Enabled= False";
mostCurrent._imgestadistica.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 79;BA.debugLine="btnFinPartido.Enabled=False";
mostCurrent._btnfinpartido.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 82;BA.debugLine="cargaSpLocal";
_cargasplocal();
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 7;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 8;BA.debugLine="Public idLocal As Int";
_idlocal = 0;
 //BA.debugLineNum = 9;BA.debugLine="Public strLocal As String";
_strlocal = "";
 //BA.debugLineNum = 10;BA.debugLine="Public idVis As Int";
_idvis = 0;
 //BA.debugLineNum = 11;BA.debugLine="Public strVis As String";
_strvis = "";
 //BA.debugLineNum = 14;BA.debugLine="End Sub";
return "";
}
public static String  _splocal_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub spLocal_ItemClick (Position As Int, Value As O";
 //BA.debugLineNum = 227;BA.debugLine="strLocal=spLocal.SelectedItem";
_strlocal = mostCurrent._splocal.getSelectedItem();
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
return "";
}
public static String  _spvis_itemclick(int _position,Object _value) throws Exception{
 //BA.debugLineNum = 230;BA.debugLine="Sub spVis_ItemClick (Position As Int, Value As Obj";
 //BA.debugLineNum = 231;BA.debugLine="strVis=spVis.SelectedItem";
_strvis = mostCurrent._spvis.getSelectedItem();
 //BA.debugLineNum = 232;BA.debugLine="End Sub";
return "";
}
}
