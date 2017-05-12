Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: True
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private btnMJ As Button
	Private btnPartidos As Button
	Private imgAtras As ImageView
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Activity.LoadLayout("MenuEstad")
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)
	If UserClosed Then
		Activity.Finish
	End If
End Sub


Sub imgAtras_Click
	Activity.Finish
End Sub

Sub btnPartidos_Click
	Main.TipoMenuEstad=0
	StartActivity(EstPartidos)
End Sub

Sub btnMJ_Click
	Main.TipoMenuEstad=1
	StartActivity(EstPartidos)
End Sub