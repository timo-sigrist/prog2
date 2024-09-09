# Multichat
Dies ist ein Multichat programmiert in Java. Aufgabe für dieses Projekt ist die Analyse des ursprünglichen Codings sowie das Refactoring und die Dokumentation des Codes. In diesem README werden das Vorgehen, die Fehler-/Probleme sowie die Lösung dokumentiert.

## 1 Projekt Regeln ⏩
Folgende Regeln müssen beim Mitwirken in diesem Projekt eingehalten werden.
### 1.1 Commits
In den Commits muss nach folgende Aufbau gearbeitet werden.
```
Typ: Titel

Body

Footer
```
#### Typ
Der Typ muss hierbei einem der folgenden Typen entsprechen:
* **feat:** Ein neues Feature
* **fix:** Die Behebung eines Bugs
* **docs:** Ergänzung einer Dokumentation
* **refactor:** Refactoring von Code
#### Titel
Der Titel ist eine kurze Zusammenfassung der Änderungen
#### Body
Im Body werden alle Änderungen genau beschrieben. Dabei wird vor allem das _was_ und teilweise auch das _wieso_ beantwortet
#### Footer
Im Footer wird angegeben, welche Issues/Pull Request dieser Commit betraf.
#### Ausnahmen
In Commits, in welchen ein Branch in den anderen gemerged wird oder in welchem nur das README.md angepasst wird, kann obiger Aufbau weggelassen werden, da bei diesen Aktionen bei beiden immer dasselbe gemacht wird
### 1.2 Branching
Grundsätzlich wird im gesamten Projekt mit zwei Branches `main` und `develop` gearbeitet. `main` ist der Hauptbranch und im `develop` werden laufend die Änderungen der unterliegenden Branches reingemerged. Neben diesen Branches wurden die Issues in verschiedene Sub-Branches gruppiert. Diese entsprechen alle dem folgenden Aufbau [Typ]/[Gruppierung]. Die Typen entsprechen hierbei wiederum den Typen der Commits. Die Gruppierung kann frei definiert werden. 
#### `develop` <- Sub-Branches
Sobald die Issues zu einem Sub-Branch erledigt sind, so kann dieser Sub-Branch in den `develop` Branch gemerged werden. Diese Branches entsprechen den gleichnamigen Issue-Labels.
#### `main` <- `develop`
Sobald die Applikation bereit zu einem Release ist, kann der aktuelle Stand vom `develop` in den `main` Branch gemerged werden.

