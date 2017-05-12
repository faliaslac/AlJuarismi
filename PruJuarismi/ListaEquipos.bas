Type=Activity
Version=6.8
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: False
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

	Public equipo As String
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.

	Private ListView1 As ListView
	Private lblEquipo As Label
	Private lblId As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)

	Activity.LoadLayout("ListaEquipos")
	
	
	ListViewInit
	ListViewFill
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub ListViewInit
	ListView1.TwoLinesLayout.ItemHeight = 40dip
	ListView1.TwoLinesLayout.Label.Left = 0
	ListView1.TwoLinesLayout.Label.Width = 10%x
	ListView1.TwoLinesLayout.Label.Height = 40dip
	ListView1.TwoLinesLayout.Label.Gravity = Gravity.CENTER_VERTICAL
	ListView1.TwoLinesLayout.Label.Color = Colors.Blue
	ListView1.TwoLinesLayout.Label.TextSize = 18
    
	ListView1.TwoLinesLayout.SecondLabel.Top = 0
	ListView1.TwoLinesLayout.SecondLabel.Left = 10%x
	ListView1.TwoLinesLayout.SecondLabel.Width = 90%x
	ListView1.TwoLinesLayout.SecondLabel.Height = 40dip
	ListView1.TwoLinesLayout.SecondLabel.Gravity = Gravity.CENTER_VERTICAL
	ListView1.TwoLinesLayout.SecondLabel.Color = Colors.Blue
	ListView1.TwoLinesLayout.SecondLabel.TextColor = Colors.White
	ListView1.TwoLinesLayout.SecondLabel.TextSize = 18
End Sub

Sub ListViewFill
	Private Query As String
	
	Query = "SELECT Id, equipo FROM " & Main.DBTableEquipos

	ExecuteListView(Main.SQL1, Query, Null, 0, ListView1, True)
End Sub

Sub ListView1_ItemClick (Position As Int, Value As Object)
	Dim elementos() As String
	
	elementos=ListView1.GetItem(Position)
	If equipo ="Local" Then
		Marcador.idLocal=elementos(0)
		Marcador.strLocal = elementos(1)
	Else
		Marcador.idVis=elementos(0)
		Marcador.strVis= elementos(1)
	End If

	Activity.Finish
	End Sub

'Executes the query and fills the ListView with the value.
'If TwoLines is true then the first column is mapped to the first line and the second column is mapped
'to the second line.
'In both cases the value set to the row is the array with all the records values.
Sub ExecuteListView(SQL2 As SQL, Query As String, StringArgs() As String, Limit As Int, ListView2 As ListView, TwoLines As Boolean)
	ListView1.Clear
	Dim Table As List
	Table = ExecuteMemoryTable(SQL2, Query, StringArgs, Limit)
	Dim Cols() As String
	For i = 0 To Table.Size - 1
		Cols = Table.Get(i)
		If TwoLines Then
			ListView2.AddTwoLines2(Cols(0), Cols(1), Cols)
		Else
			ListView2.AddSingleLine2(Cols(0), Cols)
		End If
	Next
End Sub


'Executes the query and returns the result as a list of arrays.
'Each item in the list is a strings array.
'StringArgs - Values to replace question marks in the query. Pass Null if not needed.
'Limit - Limits the results. Pass 0 for all results.
Sub ExecuteMemoryTable(SQL As SQL, Query As String, StringArgs() As String, Limit As Int) As List
	Dim cur As Cursor
	If StringArgs <> Null Then
		cur = SQL.ExecQuery2(Query, StringArgs)
	Else
		cur = SQL.ExecQuery(Query)
	End If
	Log("ExecuteMemoryTable: " & Query)
	Dim table As List
	table.Initialize
	If Limit > 0 Then Limit = Min(Limit, cur.RowCount) Else Limit = cur.RowCount
	For row = 0 To Limit - 1
		cur.Position = row
		Dim values(cur.ColumnCount) As String
		For col = 0 To cur.ColumnCount - 1
			values(col) = cur.GetString2(col)
		Next
		table.Add(values)
	Next
	cur.Close
	Return table
End Sub