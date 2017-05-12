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

public class estpartidos extends Activity implements B4AActivity{
	public static estpartidos mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.estpartidos");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (estpartidos).");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.estpartidos");
        anywheresoftware.b4a.keywords.Common.ToastMessageShow("This application was developed with B4A trial version and should not be distributed.", true);
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.estpartidos", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (estpartidos) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (estpartidos) Resume **");
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
		return estpartidos.class;
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
        BA.LogInfo("** Activity (estpartidos) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (estpartidos) Resume **");
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
public anywheresoftware.b4a.objects.WebViewWrapper _wbvtable = null;
public static String _htmlcss = "";
public anywheresoftware.b4a.objects.ImageViewWrapper _imgsalir = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public b4a.example.main _main = null;
public b4a.example.marcador _marcador = null;
public b4a.example.menuestad _menuestad = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 40;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 42;BA.debugLine="Activity.LoadLayout(\"EstPartidos\")";
mostCurrent._activity.LoadLayout("EstPartidos",mostCurrent.activityBA);
 //BA.debugLineNum = 44;BA.debugLine="If Main.TipoMenuEstad=0 Then";
if (mostCurrent._main._tipomenuestad==0) { 
 //BA.debugLineNum = 45;BA.debugLine="Label1.Text= \"Resultados de los partidos\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("Resultados de los partidos"));
 }else {
 //BA.debugLineNum = 47;BA.debugLine="Label1.Text= \"Resultados de Mi Jugador\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("Resultados de Mi Jugador"));
 };
 //BA.debugLineNum = 51;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 };
 //BA.debugLineNum = 54;BA.debugLine="Main.SQL1.Initialize(Main.DBFileDir, Main.DBFileN";
