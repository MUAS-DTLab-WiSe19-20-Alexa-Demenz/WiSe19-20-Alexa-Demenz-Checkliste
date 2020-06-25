package edu.hm.cs.tado;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PhrasesAndConstants {


    private PhrasesAndConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static final String CARD_TITLE = "TaDo";
    public static final String LISTTITLE_SLOT = "";


    //HELP
    public static final String HELP_CREATE_LIST = "Um TaDo nuzen zu könen must du erst eine Liste anlegen.\n" +
            "Sag dazu einfach: 'lege eine liste an'."; //called if no list is Present
    public static final String HELP = "Nachdem du eine Liste erstellt hast, kannst du Aufgaben hinzufügen, löschen und täglich abhaken." +
            "Ich kann dir sagen, was du heute schon erledigt hast oder was noch zu erledigen ist." +
            "Du kannst dir auch eine tägliche oder wöchentliche Liste per Email senden lassen.";
    public static final String WELCOME = "Hallo. Hier ist TaDo.";
    public static final String CANCEL_AND_STOP = "Auf Wiedersehen";
    public static final String LIST_ALREADY_EXISTS = "Es existiert bereits eine Liste. Aktuell ist es nicht möglich, " +
            "mehr als eine Liste anzulegen. Löschen Sie zuerst die bestehende Liste wenn sie eine neue Liste anlegen wollen";

    public static final String LIST_FILLED = "Die Liste wurde gefüllt";


    public static final String LIST_MISSING = "Die Liste wurde noch nicht erstellt";

    public static final String MAIL_SENT = "Die Liste wurde verschickt";
    public static final String CANT_SEND_MAIL = "Leider ist beim versenden der Liste ein Fehler aufgetreten";
    public static final String GIVE_MAIL_PERMISSION = "Bitte vergeben sie die Berechtiung, ihre" +
            " Email-Addresse einzusehen";

    public static final String LIST_CREATED = "Die Liste wurde erstellt";
    public static final String LIST_DELETED = "Die Liste wurde gelöscht";
    public static final String THERE_ARE_NO_LISTS = "Aktuell werden keine Listen von TaDo verwaltet! " +
            "Legen Sie eine Liste an um fortzufahren!";
    public static final String GOOD_BYE = "Auf Wiedersehen";

    public static final String ELEMENT_NAME = "elementName";


    public static final String NO_SUCH_ELEMENT = "Dieses Element gibt es nicht";
    public static final String ELEMENT_CHECKED = "Das Element wurde als erledigt markiert";
}
