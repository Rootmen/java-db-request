package ru.iedt.database.request.parser.elements.v3.stax;

import java.io.InputStream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.StreamReaderDelegate;

public class StaxStreamProcessor implements AutoCloseable {
private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

private final XMLStreamReader reader;

public StaxStreamProcessor(InputStream is) throws XMLStreamException {
	reader = new InsensitiveReaderDelegate(FACTORY.createXMLStreamReader(is));
}

public XMLStreamReader getReader() {
	return reader;
}

@Override
public void close() {
	if (reader != null) {
	try {
		reader.close();
	} catch (XMLStreamException ignored) {
	}
	}
}

private static class InsensitiveReaderDelegate extends StreamReaderDelegate {

	XMLStreamReader reader;

	public InsensitiveReaderDelegate(XMLStreamReader reader) {
	super(reader);
	this.reader = reader;
	}

	@Override
	public String getAttributeLocalName(int index) {
	return super.getAttributeLocalName(index).toLowerCase();
	}

	@Override
	public String getAttributeNamespace(int index) {
	return super.getAttributeNamespace(index).toLowerCase();
	}

	@Override
	public String getLocalName() {
	return super.getLocalName().toLowerCase();
	}

	@Override
	public String getAttributeValue(String namespaceURI, String localName) {
	int code = this.reader.getEventType();
	boolean isHasAttribute = code == XMLEvent.START_ELEMENT || code == XMLEvent.ATTRIBUTE;
	if (isHasAttribute) {
		int attributesCount = this.reader.getAttributeCount();
		for (int g = 0; g < attributesCount; g++) {
		QName qName = this.reader.getAttributeName(g);
		if (namespaceURI == null) {
			if (qName.getLocalPart().toLowerCase().equals(localName)) {
			return this.reader.getAttributeValue(g);
			}
		} else {
			if (qName.getLocalPart().toLowerCase().equals(localName)
				&& qName.getNamespaceURI().toLowerCase().equals(namespaceURI)) {
			return this.reader.getAttributeValue(g);
			}
		}
		}
	} else {
		throw new IllegalStateException(
			"Current state is not among the states XMLEvent.START_ELEMENT and XMLEvent.ATTRIBUTE valid for getAttributeValue()");
	}
	return null;
	}
}
}