## 2 Fehler-/Probleme 💢
In diesem Bereich werden die wichtigsten 5 essenziellen funktionalen Fehler und 5 struktruelle Fehler dokumentiert.
### 2.1 Funktionale Fehler
* [#2](/../../issues/2) Server lässt sich nur mit einem Client gleichzeitig verbinden
* [#18](/../../issues/18) Beim Beenden des Servers wird dieser nicht korrekt von den Clients abgemeldet
* [#20](/../../issues/20) Protokollierung wird mit System.out.println geschrieben
* [#22](/../../issues/22) Direkt-Nachricht an Anonymous-User funktioniert nicht 
* [#23](/../../issues/23) Direkt-Nachricht beim Sender nicht sichtbar 
### 2.2 Strukturelle Probleme
* [#1](/../../issues/1), [#3](/../../issues/3), [#4](/../../issues/4), [#5](/../../issues/5), [#8](/../../issues/8), [#29](/../../issues/29) [zusammmengefasst] Erstellung der Oberklasse ConnectionHandler
* [#6](/../../issues/6) Model View Controller Konzept wird verletzt - kein Model
* [#9](/../../issues/9) Dokumentation (JavaDoc) fehlt in allen Klasse
* [#11](/../../issues/11) Gesendete Daten werden als String versendet
* [#36](/../../issues/36) Konsolenausgaben und Messagetexte sind direkt im Code hinterlegt

## 3 Lösung 🔥
### 3.1 Klassendiagramm
Im Klassendiagramm wurden übersichtshalber alle Abhängigkeiten zwischen den Klassen weggelassen. Bloss die Abhängigkeiten von inneren Klasssen und Vererbungen werden dargestellt. Da wir die `NetworkHandler` Klasse nicht selbst programmiert haben, wurden die Attribute und Methoden zu diesem weggelassen.
![class diagramm](https://github.zhaw.ch/PM2-IT21tbWIN-scmy-pero-pasu/Uebung-hk2-baumgnoa-sigritim/blob/refactor/overall/docs/classDiagram.drawio.png)
### 3.2 Struktur
Die Struktur hat sich im Gegensatz zum vorgegebenen Code der ZHAW an einigen Stellen geändert. Die wichtigsten Änderungen und die Begründung für diese werden hier erfasst.
#### Modul `protocol` wurde zu `base`
Den grundsätzlichen Aufbau des Projekts in drei unterschiedliche Module haben wir beibehalten. Da es Sinn ergibt das Modul `server` vom Modul `client` zu separieren. Dabei stellt `protocol` das Modul dar, auf welches die beiden anderen Module zugreifen können. Bei diesem Aufbau waren wir einzig mit der Namensgebung des Moduls `protocol` unzufrieden, da nicht alle enthalten Klassen (teilweise auch neue Klasse) etwas mit dem Protokoll zu tun haben. Aus diesem Grund haben wir `protocol` zu `base` umbenennt, da dieses Modul aus unserer Sicht die Basis (en. base) für den Server und den Client darstellt.
#### Neue Klassen in den Modulen `base` und `server`
Im Projekt wurden einige neue Klassen ergänzt. Im Modul `server` kam der `ServerManager` hinzu. Dieser dient dazu, die Main-Methode aus dem Server auszulagern und das Projekt vorzubereiten für eine Erweiterung von mehreren Servern, welche auf verschiedenen Ports laufen (siehe _3.4 Mögliche Erweiterungsmöglichkeiten_).<br>
Im umbenannten Modul `base` kamen gleich drei neue Klassen hinzu. Die Klasse `NetworkMessage` repräsentiert die serialisierte Netzwerk-Nachricht, welche versendet wird. Hier wurde darauf geachtet, dass diese nur als Datenträger fungiert. Das heisst, dass das struktuirete Datenobjekt nur typisierte Datenfelder hat und keine Logik beinhaltet. Die Klasse `ConnectionHandler` stellt die Oberklasse der Klassen `ClientConnectionHandler` und `ServerConnectionHandler` dar. Diese wurde erstellt, da die beiden Subklassen viel ähnlichen oder sogar identischen Code besassen. Die Klasse `Config` beinhaltet alle Text-Elemente, welche entweder der Konsole oder dem UI übergeben werden, sowie alle Konstanten, die in diesem Projekt gebraucht werden.
#### Umbenannte Klasse im Modul `client`
Die Klassennamen der bestehenden Klassen entsprachen soweit unserem Gusto. Bloss die Klasse `ClientMessageList` wurde umbenannt auf `ClientMessageModel`, damit klar ist, dass es sich um das Model von MVC handelt.
### 3.3 Erwähnenswerten Eigenschaften / Lösungsansätze
#### KISS ("Keep it simple, stupid")
Unser Ziel war es vor allem das Projekt möglichst zu vereinfachen, damit der Code bereits ohne Dokumentation einfach lesbar ist. Und dies ist uns auch gelungen. Ein Beispiel hierzu ist die Klasse `NetworkMessage`. Diese ersetzt die vier Attribute _sender_, _receiver_, _type_ und _payload_ durch ein einzelnes Objekt und der Name sagt bereits selbst direkt aus, wozu die Klasse dient und was es ist.
#### Erweiterbarkeit
Unser Projekt zeichnet sich auch aus durch die Erweiterbarkeit. Dadurch, dass wir Texte und Konstanten zentralisiert, Attribute durch Objekte ersetzt (wie am Beispiel `NetworkMessage`) und einige Funktionalität in zusätzliche Klassen ausgelagert haben, ist das Projekt einfach erweiterbar.
### 3.4 Mögliche Erweiterungsmöglichkeiten
Die Lösung bietet verschiedene Erweiterungmöglichkeiten.
#### `ServerManager`
Der `Server` wird aktuell gestartet über den `ServerManager`. Letzterer wurde mit der Absicht erstellt, dass dieser in Zukunft die Erstellung von mehreren Servern zulassen kann. Damit kann der ServerManager in Zukunft so erweitert werden, dass verschiedene Server auf verschiedenen Ports laufen. Damit wären bspw. analog zu Teams oder auch Discord mehrere 'Channels' möglich.
#### `NetworkMessage`
Da im Coding überall ein Objekt der `NetworkMessage` übertragen wird und nicht mehr einzelne Komponenten der Message, kann diese `NetworkMessage` einfach bspw. um ein Attribut `picture` oder ähnliches ergänzt werden.
#### `Config`
In der Klasse `Config` wurden neben Konstanten auch alle Texte festgehalten. Durch diese Zentralisierung der Texte bietet sich auch die Internationalisierung (Mehrsprachigkeit) als Erweiterungsmöglichkeit an.

## 4 Team-Mitglieder 🧑🏽‍💻 
* Noah (baumgnoa@students.zhaw.ch)
* Timo (sigritim@students.zhaw.ch)