mostCurrent._main._sql1.Initialize(mostCurrent._main._dbfiledir,mostCurrent._main._dbfilename,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 58;BA.debugLine="wbvTable.Initialize(\"wbvTable\")";
mostCurrent._wbvtable.Initialize(mostCurrent.activityBA,"wbvTable");
 //BA.debugLineNum = 59;BA.debugLine="Activity.AddView(wbvTable, 0, 30%y, 100%x, 70%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._wbvtable.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (70),mostCurrent.activityBA));
 //BA.debugLineNum = 61;BA.debugLine="ShowTableInWebView";
_showtableinwebview();
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 68;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 64;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return "";
}
public static String  _cmdvolver_click() throws Exception{
 //BA.debugLineNum = 100;BA.debugLine="Sub cmdVolver_Click";
 //BA.debugLineNum = 101;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 102;BA.debugLine="End Sub";
return "";
}
public static String  _executehtml(anywheresoftware.b4a.sql.SQL _sql,String _query,String[] _stringargs,int _limit,boolean _clickable) throws Exception{
anywheresoftware.b4a.sql.SQL.CursorWrapper _cur = null;
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb = null;
int _row = 0;
int _i = 0;
 //BA.debugLineNum = 106;BA.debugLine="Sub ExecuteHtml(SQL As SQL, Query As String, Strin";
 //BA.debugLineNum = 108;BA.debugLine="Dim cur As Cursor";
_cur = new anywheresoftware.b4a.sql.SQL.CursorWrapper();
 //BA.debugLineNum = 109;BA.debugLine="If StringArgs <> Null Then";
if (_stringargs!= null) { 
 //BA.debugLineNum = 110;BA.debugLine="cur = SQL.ExecQuery2(Query, StringArgs)";
_cur.setObject((android.database.Cursor)(_sql.ExecQuery2(_query,_stringargs)));
 }else {
 //BA.debugLineNum = 112;BA.debugLine="cur = SQL.ExecQuery(Query)";
_cur.setObject((android.database.Cursor)(_sql.ExecQuery(_query)));
 };
 //BA.debugLineNum = 114;BA.debugLine="Log(\"ExecuteHtml: \" & Query)";
anywheresoftware.b4a.keywords.Common.Log("ExecuteHtml: "+_query);
 //BA.debugLineNum = 115;BA.debugLine="If Limit > 0 Then Limit = Min(Limit, cur.RowCount";
if (_limit>0) { 
_limit = (int) (anywheresoftware.b4a.keywords.Common.Min(_limit,_cur.getRowCount()));}
else {
_limit = _cur.getRowCount();};
 //BA.debugLineNum = 116;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 117;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 118;BA.debugLine="sb.Append(\"<html><body>\").Append(CRLF)";
_sb.Append("<html><body>").Append(anywheresoftware.b4a.keywords.Common.CRLF);
 //BA.debugLineNum = 119;BA.debugLine="sb.Append(\"<style type='text/css'>\").Append(HtmlC";
_sb.Append("<style type='text/css'>").Append(mostCurrent._htmlcss).Append("</style>").Append(anywheresoftware.b4a.keywords.Common.CRLF);
 //BA.debugLineNum = 120;BA.debugLine="sb.Append(\"<table><tr>\").Append(CRLF)";
_sb.Append("<table><tr>").Append(anywheresoftware.b4a.keywords.Common.CRLF);
 //BA.debugLineNum = 122;BA.debugLine="If Main.TipoMenuEstad=0 Then";
if (mostCurrent._main._tipomenuestad==0) { 
 //BA.debugLineNum = 123;BA.debugLine="sb.Append(\"<th>\").Append(\"Fecha\").Append(\"</th>\"";
_sb.Append("<th>").Append("Fecha").Append("</th>");
 //BA.debugLineNum = 124;BA.debugLine="sb.Append(\"<th>\").Append(\"Local\").Append(\"</th>\"";
_sb.Append("<th>").Append("Local").Append("</th>");
 //BA.debugLineNum = 125;BA.debugLine="sb.Append(\"<th>\").Append(\"Puntos\").Append(\"</th>";
_sb.Append("<th>").Append("Puntos").Append("</th>");
 //BA.debugLineNum = 126;BA.debugLine="sb.Append(\"<th>\").Append(\"Visitante\").Append(\"</";
_sb.Append("<th>").Append("Visitante").Append("</th>");
 //BA.debugLineNum = 127;BA.debugLine="sb.Append(\"<th>\").Append(\"Puntos\").Append(\"</th>";
_sb.Append("<th>").Append("Puntos").Append("</th>");
 }else {
 //BA.debugLineNum = 129;BA.debugLine="sb.Append(\"<th>\").Append(\"Fecha\").Append(\"</th>\"";
_sb.Append("<th>").Append("Fecha").Append("</th>");
 //BA.debugLineNum = 130;BA.debugLine="sb.Append(\"<th>\").Append(\"Local\").Append(\"</th>\"";
_sb.Append("<th>").Append("Local").Append("</th>");
 //BA.debugLineNum = 131;BA.debugLine="sb.Append(\"<th>\").Append(\"Visitante\").Append(\"</";
_sb.Append("<th>").Append("Visitante").Append("</th>");
 //BA.debugLineNum = 132;BA.debugLine="sb.Append(\"<th>\").Append(\"Puntos\").Append(\"</th>";
_sb.Append("<th>").Append("Puntos").Append("</th>");
 //BA.debugLineNum = 133;BA.debugLine="sb.Append(\"<th>\").Append(\"Personales\").Append(\"<";
_sb.Append("<th>").Append("Personales").Append("</th>");
 };
 //BA.debugLineNum = 136;BA.debugLine="sb.Append(\"</tr>\").Append(CRLF)";
_sb.Append("</tr>").Append(anywheresoftware.b4a.keywords.Common.CRLF);
 //BA.debugLineNum = 137;BA.debugLine="For row = 0 To Limit - 1";
{
final int step28 = 1;
final int limit28 = (int) (_limit-1);
for (_row = (int) (0) ; (step28 > 0 && _row <= limit28) || (step28 < 0 && _row >= limit28); _row = ((int)(0 + _row + step28)) ) {
 //BA.debugLineNum = 138;BA.debugLine="cur.Position = row";
_cur.setPosition(_row);
 //BA.debugLineNum = 139;BA.debugLine="If row Mod 2 = 0 Then";
if (_row%2==0) { 
 //BA.debugLineNum = 140;BA.debugLine="sb.Append(\"<tr>\")";
_sb.Append("<tr>");
 }else {
 //BA.debugLineNum = 142;BA.debugLine="sb.Append(\"<tr class='odd'>\")";
_sb.Append("<tr class='odd'>");
 };
 //BA.debugLineNum = 144;BA.debugLine="For i = 0 To cur.ColumnCount - 1";
{
final int step35 = 1;
final int limit35 = (int) (_cur.getColumnCount()-1);
for (_i = (int) (0) ; (step35 > 0 && _i <= limit35) || (step35 < 0 && _i >= limit35); _i = ((int)(0 + _i + step35)) ) {
 //BA.debugLineNum = 145;BA.debugLine="sb.Append(\"<td>\")";
_sb.Append("<td>");
 //BA.debugLineNum = 146;BA.debugLine="If Clickable Then";
if (_clickable) { 
 //BA.debugLineNum = 147;BA.debugLine="sb.Append(\"<a href='http://\").Append(i).Append";
_sb.Append("<a href='http://").Append(BA.NumberToString(_i)).Append(".");
 //BA.debugLineNum = 148;BA.debugLine="sb.Append(row)";
_sb.Append(BA.NumberToString(_row));
 //BA.debugLineNum = 149;BA.debugLine="sb.Append(\".com'>\").Append(cur.GetString2(i)).";
_sb.Append(".com'>").Append(_cur.GetString2(_i)).Append("</a>");
 }else {
 //BA.debugLineNum = 151;BA.debugLine="sb.Append(cur.GetString2(i))";
_sb.Append(_cur.GetString2(_i));
 };
 //BA.debugLineNum = 153;BA.debugLine="sb.Append(\"</td>\")";
_sb.Append("</td>");
 }
};
 //BA.debugLineNum = 155;BA.debugLine="sb.Append(\"</tr>\").Append(CRLF)";
_sb.Append("</tr>").Append(anywheresoftware.b4a.keywords.Common.CRLF);
 }
};
 //BA.debugLineNum = 157;BA.debugLine="cur.Close";
_cur.Close();
 //BA.debugLineNum = 158;BA.debugLine="sb.Append(\"</table></body></html>\")";
_sb.Append("</table></body></html>");
 //BA.debugLineNum = 159;BA.debugLine="Return sb.ToString";
if (true) return _sb.ToString();
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Private wbvTable As WebView";
mostCurrent._wbvtable = new anywheresoftware.b4a.objects.WebViewWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Dim HtmlCSS As String";
mostCurrent._htmlcss = "";
 //BA.debugLineNum = 31;BA.debugLine="HtmlCSS = \"table {width: 100%;border: 1px solid #";
mostCurrent._htmlcss = "table {width: 100%;border: 1px solid #cef;text-align: left; }"+" th { font-weight: bold;	background-color: #acf;	border-bottom: 1px solid #cef; }"+"td,th {	padding: 4px 5px; }"+".odd {background-color: #def; } .odd td {border-bottom: 1px solid #cef; }"+"a { text-decoration:none; color: #000;}";
 //BA.debugLineNum = 36;BA.debugLine="Private imgSalir As ImageView";
mostCurrent._imgsalir = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="End Sub";
return "";
}
public static String  _imgsalir_click() throws Exception{
 //BA.debugLineNum = 162;BA.debugLine="Sub imgSalir_Click";
 //BA.debugLineNum = 163;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 164;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _showtableinwebview() throws Exception{
String _txt = "";
 //BA.debugLineNum = 75;BA.debugLine="Sub ShowTableInWebView";
 //BA.debugLineNum = 76;BA.debugLine="Private txt As String";
_txt = "";
 //BA.debugLineNum = 78;BA.debugLine="wbvTable.Visible = True";
mostCurrent._wbvtable.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 80;BA.debugLine="If Main.TipoMenuEstad=0 Then";
if (mostCurrent._main._tipomenuestad==0) { 
 //BA.debugLineNum = 81;BA.debugLine="txt = \"SELECT FECPARTIDO, EQUIPOLOC,RESLOC,EQUIP";
_txt = "SELECT FECPARTIDO, EQUIPOLOC,RESLOC,EQUIPOVIS,RESVIS FROM PARTIDOS WHERE IDUSER = 1";
 }else {
 //BA.debugLineNum = 83;BA.debugLine="txt = \"SELECT FECPARTIDO, EQUIPOLOC,EQUIPOVIS,PU";
_txt = "SELECT FECPARTIDO, EQUIPOLOC,EQUIPOVIS,PUNTOS, PERSONALES FROM MIJUGADOR WHERE IDUSER = 1";
 };
 //BA.debugLineNum = 86;BA.debugLine="wbvTable.LoadHtml(ExecuteHtml(Main.SQL1, txt, Nul";
mostCurrent._wbvtable.LoadHtml(_executehtml(mostCurrent._main._sql1,_txt,(String[])(anywheresoftware.b4a.keywords.Common.Null),(int) (0),anywheresoftware.b4a.keywords.Common.True));
 //BA.debugLineNum = 87;BA.debugLine="End Sub";
return "";
}
public static boolean  _wbvtable_overrideurl(String _url) throws Exception{
String[] _values = null;
int _col = 0;
int _row = 0;
 //BA.debugLineNum = 89;BA.debugLine="Sub wbvTable_OverrideUrl (Url As String) As Boolea";
 //BA.debugLineNum = 91;BA.debugLine="Private values() As String";
_values = new String[(int) (0)];
java.util.Arrays.fill(_values,"");
 //BA.debugLineNum = 92;BA.debugLine="values = Regex.Split(\"[.]\", Url.SubString(7))";
_values = anywheresoftware.b4a.keywords.Common.Regex.Split("[.]",_url.substring((int) (7)));
 //BA.debugLineNum = 93;BA.debugLine="Dim col, row As Int";
_col = 0;
_row = 0;
 //BA.debugLineNum = 94;BA.debugLine="col = values(0)";
_col = (int)(Double.parseDouble(_values[(int) (0)]));
 //BA.debugLineNum = 95;BA.debugLine="row = values(1)";
_row = (int)(Double.parseDouble(_values[(int) (1)]));
 //BA.debugLineNum = 96;BA.debugLine="ToastMessageShow(\"User pressed on column: \" & col";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("User pressed on column: "+BA.NumberToString(_col)+" and row: "+BA.NumberToString(_row)),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 97;BA.debugLine="Return True 'Don't try to navigate to this URL";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 98;BA.debugLine="End Sub";
return false;
}
}
