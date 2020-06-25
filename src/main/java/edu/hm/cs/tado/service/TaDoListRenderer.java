package edu.hm.cs.tado.service;

import edu.hm.cs.tado.model.TaDoList;
import edu.hm.cs.tado.model.TodoElement;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Renderer, der eine TaDoListe zu HTML rendern kann.
 */
public class TaDoListRenderer {

    private static final String HEADER = "<!DOCTYPE HTML> <html> <head>" +
            "<meta charset=\"UTF-8\"> </head> <body>";
    private static final String FOOTER = "</body> </html>";
    private static final char CHECK_CHAR = '✓';
    private static final String EMAIL_HEADLINE = "<h1>Ihre angeforderte TaDo Liste</h1>";
    private static final String WEEK_REPORT_HEADLINE = "<h1>Ihr Wochenbericht</h1>";
    public static final String TABLE_STYLE = " style='border-collapse: collapse;'";
    public static final String ELEMENT_STYLE = " style='border: 1px solid black; text-align: center; padding: 5px;'";


    private TaDoList list;

    public TaDoListRenderer(TaDoList list) {
        this.list = list;
    }

    public String render() {
        return renderReport(EMAIL_HEADLINE + renderListToHtml());
    }

    public String renderWeek() {
        return renderReport(WEEK_REPORT_HEADLINE + renderListWeekreportToHtml());
    }

    private String renderReport(String content) {
        String document = HEADER;

        document += content;

        return document + FOOTER;
    }

    private String renderListToHtml() {
        StringBuilder listHtml = new StringBuilder("<table><tbody>");
        for (TodoElement element : list.getElements()) {
            listHtml.append(getSingleEntryLine(element));
        }
        return listHtml + "</tbody></table>";
    }

    private String renderListWeekreportToHtml() {
        StringBuilder listHtml = new StringBuilder("<table" + TABLE_STYLE + ">");
        listHtml.append(getWeekreportHeader());
        listHtml.append("<tbody>");
        for (TodoElement element : list.getElements()) {
            listHtml.append(getSingleWeekreportEntryLine(element));
        }
        return listHtml + "</tbody></table>";
    }

    private String getSingleWeekreportEntryLine(TodoElement element) {
        StringBuilder content = new StringBuilder();
        for (int i = 6; i >= 0; i--) {
            content.append("<td" + ELEMENT_STYLE + ">");
            if (element.isChecked(i)) {
                content.append(CHECK_CHAR);
            }
            content.append("</td>");
        }
        return getEntryLine(element.getName(), content.toString(), ELEMENT_STYLE);
    }

    private String getWeekreportHeader() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        StringBuilder contentBuilder = new StringBuilder("<tr>");
        contentBuilder.append("<th" + ELEMENT_STYLE + ">Element</th>");
        for (int i = 6; i >= 0; i--) {
            contentBuilder.append("<th" + ELEMENT_STYLE + ">");
            contentBuilder.append(LocalDate.now().minus(i, ChronoUnit.DAYS).format(dateFormat));
            contentBuilder.append("</th>");
        }
        contentBuilder.append("</tr>");
        return contentBuilder.toString();
    }

    private String getSingleEntryLine(TodoElement element) {
        String content = "<td>";
        if (element.isCheckedToday()) {
            content += CHECK_CHAR;
        }
        content += "</td>";
        return getEntryLine(element.getName(), content, "");
    }

    private String getEntryLine(String name, String content, String attributes) {
        String elementHtml = "<tr><td" + attributes + ">";
        elementHtml += name + "</td>";
        elementHtml += content;
        return elementHtml + "</tr>";
    }

    public static void main(String... args) throws IOException, AddressException {
        TaDoList list = new TaDoList();
        list.addElement(new TodoElement("test"));
        list.getElements().get(0).check();
        MailSenderService mailsender = new MailSenderService();
        mailsender.sendMail("test", new TaDoListRenderer(list).renderWeek(), InternetAddress.parse("mzinsmei@hm.edu"));
    }
}
