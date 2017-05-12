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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.main");
        anywheresoftware.b4a.keywords.Common.ToastMessageShow("This application was developed with B4A trial version and should not be distributed.", true);
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (main) Resume **");
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
public static int _intuser = 0;
public static anywheresoftware.b4a.sql.SQL _sql1 = null;
public static anywheresoftware.b4a.sql.SQL.CursorWrapper _cursor1 = null;
public static String _dbfilename = "";
public static String _dbfiledir = "";
public static String _dbtableequipos = "";
public static String _dbtablemijugador = "";
public static String _dbtablepartidos = "";
public static String _dbtableusuarios = "";
public static int _tipomenuestad = 0;
public anywheresoftware.b4a.objects.ButtonWrapper _btnanonimo = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnidentificar = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtpwd = null;
public anywheresoftware.b4a.objects.EditTextWrapper _txtuser = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imgsalir = null;
public b4a.example.marcador _marcador = null;
public b4a.example.estpartidos _estpartidos = null;
public b4a.example.menuestad _menuestad = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (marcador.mostCurrent != null);
vis = vis | (estpartidos.mostCurrent != null);
vis = vis | (menuestad.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 50;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 52;BA.debugLine="Activity.LoadLayout(\"Main\")";
mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 54;BA.debugLine="Msgbox(\"Bienvenido a Al-Juarismi!\", \"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Bienvenido a Al-Juarismi!"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 //BA.debugLineNum = 55;BA.debugLine="intUser=0";
_intuser = (int) (0);
 //BA.debugLineNum = 56;BA.debugLine="If File.Exists(File.DirInternal,DBFileName) = Fal";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_dbfilename)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 57;BA.debugLine="File.Copy(File.DirAssets,DBFileName,File.DirInte";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),_dbfilename,anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_dbfilename);
 //BA.debugLineNum = 58;BA.debugLine="Msgbox(\"base de datos no encontrada\" , \"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("base de datos no encontrada"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 61;BA.debugLine="If SQL1.IsInitialized = False Then";
if (_sql1.IsInitialized()==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 62;BA.debugLine="SQL1.Initialize(File.DirInternal, DBFileName, Fa";
_sql1.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternal(),_dbfilename,anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 71;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 73;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 67;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _btnanonimo_click() throws Exception{
 //BA.debugLineNum = 95;BA.debugLine="Sub btnAnonimo_Click";
 //BA.debugLineNum = 96;BA.debugLine="StartActivity(Marcador)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._marcador.getObject()));
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
public static String  _btnidentificar_click() throws Exception{
int _intencontrado = 0;
String _strsql = "";
int _i = 0;
 //BA.debugLineNum = 76;BA.debugLine="Sub btnIdentificar_Click";
 //BA.debugLineNum = 77;BA.debugLine="Dim intEncontrado As Int = 0";
_intencontrado = (int) (0);
 //BA.debugLineNum = 78;BA.debugLine="Dim strSQL As String = \"SELECT * FROM Usuarios wh";
_strsql = "SELECT * FROM Usuarios where User = '"+mostCurrent._txtuser.getText()+"' and Pwd = '"+mostCurrent._txtpwd.getText()+"'";
 //BA.debugLineNum = 80;BA.debugLine="cursor1 = SQL1.ExecQuery(strSQL)";
_cursor1.setObject((android.database.Cursor)(_sql1.ExecQuery(_strsql)));
 //BA.debugLineNum = 82;BA.debugLine="For i = 0 To cursor1.RowCount - 1";
{
final int step4 = 1;
final int limit4 = (int) (_cursor1.getRowCount()-1);
for (_i = (int) (0) ; (step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4); _i = ((int)(0 + _i + step4)) ) {
 //BA.debugLineNum = 83;BA.debugLine="cursor1.Position = i";
_cursor1.setPosition(_i);
 //BA.debugLineNum = 84;BA.debugLine="intUser= cursor1.GetInt(\"idUser\")";
_intuser = _cursor1.GetInt("idUser");
 //BA.debugLineNum = 85;BA.debugLine="intEncontrado=1";
_intencontrado = (int) (1);
 }
};
 //BA.debugLineNum = 87;BA.debugLine="If intEncontrado=1 Then";
if (_intencontrado==1) { 
 //BA.debugLineNum = 88;BA.debugLine="StartActivity(Marcador)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._marcador.getObject()));
 }else {
 //BA.debugLineNum = 91;BA.debugLine="Msgbox(\"El usuario/contraseña son erróneos\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("El usuario/contraseña son erróneos"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 35;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 39;BA.debugLine="Private btnAnonimo As Button";
mostCurrent._btnanonimo = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private btnIdentificar As Button";
mostCurrent._btnidentificar = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private txtPwd As EditText";
mostCurrent._txtpwd = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private txtUser As EditText";
mostCurrent._txtuser = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 46;BA.debugLine="Private imgSalir As ImageView";
mostCurrent._imgsalir = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 48;BA.debugLine="End Sub";
return "";
}
public static String  _imgsalir_click() throws Exception{
 //BA.debugLineNum = 99;BA.debugLine="Sub imgSalir_Click";
 //BA.debugLineNum = 100;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
marcador._process_globals();
estpartidos._process_globals();
menuestad._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Public intUser As Int";
_intuser = 0;
 //BA.debugLineNum = 20;BA.debugLine="Public SQL1 As SQL";
_sql1 = new anywheresoftware.b4a.sql.SQL();
 //BA.debugLineNum = 21;BA.debugLine="Public cursor1 As Cursor";
_cursor1 = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 23;BA.debugLine="Public DBFileName As String					: DBFileName = \"m";
_dbfilename = "";
 //BA.debugLineNum = 23;BA.debugLine="Public DBFileName As String					: DBFileName = \"m";
_dbfilename = "marcador.db";
 //BA.debugLineNum = 24;BA.debugLine="Public DBFileDir As String					: DBFileDir = Fil";
_dbfiledir = "";
 //BA.debugLineNum = 24;BA.debugLine="Public DBFileDir As String					: DBFileDir = Fil";
_dbfiledir = anywheresoftware.b4a.keywords.Common.File.getDirInternal();
 //BA.debugLineNum = 26;BA.debugLine="Public DBTableEquipos As String				: DBTableEquip";
_dbtableequipos = "";
 //BA.debugLineNum = 26;BA.debugLine="Public DBTableEquipos As String				: DBTableEquip";
_dbtableequipos = "Equipos";
 //BA.debugLineNum = 27;BA.debugLine="Public DBTableMiJugador As String			: DBTableMiJu";
_dbtablemijugador = "";
 //BA.debugLineNum = 27;BA.debugLine="Public DBTableMiJugador As String			: DBTableMiJu";
_dbtablemijugador = "MiJugador";
 //BA.debugLineNum = 28;BA.debugLine="Public DBTablePartidos As String			: DBTableMiJug";
_dbtablepartidos = "";
 //BA.debugLineNum = 28;BA.debugLine="Public DBTablePartidos As String			: DBTableMiJug";
_dbtablemijugador = "Partidos";
 //BA.debugLineNum = 29;BA.debugLine="Public DBTableUsuarios As String			: DBTableUsuar";
_dbtableusuarios = "";
 //BA.debugLineNum = 29;BA.debugLine="Public DBTableUsuarios As String			: DBTableUsuar";
_dbtableusuarios = "Usuarios";
 //BA.debugLineNum = 31;BA.debugLine="Public TipoMenuEstad As Int ' 0 - partidos, 1 - m";
_tipomenuestad = 0;
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return "";
}
}
