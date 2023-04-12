# Riddles-Game-App

Aplicatie client - server folosind multithreading in Java <br>
Interfata grafica a fost dezvoltata folosind JavaFX <br>

Aplicatia foloseste o baza de date PostgreSQL pentru persistenta datelor si gradle pentru adaugarea dependentelor. <br>
Aceasta permite conectarea simultana a mai multor utilizatori precum si actualizarea automata a interfetelor acestora 
prin utilizarea sablonului Observer si al celui Proxy pentru mentinerea conexiunii cu serverul. <br>

Contine urmatoarele module: <br>
Client: interfata grafica plus controller-ele specifice <br>
Domain: entitatile nececesare aplicatiei <br>
Network: modulul prin care se realizeaza conexiunea dintre client si server <br>
Server: implementarea efectiva a serviciilor oferite de aplicatie, acest modul este singurul care comunica cu partea de persistenta a datelor <br>
Repository: partea de persistenta a datelor si operatiile necesare aplicatiei ce implica accesarea si extragerea elementelor din baza de date <br>
Services: interfetele pentru servicii si client observer <br>

Flow-ul aplicatiei presupune conectarea unui utilizator la server se trimite un request prin intermediul Proxy-ului. <br>
Serverul creeaza un Worker Thread pentru deservirea clientului ce comunica printr-un socket cu Proxy-ul de la nivelul clientului pe toata durata
in care acesta ramane autentificat in aplicatie. <br>
Extragerea informatiei/input-urilor utilizatorului din interfata grafica se face prin intermediul controller-elor care apoi se adreseaza Proxy-ului <br>

Interfata grafica -> Controller -> Proxy -> (Socket: Request/Response) -> Client Worker -> Implementarea serviciilor <br>

Aplicatia, de asemenea, se utilizeaza de un OpenAPI (https://riddles-api.vercel.app/) pentru a extrage niste ghicitori aleatorii, utilizand
request-uri http. Pentru extragerea informatiilor din formatul Json am utilizat libraria Gson (https://mvnrepository.com/artifact/com.google.code.gson/gson)

Diagrama bazei de date: <br>
![image](https://user-images.githubusercontent.com/108681827/231420166-a1eb3d17-c57b-4ad1-9dc6-07093208d844.png)
