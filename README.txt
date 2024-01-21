#1 Für unsere tabellen in sql einfach zu src/sql navigieren und in mysql workbench einbinden, vorher in mysql workbench die datenbank plotarmor erstellen mit
"create database plotarmor;"
#2 Mysql server mit username "root" und passwort "root" einrichten.

--------
#Für das erstellen der executable file von plotarmor:

1. File->Project Structure->Artifacts-> "+" -> "JAR" -> "From modules without dependencies -> Main Class auswählen
->directory for Meta-INF = "blabla/src" -> "+" -> File -> "\plotarmor\libraries\javafx-sdk-21.0.1\bin\" -> alle files auswählen
und dann auf OK klicken -> "output directory = "wo du die plotarmor.jar abspeichern möchtest" -> apply und auf ok ->
Bild -> build artifacts -> Build.
2. installiere launch4j und öffne es -> output file ist der pfad wo du die exe speichern möchtest -> jar wo die plotarmor.jar liegt auswählen
-> "JRE" -> "JRE Paths" eingeben bsp: "C:\Program Files (x86)\Java\jdk-20.0.1"
3. JVM options einfügen: "--module-path "PFAD TO \plotarmor\libraries\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml"
