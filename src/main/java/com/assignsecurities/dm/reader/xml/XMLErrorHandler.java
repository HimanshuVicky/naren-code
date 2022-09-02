package com.assignsecurities.dm.reader.xml;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLErrorHandler extends DefaultHandler
{
	
    private  List<StringBuilder> displayTextList = new ArrayList<StringBuilder>();
    private int numberLines = 0;
    private StringBuilder indentation = new StringBuilder("");
    public static void main(String args[])
    {
    	XMLErrorHandler obj = new XMLErrorHandler();
        obj.childLoop(args[0]);

        try {
            FileWriter filewriter = new FileWriter("new.xml");


               for(int loopIndex = 0; loopIndex < obj.numberLines; loopIndex++){

                   filewriter.write(obj.displayTextList.get(loopIndex).toString().toCharArray());

                   filewriter.write('\n');

               }


               filewriter.close();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void childLoop(String uri)
    {
        DefaultHandler handler = this;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(uri), handler);
        } catch (Throwable t) {}
    }

    public void startDocument()
    {
        displayTextList.add(indentation);
        displayTextList.get(numberLines).append( "<?xml version=\"1.0\" encoding=\""+
            "UTF-8" + "\"?>");
        numberLines++;
    }

    public void processingInstruction(String target, String data)
    {
        displayTextList.add( indentation);
        displayTextList.get(numberLines).append( "<?");
        displayTextList.get(numberLines).append(target);
        if (data != null && data.length() > 0) {
            displayTextList.get(numberLines).append(' ');
            displayTextList.get(numberLines).append(data);
        }
        displayTextList.get(numberLines).append("?>");
        numberLines++;
    }

    public void startElement(String uri, String localName,
        String qualifiedName, Attributes attributes)
    {
        displayTextList.add(indentation);

        indentation.append( "    ");

        displayTextList.get(numberLines).append('<');
        displayTextList.get(numberLines).append( qualifiedName);
        if (attributes != null) {
            int numberAttributes = attributes.getLength();
            for (int loopIndex = 0; loopIndex < numberAttributes; loopIndex++){
                displayTextList.get(numberLines).append( ' ');
                displayTextList.get(numberLines).append( attributes.getQName(loopIndex));
                displayTextList.get(numberLines).append( "=\"");
                displayTextList.get(numberLines).append( attributes.getValue(loopIndex));
                displayTextList.get(numberLines).append( '"');
            }
        }
        displayTextList.get(numberLines).append( '>');
        numberLines++;
    }

    public void characters(char characters[], int start, int length)
    {
        String characterData = (new String(characters, start, length)).trim();
        if(characterData.indexOf("\n") < 0 && characterData.length() > 0) {
            displayTextList.add(indentation);
            displayTextList.get(numberLines).append( characterData);
            numberLines++;
        }
    }

    public void ignorableWhitespace(char characters[], int start, int length)
    {
        //characters(characters, start, length);
    }

    public void endElement(String uri, String localName, String qualifiedName)
    {
        indentation.replace(0, indentation.length(), indentation.substring(0, indentation.length() - 4) );
        displayTextList.add( indentation);
        displayTextList.get(numberLines).append( "</");
        displayTextList.get(numberLines).append( qualifiedName);
        displayTextList.get(numberLines).append( '>');
        numberLines++;

        if (qualifiedName.equals("lastName")) {

               startElement("", "error", "error", null);

               characters("2004".toCharArray(), 0, "2004".length());

               endElement("", "error", "error");
        }
    }

    public void warning(SAXParseException exception)
    {
        System.err.println("Warning: " +
            exception.getMessage());
    }

    public void error(SAXParseException exception)
    {
        System.err.println("Error: " +
            exception.getMessage());
    }

    public void fatalError(SAXParseException exception)
    {
        System.err.println("Fatal error: " +
            exception.getMessage());
    }
}