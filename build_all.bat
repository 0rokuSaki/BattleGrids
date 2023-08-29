:: Create a directory for the compiled files
mkdir out

:: Compile the source files
javac -d out src\Shared\*.java src\Client\Controllers\*.java src\Client\*.java src\Server\GameSession\*.java src\Server\*.java

:: Copy FXML files
mkdir out\Client\fxml
copy src\Client\fxml\*.fxml  out\Client\fxml
pause