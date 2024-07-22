package ru.iedt.database.request.parser.elements.v3.stax;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

public class StaxStreamValidator {
private static final SchemaFactory factory =
	SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
private static final Schema schema;

static {
	try {
	schema =
		factory.newSchema(
			new File(
				Objects.requireNonNull(StaxStreamValidator.class.getResource("/Definitions.xsd"))
					.toURI()
					.getPath()));
	} catch (SAXException | URISyntaxException e) {
	// TODO логирование ошибки статической инициализации
	throw new RuntimeException(e);
	}
}

public static Exception staxStreamValidateSchema(Path paths) {
	try {
	Validator validator = schema.newValidator();
	validator.validate(new StreamSource(Files.newInputStream(paths)));
	return null;
	} catch (SAXException | IOException e) {
	// TODO логирование ошибки
	// return e;
	return null;
	}
}
}
