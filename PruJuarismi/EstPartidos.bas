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
	
	'Public DBFileName As String				: DBFileName = "persons.db"
	'	Public DBFileDir As String					: DBFileDir = File.DirInternal
	'Public DBFileDir As String			'		: DBFileDir = File.DirRootExternal
	
	'Public DBTableName As String				: DBTableName = "persons"
	
	'Public SQL1 As SQL
	
	Private wbvTable As WebView
	'Private ltvLastFirstName As ListView
	
	
	

	Dim HtmlCSS As String
	HtmlCSS = "table {width: 100%;border: 1px solid #cef;text-align: left; }" _
		& " th { font-weight: bold;	background-color: #acf;	border-bottom: 1px solid #cef; }" _ 
		& "td,th {	padding: 4px 5px; }" _
		& ".odd {background-color: #def; } .odd td {border-bottom: 1px solid #cef; }" _
		& "a { text-decoration:none; color: #000;}"
	Private imgSalir As ImageView
	Private Label1 As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	Activity.LoadLayout("EstPartidos")
	
	If Main.TipoMenuEstad=0 Then
		Label1.Text= "Resultados de los partidos"
	Else
		Label1.Text= "Resultados de Mi Jugador"
	End If
	
	'	File.Delete(File.DirRootExternal, DBFileName) ' for testing
	If FirstTime Then
		'DBFileDir = DBUtils.CopyDBFromAssets(DBFileName)
	End If
	Main.SQL1.Initialize(Main.DBFileDir, Main.DBFileName, True)
	

	
	wbvTable.Initialize("wbvTable")
	Activity.AddView(wbvTable, 0, 30%y, 100%x, 70%y)

	ShowTableInWebView
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub




Sub ShowTableInWebView
	Private txt As String
	
	wbvTable.Visible = True
	
	If Main.TipoMenuEstad=0 Then
		txt = "SELECT FECPARTIDO, EQUIPOLOC,RESLOC,EQUIPOVIS,RESVIS FROM PARTIDOS WHERE IDUSER = 1"
	Else
		txt = "SELECT FECPARTIDO, EQUIPOLOC,EQUIPOVIS,PUNTOS, PERSONALES FROM MIJUGADOR WHERE IDUSER = 1"
	End If
	
	wbvTable.LoadHtml(ExecuteHtml(Main.SQL1, txt, Null, 0, True))
End Sub

Sub wbvTable_OverrideUrl (Url As String) As Boolean
	'parse the row and column numbers from the URL
	Private values() As String
	values = Regex.Split("[.]", Url.SubString(7))
	Dim col, row As Int
	col = values(0)
	row = values(1)
	ToastMessageShow("User pressed on column: " & col & " and row: " & row, False)
	Return True 'Don't try to navigate to this URL
End Sub

Sub cmdVolver_Click
	Activity.Finish
End Sub

'Creates a html text that displays the data in a table.
'The style of the table can be changed by modifying HtmlCSS variable.
Sub ExecuteHtml(SQL As SQL, Query As String, StringArgs() As String, Limit As Int, Clickable As Boolean) As String
	'Dim Table As List
	Dim cur As Cursor
	If StringArgs <> Null Then
		cur = SQL.ExecQuery2(Query, StringArgs)
	Else
		cur = SQL.ExecQuery(Query)
	End If
	Log("ExecuteHtml: " & Query)
	If Limit > 0 Then Limit = Min(Limit, cur.RowCount) Else Limit = cur.RowCount
	Dim sb As StringBuilder
	sb.Initialize
	sb.Append("<html><body>").Append(CRLF)
	sb.Append("<style type='text/css'>").Append(HtmlCSS).Append("</style>").Append(CRLF)
	sb.Append("<table><tr>").Append(CRLF)
	
	If Main.TipoMenuEstad=0 Then
		sb.Append("<th>").Append("Fecha").Append("</th>")
		sb.Append("<th>").Append("Local").Append("</th>")
		sb.Append("<th>").Append("Puntos").Append("</th>")
		sb.Append("<th>").Append("Visitante").Append("</th>")
		sb.Append("<th>").Append("Puntos").Append("</th>")
	Else
		sb.Append("<th>").Append("Fecha").Append("</th>")
		sb.Append("<th>").Append("Local").Append("</th>")
		sb.Append("<th>").Append("Visitante").Append("</th>")
		sb.Append("<th>").Append("Puntos").Append("</th>")
		sb.Append("<th>").Append("Personales").Append("</th>")
	End If
	
	sb.Append("</tr>").Append(CRLF)
	For row = 0 To Limit - 1
		cur.Position = row
		If row Mod 2 = 0 Then
			sb.Append("<tr>")
		Else
			sb.Append("<tr class='odd'>")
		End If
		For i = 0 To cur.ColumnCount - 1
			sb.Append("<td>")
			If Clickable Then
				sb.Append("<a href='http://").Append(i).Append(".")
				sb.Append(row)
				sb.Append(".com'>").Append(cur.GetString2(i)).Append("</a>")
			Else
				sb.Append(cur.GetString2(i))
			End If
			sb.Append("</td>")
		Next
		sb.Append("</tr>").Append(CRLF)
	Next
	cur.Close
	sb.Append("</table></body></html>")
	Return sb.ToString
End Sub

Sub imgSalir_Click
	Activity.Finish
End Sub