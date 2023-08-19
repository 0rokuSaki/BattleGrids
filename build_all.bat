javac -d ./out ./src/Shared/*.java ./src/Client/Controllers/*.java ./src/Client/*.java ./src/Server/GameSession/*.java ./src/Server/*.java
xcopy "src/Client/fxml" "out/Client/fxml" /e/a
pause