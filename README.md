# TaDo - Sprachgesteuerte Unterstüzungssoftware für Pleger zur Planung und Verwaltung von Aufgabenlisten

## Kurzbeschreibung
Bei TaDo handelt es sich um einen Amazon Alexa-Skill der in Kombination mit einem Alexa fähigen Smart-Device Plegern von Alzheimer Patienten durch 
Spracheingabe in einem Dialog mit Alexa ermöglicht, Aufgabenlisten anzulegen, abzuhaken und Information über bereits abgehakte oder noch zu erledigende Elemente zu bekommen.

## Verwendete Technologie
- Es wurde die Alexa Toolchain in Java eingesetzt 
- Als Datenbank wird DynamoDB verwendet
- Der Endpoint des Skills wird in einer AWS-Lambda-Funktion gehostet
- Das versenden von Mails geschieht über SMTP mit der javax.mail Bibliothek
- Für Unit-Tests wird JUnit eingesetzt

## Features
- Es wird eine Checkliste für Pfleger verwaltet
- Abhandeln von Listeninhalten - Erledigte Aufgaben können als solche markiert und somit im klassichen ToDo-Listen Stil abgehakt werden
- Einmaliges Abhaken eines Elements markiert dieses nur für den aktuellen Tag als erledigt
- Erfragen von Aufgaben die am aktuellen Tag noch offen sind
- Mitverfolgung der Aufgabenfortschritte als Angehöriger oder Patient
- Beispielsweise für die Pflegeleitung kann die heutige Liste (in der die erledigten Elemente einen Haken haben) per Mail versendet werden
- Außerdem kann auch eine Übersicht über die jeweils abgehakten Elemente der letzten 7 Tage (Wochenübersicht) verschickt werden
