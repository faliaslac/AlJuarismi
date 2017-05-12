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
	Public idLocal As Int
	Public strLocal As String
	Public idVis As Int
	Public strVis As String


End Sub

Sub Globals
	
	Public DBFileName As String				: DBFileName = "marcador.db"
	'	Public DBFileDir As String					: DBFileDir = File.DirInternal
	Public DBFileDir As String			'		: DBFileDir = File.DirRootExternal
	
	
	Private contadorLocal As Int
	Private contadorVisitante As Int
	Private contadorMJ As Int
	Private personalesMJ As Int
	
	Private lblLocPuntos As Label
	Private lblVisPuntos As Label
	
	Private LblOwner As Label
	Private lblMJPuntos As Label
	Private imgEstadistica As ImageView
	Private btnPerMJ1 As Button
	Private btnPerMJm1 As Button
	Private lblPerMJ As Label
	Public txtEquipoLoc As EditText
	Public txtEquipoVis As EditText
	Private btnFinPartido As Button
	Private imgSalir As ImageView
	Private spLocal As Spinner
	Private spVis As Spinner
End Sub

Sub Activity_Create(FirstTime As Boolean)

	Activity.LoadLayout("Marcador")
	New
'	If File.Exists(File.DirInternal,"db.sql") = False Then
'		File.Copy(File.DirAssets,"db.sql",File.DirInternal,"db.sql")
'	End If
'	
'	If SQL1.IsInitialized = False Then
'		SQL1.Initialize(File.DirInternal, "db.sql", False)
'	End If
'	
'	DBload
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub New
	contadorLocal=0
	lblLocPuntos.Text=contadorLocal
	
	
	contadorVisitante=0
	lblVisPuntos.Text=contadorVisitante
	LblOwner.Text="Fecha del partido: " &  DateTime.Date(DateTime.Now)
	'Msgbox ("El usuario seleccionado es " & Main.intUser & "", "")
	If Main.intUser=0 Then
		imgEstadistica.Enabled= False
		btnFinPartido.Enabled=False
	End If
	
	cargaSpLocal
End Sub


Sub muestraPuntosLocal
	lblLocPuntos.Text=contadorLocal
End Sub

Sub muestraPuntosVis
	lblVisPuntos.Text=contadorVisitante
End Sub

Sub muestraPuntosMJ
	lblMJPuntos.text=contadorMJ
End Sub

Sub muestraPersonalesMJ
	If personalesMJ>5 Then
		Msgbox("No se pueden tener más de 5 faltas personales","")
		personalesMJ=5
	End If
	lblPerMJ.Text=personalesMJ
End Sub

'*****
'* tratamiento de eventos de formulario
'*****

Sub lblMJPuntos_Click
	contadorMJ=contadorMJ+2
	muestraPuntosMJ
End Sub

Sub btnMJPm1_Click
	contadorMJ=contadorMJ-1
	muestraPuntosMJ
End Sub

Sub btnMJP1_Click
	contadorMJ=contadorMJ+1
	muestraPuntosMJ
End Sub

Sub imgEstadistica_Click
	StartActivity(MenuEstad)
End Sub

Sub lblPerMJ_Click
	personalesMJ=personalesMJ+1
	muestraPersonalesMJ
End Sub

Sub btnPerMJm1_Click
	personalesMJ=personalesMJ-1
	muestraPersonalesMJ
End Sub

Sub btnPerMJ1_Click
	personalesMJ=personalesMJ+1
	muestraPersonalesMJ
End Sub

Sub btnVism1_Click
	contadorVisitante=contadorVisitante-1
	muestraPuntosVis
End Sub

Sub btnVis1_Click
	contadorVisitante=contadorVisitante+1
	muestraPuntosVis
End Sub

Sub btnLocm1_Click
	contadorLocal=contadorLocal-1
	muestraPuntosLocal
End Sub

Sub btnLoc1_click
	contadorLocal=contadorLocal+1
	muestraPuntosLocal
End Sub

Sub lblVisPuntos_Click
	contadorVisitante=contadorVisitante+2
	muestraPuntosVis
End Sub

Sub lblLocPuntos_Click
	contadorLocal=contadorLocal+2
	muestraPuntosLocal
End Sub

Sub btnBorrar_Click
	contadorLocal=0
	contadorVisitante=0
	contadorMJ=0
	muestraPuntosLocal
	muestraPuntosVis
	muestraPuntosMJ
End Sub




Sub btnFinPartido_Click
	Dim strSQL As String
	strSQL = "INSERT INTO PARTIDOS (IDUSER, FECPARTIDO, EQUIPOLOC, EQUIPOVIS, RESLOC, RESVIS) VALUES(" & Main.intUser &",'" & _
	DateTime.Date(DateTime.Now) & "','" &  strLocal & "','" & strVis & "'," & contadorLocal & "," & contadorVisitante & ")" 
	Main.SQL1.ExecNonQuery(strSQL)
	
	strSQL = "INSERT INTO MIJUGADOR (IDUSER, FECPARTIDO, EQUIPOLOC, EQUIPOVIS, PUNTOS, PERSONALES) VALUES(" & Main.intUser &",'" & _
	DateTime.Date(DateTime.Now) & "','" &  strLocal & "','" & strVis & "'," & contadorMJ & "," & personalesMJ & ")" 
	Main.SQL1.ExecNonQuery(strSQL)
	
	Msgbox("Resultados guardados","")
	
End Sub



Sub imgSalir_Click
	Activity.Finish
End Sub

'*****
'* inicializar los combo de equipos
'*****
Sub cargaSpLocal
	
	Dim strSQL As String = "SELECT equipo FROM " & Main.DBTableEquipos
	
	Main.cursor1 = Main.SQL1.ExecQuery(strSQL)
	
	For i = 0 To Main.cursor1.RowCount - 1
		Main.cursor1.Position = i
		
		spLocal.Add(Main.cursor1.GetString("Equipo"))
		spVis.Add(Main.cursor1.GetString("Equipo"))
		
	Next
	spLocal.Prompt="Local"
	spVis.Prompt="Visitante"
End Sub

Sub spLocal_ItemClick (Position As Int, Value As Object)
	strLocal=spLocal.SelectedItem
End Sub

Sub spVis_ItemClick (Position As Int, Value As Object)
	strVis=spVis.SelectedItem
End Sub